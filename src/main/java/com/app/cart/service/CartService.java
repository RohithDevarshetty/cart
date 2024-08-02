package com.app.cart.service;

import com.app.cart.dto.CartItemDTO;
import com.app.cart.dto.CartRequestDTO;
import com.app.cart.dto.CartResponseDTO;
import com.app.cart.exception.CartException;

public interface CartService {

    CartResponseDTO refreshCart(Long usedId);
    CartItemDTO addProducts(CartRequestDTO cartRequestDTO) throws CartException;

    CartItemDTO updateProduct(CartRequestDTO cartRequestDTO);

    void deleteProduct(CartRequestDTO cartRequestDTO);

    void clearCart(Long userId);

    void checkoutProducts(Long userId);

}
