package com.hillel.homework16;

//import com.hillel.homework16.models.Order;
import com.hillel.homework16.models.Order;
import com.hillel.homework16.models.Product;
//import com.hillel.homework16.repositories.OrderRepository;
import com.hillel.homework16.repositories.OrderRepository;
import com.hillel.homework16.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class ProductAndOrderLoader {

    private static final Logger log = LoggerFactory.getLogger(ProductAndOrderLoader.class);


    @Bean
    CommandLineRunner initProductAndOrder(OrderRepository orderRepository, ProductRepository productRepository) {
       return args -> {
           log.info("Preloading " + productRepository.save(new Product("broad", 1.4)));
           log.info("Preloading " + productRepository.save(new Product("butter", 2.2)));
           log.info("Preloading " + productRepository.save(new Product("salt", 0.7)));
           log.info("Preloading " + productRepository.save(new Product("sugar", 12.5)));
           log.info("Preloading " + productRepository.save(new Product("potatoes", 1.3)));
           log.info("Preloading " + productRepository.save(new Product("eggs", 4.0)));
           log.info("Preloading " + productRepository.save(new Product("milk", 3.2)));
           log.info("Preloading " + productRepository.save(new Product("cucumber", 1.2)));
           log.info("Preloading " + productRepository.save(new Product("cabbage", 1.4)));
           List<Product> list = productRepository.findAllById(List.of(1,2,3));
           log.info("Preloading " + orderRepository.save(new Order(LocalDateTime.now(), list)));
           list = productRepository.findAllById(List.of(3, 4, 5));
           log.info("Preloading " + orderRepository.save(new Order(LocalDateTime.now(), list)));
       };
    }



}
