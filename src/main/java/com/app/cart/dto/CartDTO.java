package com.app.cart.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class CartDTO implements Serializable {

    private Long userId;
    private Long productId;
    private Integer stock;
    private String status;
}