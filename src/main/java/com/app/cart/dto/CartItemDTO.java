package com.app.cart.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class CartItemDTO implements Serializable {

    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private Double price;
    private Date created;
    private Date updated;
}