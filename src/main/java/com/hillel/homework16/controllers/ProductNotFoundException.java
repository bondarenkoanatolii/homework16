package com.hillel.homework16.controllers;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Integer id) {
        super("Could not find product " + id);
    }
}
