package com.app.cart.utils;

import com.app.cart.constants.ErrorCodes;
import com.app.cart.entity.CartItem;
import com.app.cart.entity.User;
import com.app.cart.exception.CartException;
import com.app.cart.repository.CartRepository;
import com.app.cart.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValidatorUtilTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private ValidatorUtil validatorUtil;

    @BeforeEach
    public void setUp() {
        validatorUtil = new ValidatorUtil(userRepository, cartRepository);
    }

    @Test
    public void testValidateUser_InvalidUser_ThrowsCartException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        CartException exception = assertThrows(CartException.class, () -> {
            validatorUtil.validateUser(1L);
        });

        assert exception.getCode().equals(ErrorCodes.INVALID_USER.getCode());
        assert exception.getMessage().equals(ErrorCodes.INVALID_USER.getMessage() + "1");
    }

    @Test
    public void testValidateUser_ValidUser_DoesNotThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        validatorUtil.validateUser(1L);
    }

    @Test
    public void testValidateCartNotEmpty_EmptyCart_ThrowsCartException() {
        when(cartRepository.findAllByUserId(anyLong())).thenReturn(Collections.emptyList());

        CartException exception = assertThrows(CartException.class, () -> {
            validatorUtil.validateCartNotEmpty(1L);
        });

        assert exception.getCode().equals(ErrorCodes.EMPTY_CART.getCode());
        assert exception.getMessage().equals(ErrorCodes.EMPTY_CART.getMessage() + "1");
    }

    @Test
    public void testValidateCartNotEmpty_NonEmptyCart_DoesNotThrowException() {
        when(cartRepository.findAllByUserId(anyLong())).thenReturn(List.of(new CartItem()));

        validatorUtil.validateCartNotEmpty(1L);
    }
}
