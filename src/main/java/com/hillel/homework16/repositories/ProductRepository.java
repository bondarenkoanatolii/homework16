package com.hillel.homework16.repositories;

import com.hillel.homework16.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
