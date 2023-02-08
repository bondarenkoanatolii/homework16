package com.hillel.homework16.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.hillel.homework16.models.Order;
import com.hillel.homework16.models.Product;
import com.hillel.homework16.repositories.OrderRepository;
import com.hillel.homework16.repositories.ProductRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderController {
    private final OrderRepository repository;
    private final OrderModelAssembler assembler;
    private final ProductRepository productRepository;

    public OrderController(OrderRepository repository, OrderModelAssembler assembler, ProductRepository productRepository) {
        this.repository = repository;
        this.assembler = assembler;
        this.productRepository = productRepository;
    }

    @GetMapping("/orders")
    CollectionModel<EntityModel<Order>> all() {
        List<EntityModel<Order>> orders = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }

    @GetMapping("/orders/{id}")
    EntityModel<Order> one(@PathVariable Integer id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return assembler.toModel(order);
    }

    @PostMapping("/orders")
    Order newOrder(@RequestBody RequestNewOrder requestNewOrder) {
        for (Integer id : requestNewOrder.listId) {
            productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException(id));
        }

        List<Product> listProducts = productRepository.findAllById(requestNewOrder.listId);
        return repository.save(new Order(requestNewOrder.date, listProducts));
    }

    // для зручного додавання замовлення
    private static class RequestNewOrder {
        private String date;
        private List<Integer> listId;

        public RequestNewOrder(String date, List<Integer> listId) {
            this.date = date;
            this.listId = listId;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<Integer> getListId() {
            return listId;
        }

        public void setListId(List<Integer> listId) {
            this.listId = listId;
        }
    }
}
