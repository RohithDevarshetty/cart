package com.app.cart.service.impl;

import com.app.cart.dto.CartItemDTO;
import com.app.cart.dto.CartRequestDTO;
import com.app.cart.dto.CartResponseDTO;
import com.app.cart.dto.UserDTO;
import com.app.cart.entity.CartItem;
import com.app.cart.entity.Product;
import com.app.cart.exception.CartException;
import com.app.cart.constants.ErrorCodes;
import com.app.cart.mapper.CartItemMapper;
import com.app.cart.repository.CartRepository;
import com.app.cart.repository.ProductRepository;
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
    private final CartItemMapper cartItemMapper;
    private final ValidatorUtil validatorUtil;
    private final UserService userService;

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, CartItemMapper cartItemMapper, ValidatorUtil validatorUtil, UserService userService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemMapper = cartItemMapper;
        this.validatorUtil = validatorUtil;
        this.userService = userService;
    }

    public CartItemDTO addProducts(CartRequestDTO cartRequestDTO) throws CartException {
        validatorUtil.validateUser(cartRequestDTO.getUserId());
        if(cartRequestDTO.getQuantity()<=0){
            throw new CartException(ErrorCodes.INVALID_INPUT.getCode(), ErrorCodes.INVALID_INPUT.getMessage());
        }
        Optional<Product> productOptional = productRepository.findById(cartRequestDTO.getProductId());
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            Optional<CartItem> cartOptional = cartRepository.findByProductIdAndUserId(cartRequestDTO.getProductId(), cartRequestDTO.getUserId());

            CartItemDTO cartItemDTO;
            if(cartOptional.isPresent()){
                CartItem cartItem = cartOptional.get();
                cartItemDTO = cartItemMapper.toDto(cartItem);
                if(cartRequestDTO.getQuantity() + cartItemDTO.getQuantity() > product.getStockQuantity()){
                    throw new CartException(ErrorCodes.NO_STOCK.getCode(), ErrorCodes.NO_STOCK.getMessage());
                }
                cartItemDTO.setQuantity(cartRequestDTO.getQuantity() + cartItemDTO.getQuantity());
                cartItemDTO.setPrice(product.getUnitPrice());
            }
            else {
                cartItemDTO = new CartItemDTO();
                cartItemDTO.setUserId(cartRequestDTO.getUserId());
                cartItemDTO.setProductId(product.getId());
                cartItemDTO.setQuantity(cartRequestDTO.getQuantity());
                cartItemDTO.setPrice(product.getUnitPrice());
            }
            CartItem savedCartItem = cartRepository.save(cartItemMapper.toEntity(cartItemDTO));
            log.info("Saved Card {}", savedCartItem);
            return cartItemMapper.toDto(savedCartItem);
        } else {
            log.error("Error while processing {}", cartRequestDTO);
            throw new CartException(ErrorCodes.INVALID_INPUT.getCode(), ErrorCodes.INVALID_INPUT.getMessage());
        }
    }


    public CartResponseDTO refreshCart(Long userId){
        validatorUtil.validateUser(userId);
        UserDTO userDTO = userService.getUser(userId);
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        List<CartItem> cartItemList =  cartRepository.findAllByUserId(userId);
        List<CartItemDTO> cartItemDTOList = cartItemList.parallelStream().map(cartItemMapper::toDto).toList();
        cartResponseDTO.setProductList(cartItemDTOList);
        cartResponseDTO.setUser(userDTO);
        double price = 0.0D;
        for(CartItemDTO cartItemDTO: cartItemDTOList){
            price+= cartItemDTO.getQuantity()*cartItemDTO.getPrice();
        }
        cartResponseDTO.setCartPrice(price);
        return cartResponseDTO;
    }

    public CartItemDTO updateProduct(CartRequestDTO cartRequestDTO) {
        validatorUtil.validateUser(cartRequestDTO.getUserId());
        if(cartRequestDTO.getQuantity() <=0){
            throw new CartException(ErrorCodes.INVALID_INPUT.getCode(), ErrorCodes.INVALID_INPUT.getMessage());
        }
        Optional<CartItem> updateItem = cartRepository.findByProductIdAndUserId(cartRequestDTO.getProductId(), cartRequestDTO.getUserId());
        Optional<Product> productOptional = productRepository.findById(cartRequestDTO.getProductId());
        CartItem item;
        if (updateItem.isPresent() && productOptional.isPresent()) {
            Product product = productOptional.get();
            item = updateItem.get();
            if(cartRequestDTO.getQuantity() > product.getStockQuantity()){
                throw new CartException(ErrorCodes.NO_STOCK.getCode(), ErrorCodes.NO_STOCK.getMessage());
            }
            item.setQuantity(cartRequestDTO.getQuantity());
            item.setPrice(item.getProduct().getUnitPrice());
        }else{
            throw new CartException(ErrorCodes.INVALID_INPUT.getCode(), ErrorCodes.INVALID_INPUT.getMessage());
        }
        CartItem savedCartItem = cartRepository.save(item);
        return cartItemMapper.toDto(savedCartItem);
    }

    public void deleteProduct(CartRequestDTO cartRequestDTO) {
        Optional<CartItem> cartOptional = cartRepository.findByProductIdAndUserId(cartRequestDTO.getProductId(), cartRequestDTO.getUserId());
        if(cartOptional.isPresent()){
            CartItem cartItem = cartOptional.get();
            cartRepository.deleteById(cartItem.getId());
        }else{
            throw new CartException(ErrorCodes.INVALID_INPUT.getCode(), ErrorCodes.INVALID_INPUT.getMessage());
        }
    }

    public void clearCart(Long userId) {
        validatorUtil.validateUser(userId);
        List<CartItem> cartItem = cartRepository.findAllByUserId(userId);
        if(cartItem.isEmpty()){
            throw new CartException(ErrorCodes.EMPTY_CART.getCode(), ErrorCodes.EMPTY_CART.getMessage() + userId);
        }
        cartRepository.deleteAll(cartItem);
    }

    public void checkoutProducts(Long userId){
        clearCart(userId);
    }

}
