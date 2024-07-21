package com.app.cart.dto;


import lombok.Data;

@Data
public class CartRequestDTO {
    private Long userId;
    private Long productId;
    private Integer quantity;
}
