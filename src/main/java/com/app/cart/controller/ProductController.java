package com.app.cart.controller;

import com.app.cart.entity.Product;
import com.app.cart.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<Product> getAll() {
        try {
            return productService.findAll();
        } catch (Exception e) {
            log.error("Error fetching products", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch products");
        }
    }

    @RequestMapping(method = RequestMethod.GET,value ="/initial-save", produces = "application/json")
    public void saveInitialBatch(){
        productService.saveInitialBatch();
    }

}
