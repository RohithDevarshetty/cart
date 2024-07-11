package com.app.cart.entity;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Cart> carts = new HashSet<>();
}