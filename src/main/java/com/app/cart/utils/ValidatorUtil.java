package com.app.cart.utils;

import com.app.cart.constants.ErrorCodes;
import com.app.cart.entity.Cart;
import com.app.cart.exception.CartException;
import com.app.cart.repository.CartRepository;
import com.app.cart.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidatorUtil {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public ValidatorUtil(UserRepository userRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    public boolean validateUser(Long userId) throws CartException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new CartException(ErrorCodes.INVALID_USER.getCode(), ErrorCodes.INVALID_USER.getMessage() + userId);
        }
        return true;
    }

    public void validateCartNotEmpty(Long userId) throws CartException {
        List<Cart> cartList = cartRepository.findAllByUserId(userId);
        if (cartList.isEmpty()) {
            throw new CartException(ErrorCodes.EMPTY_CART.getCode(), ErrorCodes.EMPTY_CART.getMessage() + userId);
        }
    }
}