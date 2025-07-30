package com.example.ecommerce.dto;

import com.example.ecommerce.models.Product;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

public class OrderDTO {

    private Long id;

    private String customer;

    private Date orderDate;
    //dont return entity
    private List<dto> products;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
