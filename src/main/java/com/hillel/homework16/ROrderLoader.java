package com.hillel.homework16;

import com.hillel.homework16.models.Order;
import com.hillel.homework16.models.Product;
import com.hillel.homework16.repositories.OrderRepository;
import com.hillel.homework16.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ROrderLoader {

    private static final Logger log = LoggerFactory.getLogger(ROrderLoader.class);

    @Bean
    CommandLineRunner initOrder(OrderRepository orderRepository, ProductRepository productRepository) {
        return args -> {
            List<Product> list = productRepository.findAllById(List.of(1,2,3));
            log.info("Preloading " + orderRepository.save(new Order("12-11-2012", list)));
            list = productRepository.findAllById(List.of(3, 4, 5));
            log.info("Preloading " + orderRepository.save(new Order("31-05-2014", list)));
        };
    }
}