package com.example.ecommerce.models;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

import lombok.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customer;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;
}
