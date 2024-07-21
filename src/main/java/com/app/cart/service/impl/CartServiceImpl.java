package com.app.cart.service.impl;

import com.app.cart.dto.CartDTO;
import com.app.cart.dto.CartRequestDTO;
import com.app.cart.dto.CartResponseDTO;
import com.app.cart.dto.UserDTO;
import com.app.cart.entity.Cart;
import com.app.cart.entity.Product;
import com.app.cart.entity.User;
import com.app.cart.exception.CartException;
import com.app.cart.constants.ErrorCodes;
import com.app.cart.mapper.CartMapper;
import com.app.cart.repository.CartRepository;
import com.app.cart.repository.ProductRepository;
import com.app.cart.repository.UserRepository;
import com.app.cart.service.CartService;
import com.app.cart.service.UserService;
import com.app.cart.utils.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    private final ValidatorUtil validatorUtil;
    private final UserService userService;

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, CartMapper cartMapper, ValidatorUtil validatorUtil, UserService userService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
        this.validatorUtil = validatorUtil;
        this.userService = userService;
    }

    public Cart addProducts(CartRequestDTO cartRequestDTO) throws CartException {
        validatorUtil.validateUser(cartRequestDTO.getUserId());
        Optional<Product> productOptional = productRepository.findById(cartRequestDTO.getProductId());
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            Optional<Cart> cartOptional = cartRepository.findByProductIdAndUserId(cartRequestDTO.getProductId(), cartRequestDTO.getUserId());

            CartDTO cartDTO;
            if(cartOptional.isPresent()){
                Cart cart = cartOptional.get();
                cartDTO = cartMapper.toDto(cart);
                cartDTO.setQuantity(cartRequestDTO.getQuantity() + cartDTO.getQuantity());
                cartDTO.setPrice(product.getUnitPrice());
            }
            else {
                cartDTO = new CartDTO();
                cartDTO.setUserId(cartRequestDTO.getUserId());
                cartDTO.setProductId(product.getId());
                cartDTO.setQuantity(cartRequestDTO.getQuantity());
                cartDTO.setPrice(product.getUnitPrice());
            }
            Cart savedCart = cartRepository.save(cartMapper.toEntity(cartDTO));
            log.info("Saved Card {}", savedCart);
            return savedCart;
        } else {
            log.error("Error while processing {}", cartRequestDTO);
            throw new CartException(ErrorCodes.INVALID_INPUT.getCode(), ErrorCodes.INVALID_INPUT.getMessage());
        }
    }


    public CartResponseDTO refreshCart(Long userId){
        validatorUtil.validateUser(userId);
        UserDTO userDTO = userService.getUser(userId);
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        List<Cart> cartList =  cartRepository.findAllByUserId(userId);
        List<CartDTO> cartDTOList = cartList.parallelStream().map(cartMapper::toDto).toList();
        cartResponseDTO.setProductList(cartDTOList);
        cartResponseDTO.setUser(userDTO);
        double price = 0.0D;
        for(CartDTO cartDTO: cartDTOList){
            price+= cartDTO.getQuantity()*cartDTO.getPrice();
        }
        cartResponseDTO.setCartPrice(price);
        return cartResponseDTO;
    }

    public Cart updateProduct(CartRequestDTO cartRequestDTO) {
        validatorUtil.validateUser(cartRequestDTO.getUserId());
        if(cartRequestDTO.getQuantity() <=0){
            throw new CartException(ErrorCodes.INVALID_INPUT.getCode(), ErrorCodes.INVALID_INPUT.getMessage());
        }
        Optional<Cart> updateItem = cartRepository.findByProductIdAndUserId(cartRequestDTO.getProductId(), cartRequestDTO.getUserId());
        Cart item = new Cart();
        if (updateItem.isPresent()) {
            item = updateItem.get();
            item.setQuantity(cartRequestDTO.getQuantity());
            item.setPrice(item.getProduct().getUnitPrice());
        }
        return cartRepository.save(item);
    }

    public void deleteProduct(CartRequestDTO cartRequestDTO) {
        Optional<Cart> cartOptional = cartRepository.findByProductIdAndUserId(cartRequestDTO.getProductId(), cartRequestDTO.getUserId());
        if(cartOptional.isPresent()){
            Cart cart  = cartOptional.get();
            cartRepository.deleteById(cart.getId());
        }else{
            throw new CartException(ErrorCodes.INVALID_INPUT.getCode(), ErrorCodes.INVALID_INPUT.getMessage());
        }
    }

    public void clearCart(Long userId) {
        validatorUtil.validateUser(userId);
        List<Cart> cart = cartRepository.findAllByUserId(userId);
        if(cart.isEmpty()){
            throw new CartException(ErrorCodes.EMPTY_CART.getCode(), ErrorCodes.EMPTY_CART.getMessage() + userId);
        }
        cartRepository.deleteAll(cart);
    }

    public void checkoutProducts(Long userId){
        clearCart(userId);
    }

}
