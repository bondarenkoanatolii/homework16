package com.hillel.homework16.repositories;

import com.hillel.homework16.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
