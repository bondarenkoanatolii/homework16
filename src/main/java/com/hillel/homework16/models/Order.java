package com.hillel.homework16.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity(name = "Orders")
public class Order {
    private @Id @GeneratedValue Integer id;
    private LocalDateTime date;
    private double cost;
    @ManyToMany
    private List<Product> products;

    public Order() {
    }

    public Order(LocalDateTime date, List<Product> products) {
        this.date = date;
        this.cost = products.stream().mapToDouble(Product::getCost).sum();
        this.products = products;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = products.stream().mapToDouble(Product::getCost).sum();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Double.compare(order.cost, cost) == 0 && Objects.equals(id, order.id) && Objects.equals(date, order.date) && Objects.equals(products, order.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, cost, products);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", date=" + date +
                ", cost=" + cost +
                ", products=" + products.toString() +
                '}';
    }
}
