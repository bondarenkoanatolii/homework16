package com.hillel.homework16.controllers;

import com.hillel.homework16.models.Product;
import com.hillel.homework16.repositories.ProductRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {
    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/products")
    List<Product> all() {
        return repository.findAll();
    }

}
