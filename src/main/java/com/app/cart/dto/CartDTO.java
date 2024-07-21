package com.app.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class CartDTO implements Serializable {

    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private Double price;
    private Date created;
    private Date updated;
}