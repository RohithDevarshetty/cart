package com.app.cart.service;

import com.app.cart.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    void saveInitialBatch();
}
