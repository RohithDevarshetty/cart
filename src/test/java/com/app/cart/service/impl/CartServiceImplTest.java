package com.app.cart.service.impl;

import com.app.cart.dto.CartItemDTO;
import com.app.cart.dto.CartRequestDTO;
import com.app.cart.dto.CartResponseDTO;
import com.app.cart.dto.UserDTO;
import com.app.cart.entity.CartItem;
import com.app.cart.entity.Product;
import com.app.cart.entity.User;
import com.app.cart.exception.CartException;
import com.app.cart.constants.ErrorCodes;
import com.app.cart.mapper.CartItemMapper;
import com.app.cart.repository.CartRepository;
import com.app.cart.repository.ProductRepository;
import com.app.cart.service.UserService;
import com.app.cart.utils.ValidatorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemMapper cartItemMapper;

    @Mock
    private ValidatorUtil validatorUtil;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProducts_Success() throws CartException {
        Long userId = 1L;
        Long productId = 2L;
        int quantity = 5;
        Product product = new Product();
        product.setId(productId);
        product.setUnitPrice(10.0);

        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setUserId(userId);
        cartRequestDTO.setProductId(productId);
        cartRequestDTO.setQuantity(quantity);

        User user = new User();
        user.setId(userId);

        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setUserId(userId);
        cartItemDTO.setProductId(productId);
        cartItemDTO.setQuantity(quantity);
        cartItemDTO.setPrice(product.getUnitPrice());

        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(product.getUnitPrice());
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByProductIdAndUserId(productId, userId)).thenReturn(Optional.empty());
        when(cartItemMapper.toDto(any(CartItem.class))).thenReturn(cartItemDTO);
        when(cartItemMapper.toEntity(cartItemDTO)).thenReturn(cartItem);
        when(cartRepository.save(cartItem)).thenReturn(cartItem);

        CartItemDTO result = cartService.addProducts(cartRequestDTO);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(productId, result.getProductId());
        assertEquals(quantity, result.getQuantity());
        assertEquals(product.getUnitPrice(), result.getPrice());
        verify(cartRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testAddProducts_ProductNotFound() {
        Long userId = 1L;
        Long productId = 2L;

        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setUserId(userId);
        cartRequestDTO.setProductId(productId);
        cartRequestDTO.setQuantity(5);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        CartException exception = assertThrows(CartException.class, () -> cartService.addProducts(cartRequestDTO));

        assertEquals(ErrorCodes.INVALID_INPUT.getCode(), exception.getCode());
        assertEquals(ErrorCodes.INVALID_INPUT.getMessage(), exception.getMessage());
        verify(cartRepository, never()).save(any(CartItem.class));
    }

    @Test
    void testRefreshCart_Success() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setName("User Name");

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(5);
        cartItem.setPrice(10.0);

        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setQuantity(5);
        cartItemDTO.setPrice(10.0);

        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setUser(userDTO);
        cartResponseDTO.setProductList(List.of(cartItemDTO));
        cartResponseDTO.setCartPrice(50.0);

        when(validatorUtil.validateUser(userId)).thenReturn(true);
        when(userService.getUser(userId)).thenReturn(userDTO);
        when(cartRepository.findAllByUserId(userId)).thenReturn(List.of(cartItem));
        when(cartItemMapper.toDto(cartItem)).thenReturn(cartItemDTO);

        CartResponseDTO result = cartService.refreshCart(userId);

        assertNotNull(result);
        assertEquals(userDTO, result.getUser());
        assertEquals(50.0, result.getCartPrice());
        assertEquals(1, result.getProductList().size());
        verify(cartRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void testRefreshCart_UserNotFound() {
        Long userId = 1L;
        when(validatorUtil.validateUser(userId)).thenThrow(new CartException(ErrorCodes.INVALID_INPUT.getCode(), ErrorCodes.INVALID_INPUT.getMessage()));

        CartException exception = assertThrows(CartException.class, () -> cartService.refreshCart(userId));

        assertEquals(ErrorCodes.INVALID_INPUT.getCode(), exception.getCode());
        assertEquals(ErrorCodes.INVALID_INPUT.getMessage(), exception.getMessage());
        verify(cartRepository, never()).findAllByUserId(userId);
    }

    @Test
    void testClearCart_Success() {
        Long userId = 1L;
        CartItem cartItem = new CartItem();
        when(validatorUtil.validateUser(userId)).thenReturn(true);
        when(cartRepository.findAllByUserId(userId)).thenReturn(List.of(cartItem));
        doNothing().when(cartRepository).deleteAll(List.of(cartItem));

        cartService.clearCart(userId);

        verify(cartRepository, times(1)).deleteAll(List.of(cartItem));
    }

    @Test
    void testClearCart_Empty() {
        Long userId = 1L;
        when(validatorUtil.validateUser(userId)).thenReturn(true);
        when(cartRepository.findAllByUserId(userId)).thenReturn(List.of());

        CartException exception = assertThrows(CartException.class, () -> cartService.clearCart(userId));

        assertEquals(ErrorCodes.EMPTY_CART.getCode(), exception.getCode());
        assertTrue(exception.getMessage().contains(userId.toString()));
        verify(cartRepository, never()).deleteAll(anyList());
    }

}
