package com.app.cart.service.impl;

import com.app.cart.dto.CartDTO;
import com.app.cart.entity.Cart;
import com.app.cart.entity.Product;
import com.app.cart.entity.User;
import com.app.cart.repository.CartRepository;
import com.app.cart.repository.ProductRepository;
import com.app.cart.repository.UserRepository;
import com.app.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    public Cart saveProducts(CartDTO cartDTO) {
        Cart cart = new Cart();
        Optional<Product> productOptional = productRepository.findById(cartDTO.getProductId());
        if(productOptional.isPresent()){
            Product product = productOptional.get();
            cart.setProduct(product);
            Optional<User> userOptional = userRepository.findById(1L);

            if(userOptional.isPresent()) {
                User user = userOptional.get();
                cart.setUser(user);
            }
            cart.setStatus(cartDTO.getStatus());
            cart.setDate(new Date());
            cart.setStock(cartDTO.getStock());
            cart.setAmount(product.getUnitPrice() * cartDTO.getStock());
        }

        return cartRepository.save(cart);
    }


    public List<Cart> findAll() {
        return cartRepository.findByStatus("NOT_PURCHASED");
    }

    public Cart updateProduct(CartDTO CartDTO, Long id) {
        Optional<Cart> updateItem = cartRepository.findById(id);
        Cart item  = new Cart();
        if(updateItem.isPresent()){
            item  = updateItem.get();
            item.setStock(CartDTO.getStock());
            item.setAmount(item.getProduct().getUnitPrice() * CartDTO.getStock());

        }
        return cartRepository.save(item);
    }

    public void deleteProduct(Long id) {
        cartRepository.deleteById(id);
    }

    public void clearCart(Object object) {
        cartRepository.deleteAll(findAll());
    }


    public List<Cart> findByPurchased() {
        return cartRepository.findByStatus("PURCHASED");
    }


    public void checkoutProducts(Long id) {
        Optional<Cart> cartOptional = cartRepository.findById(id);
        Cart cart = cartOptional.get(); //TODO
        cart.setStatus("PURCHASED");
        cartRepository.save(cart);
    }
}
