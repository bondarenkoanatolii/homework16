package com.hillel.homework16;

import com.hillel.homework16.models.Product;
import com.hillel.homework16.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductLoader {

    private static final Logger log = LoggerFactory.getLogger(ProductLoader.class);


    @Bean
    CommandLineRunner initProduct(ProductRepository productRepository) {
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

       };
    }



}
