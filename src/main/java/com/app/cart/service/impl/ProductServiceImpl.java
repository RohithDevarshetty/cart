package com.app.cart.service.impl;

import com.app.cart.entity.Product;
import com.app.cart.repository.ProductRepository;
import com.app.cart.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void saveInitialBatch() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Dettol Soap", 10.0d, 10000, "Body Soap"));
        products.add(new Product("Lux Soap", 8.0d, 12000, "Body Soap"));
        products.add(new Product("Dove Soap", 12.0d, 9000, "Body Soap"));
        products.add(new Product("Pears Soap", 11.0d, 8000, "Body Soap"));
        products.add(new Product("Lifebuoy Soap", 9.0d, 15000, "Body Soap"));
        products.add(new Product("Colgate Toothpaste", 5.5d, 15000, "Toothpaste"));
        products.add(new Product("Samsung Galaxy S21", 799.99d, 500, "Smartphone"));
        products.add(new Product("Sony Headphones", 199.99d, 2000, "Electronics"));
        products.add(new Product("Nike Running Shoes", 120.0d, 8000, "Footwear"));
        products.add(new Product("Levi's Jeans", 60.0d, 6000, "Clothing"));
        products.add(new Product("Apple MacBook Air", 999.99d, 300, "Laptop"));
        products.add(new Product("Adidas Soccer Ball", 25.0d, 2500, "Sports"));
        products.add(new Product("Panasonic Microwave", 150.0d, 1000, "Home Appliance"));
        products.add(new Product("Nestlé KitKat", 1.5d, 30000, "Snack"));
        products.add(new Product("Canon EOS 5D", 2500.0d, 150, "Camera"));
        productRepository.saveAll(products);
    }
}
