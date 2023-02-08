package com.hillel.homework16.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.hillel.homework16.models.Product;
import com.hillel.homework16.repositories.ProductRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductController {
    private final ProductRepository repository;
    private final ProductModelAssembler assembler;

    public ProductController(ProductRepository repository, ProductModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/products")
    CollectionModel<EntityModel<Product>> all() {
        List<EntityModel<Product>> products = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(products,
                linkTo(methodOn(ProductController.class).all()).withSelfRel());
    }

    // Якщо такого продукту немає - помилка "Could not find product"
    @GetMapping("/products/{id}")
    EntityModel<Product> one(@PathVariable Integer id) {
        Product product = repository.findById(id) //
                .orElseThrow(() -> new ProductNotFoundException(id));
        return assembler.toModel(product);
    }

    @PostMapping("/products")
    // API - {"name" : "nameNewProduct", "cost" : doubleCostNewProduct}
    Product newProduct(@RequestBody Product product) {
        return repository.save(product);
    }

    // Треба подумати, а чи правильно змінювати Product, на які вже існують посилання в Order?
    // Якщо товар ще ніхто не замовляв, то все нормально.
    // Якщо ж id відсутній, додається новий продукт з автоматично згенерованим id
    @PutMapping("/products/{id}")
    Product replaceProduct(@RequestBody Product newProduct, @PathVariable Integer id) {
        return repository.findById(id)
                .map(product -> {
                    product.setName(newProduct.getName());
                    product.setCost(newProduct.getCost());
                    return repository.save(product);})
                .orElseGet(() -> repository.save(newProduct));

    }

    @DeleteMapping("/products/{id}")
    // Неможливо видаляти product, на який є посилання в order.
    // Можливо пізніше для себе оброблю цей випадок.
    // Або відсутній id?
    void deleteProduct(@PathVariable Integer id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new ProductNotFoundException(id);
        }
    }
}
