package com.app.cart.service.impl;

import com.app.cart.entity.Product;
import com.app.cart.repository.ProductRepository;
import com.app.cart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    private static List<Product> products = new ArrayList<>();

    static {
        products.add(new Product(35.75d,1000,"Pears baby soap for Kids","Soap"));
        products.add(new Product(45.50d,500,"Signal Tooth Brushes Size in (L, M, S)","Tooth Brushe"));
        products.add(new Product(1500.0d,100,"Casual Shirt imported from France","Shirt"));
        products.add(new Product(1000.0d,400,"Leather bag imported from USA ","Office Bag"));
        products.add(new Product(450.0d,800,"Hot Water Bottles","Bottle"));
        products.add(new Product(2500.0d,800,"Imported wrist watches from swiss","Wrist Watch"));
        products.add(new Product(45000.0d,800,"3G/4G capability","Mobile Phone"));
        products.add(new Product(300.0d,800,"Head and Shoulders Shampoo","Shampoo"));
        products.add(new Product(550.0d,800,"Imported Leather Wallets from AUS","Leather Wallets"));
        products.add(new Product(85000.0d,800,"Imported Canon camera from USA","Camera"));
    }

    public void saveInitialBatch(){
        productRepository.saveAll(products);
    }

    public List<Product> findAll(){
        return productRepository.findAll();
    }
}
