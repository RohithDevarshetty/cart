package com.app.cart.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private Integer stockQuantity;
    private Double unitPrice;

}
