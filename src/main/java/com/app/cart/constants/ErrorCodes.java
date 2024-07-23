package com.app.cart.constants;

import lombok.Getter;

@Getter
public enum ErrorCodes {
    INVALID_USER("ERR001", "Invalid user ID: "),
    INVALID_INPUT("ERR003", "Invalid input"),
    EMPTY_CART("ERR002", "Cart is empty for user ID: "),
    NO_STOCK("ERR003", "Selected more than stock available");


    private final String code;
    private final String message;

    ErrorCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
