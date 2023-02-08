package com.hillel.homework16.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


@Entity(name = "Orders") // order - зарезервоване слово. Тому name = "Orders".
public class Order {

    @Id
    @GeneratedValue
    private Integer id;

    private LocalDate date;
    private double cost;

    @ManyToMany
    private List<Product> products;

    // Для зручного вигляду дати при додаванні та перегляду замовлення
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Order() {
    }

    public Order(String date, List<Product> products) {
        this.date = LocalDate.parse(date, formatter);
        this.cost = products.stream().mapToDouble(Product::getCost).sum();
        this.products = products;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getCost() {
        // Щоб правильно рахувалося, якщо змінилася ціна продукту.
        // Або залишити старий варіант - return this.cost;
        return this.cost = products.stream().mapToDouble(Product::getCost).sum();
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
        return Double.compare(order.cost, cost) == 0 && Objects.equals(id, order.id) && Objects.equals(date, order.date)
                && Objects.equals(products, order.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, cost, products);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", date=" +  date.format(formatter) +
                ", cost=" + cost +
                ", products=" + products.toString() +
                '}';
    }
}
