package com.app.cart.service;

import com.app.cart.dto.CartDTO;
import com.app.cart.entity.Cart;

import java.util.List;
public interface CartService {

    List<Cart> findAll();
    Cart saveProducts(CartDTO cartDTO);

    Cart updateProduct(CartDTO CartDTO, Long id);

    void deleteProduct(Long id);

    void clearCart(Object object);

    List<Cart> findByPurchased();

    void checkoutProducts(Long id);
}
