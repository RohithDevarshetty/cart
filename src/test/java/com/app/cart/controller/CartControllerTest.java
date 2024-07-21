package com.app.cart.controller;

import com.app.cart.dto.CartRequestDTO;
import com.app.cart.dto.CartResponseDTO;
import com.app.cart.entity.Cart;
import com.app.cart.exception.CartException;
import com.app.cart.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddProductItem_Success() {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        Cart cart = new Cart();
        when(cartService.addProducts(any(CartRequestDTO.class))).thenReturn(cart);

        Cart result = cartController.addProductItem(cartRequestDTO);

        assertNotNull(result);
        verify(cartService, times(1)).addProducts(any(CartRequestDTO.class));
    }

    @Test
    public void testAddProductItem_Exception() {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        when(cartService.addProducts(any(CartRequestDTO.class))).thenThrow(new RuntimeException("Error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cartController.addProductItem(cartRequestDTO);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Failed to add product to cart", exception.getReason());
        verify(cartService, times(1)).addProducts(any(CartRequestDTO.class));
    }

    @Test
    public void testRefreshCart_Success() {
        Long userId = 1L;
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        when(cartService.refreshCart(anyLong())).thenReturn(cartResponseDTO);

        CartResponseDTO result = cartController.refreshCart(userId);

        assertNotNull(result);
        verify(cartService, times(1)).refreshCart(anyLong());
    }

    @Test
    public void testRefreshCart_Exception() {
        Long userId = 1L;
        when(cartService.refreshCart(anyLong())).thenThrow(new RuntimeException("Error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cartController.refreshCart(userId);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Failed to fetch cart", exception.getReason());
        verify(cartService, times(1)).refreshCart(anyLong());
    }

    @Test
    public void testUpdateProductItem_Success() {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        Cart cart = new Cart();
        when(cartService.updateProduct(any(CartRequestDTO.class))).thenReturn(cart);

        Cart result = cartController.updateProductItem(cartRequestDTO);

        assertNotNull(result);
        verify(cartService, times(1)).updateProduct(any(CartRequestDTO.class));
    }

    @Test
    public void testUpdateProductItem_Exception() {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        when(cartService.updateProduct(any(CartRequestDTO.class))).thenThrow(new RuntimeException("Error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cartController.updateProductItem(cartRequestDTO);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Failed to update cart", exception.getReason());
        verify(cartService, times(1)).updateProduct(any(CartRequestDTO.class));
    }

    @Test
    public void testRemoveItem_Success() {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        doNothing().when(cartService).deleteProduct(any(CartRequestDTO.class));

        cartController.removeItem(cartRequestDTO);

        verify(cartService, times(1)).deleteProduct(any(CartRequestDTO.class));
    }

    @Test
    public void testRemoveItem_Exception() {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        doThrow(new RuntimeException("Error")).when(cartService).deleteProduct(any(CartRequestDTO.class));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cartController.removeItem(cartRequestDTO);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Failed to delete item from cart", exception.getReason());
        verify(cartService, times(1)).deleteProduct(any(CartRequestDTO.class));
    }

    @Test
    public void testClearCart_Success() {
        Long userId = 1L;
        doNothing().when(cartService).clearCart(anyLong());

        cartController.clearCart(userId);

        verify(cartService, times(1)).clearCart(anyLong());
    }

    @Test
    public void testClearCart_Exception() {
        Long userId = 1L;
        doThrow(new RuntimeException("Error")).when(cartService).clearCart(anyLong());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cartController.clearCart(userId);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Failed to clear cart", exception.getReason());
        verify(cartService, times(1)).clearCart(anyLong());
    }

    @Test
    public void testPurchaseProducts_Success() {
        Long userId = 1L;
        doNothing().when(cartService).checkoutProducts(anyLong());

        cartController.purchaseProducts(userId);

        verify(cartService, times(1)).checkoutProducts(anyLong());
    }

    @Test
    public void testPurchaseProducts_Exception() {
        Long userId = 1L;
        doThrow(new RuntimeException("Error")).when(cartService).checkoutProducts(anyLong());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            cartController.purchaseProducts(userId);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Failed while cart checkout", exception.getReason());
        verify(cartService, times(1)).checkoutProducts(anyLong());
    }
}
