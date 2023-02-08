package com.hillel.homework16.controllers;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Integer id) {
        super("Could not find order " + id);
    }
}
