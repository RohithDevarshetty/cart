package com.app.cart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
@NoArgsConstructor
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "name")
    @Getter
    @Setter
    private String name;

    @Column(name = "description")
    @Getter
    @Setter
    private String description;

    @Column(name = "stock_quantity")
    @Getter
    @Setter
    private Integer stockQuantity;

    @Column(name = "unit_price")
    @Getter
    @Setter
    private Double unitPrice;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private Set<Cart> shoppingCarts = new HashSet<>();

    public Product(String name, Double unitPrice, Integer stock_quantity, String description) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.stockQuantity = stock_quantity;
        this.description = description;
    }

}