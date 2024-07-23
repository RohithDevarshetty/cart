package com.app.cart.service;

import com.app.cart.dto.CartDTO;
import com.app.cart.dto.CartRequestDTO;
import com.app.cart.dto.CartResponseDTO;
import com.app.cart.entity.Cart;
import com.app.cart.exception.CartException;

import java.util.List;
public interface CartService {

    CartResponseDTO refreshCart(Long usedId);
    CartDTO addProducts(CartRequestDTO cartRequestDTO) throws CartException;

    CartDTO updateProduct(CartRequestDTO cartRequestDTO);

    void deleteProduct(CartRequestDTO cartRequestDTO);

    void clearCart(Long userId);

    void checkoutProducts(Long userId);

}
