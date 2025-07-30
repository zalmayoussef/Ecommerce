package com.example.ecommerce.dto;

import com.example.ecommerce.models.Product;

import java.util.Date;
import java.util.List;

import lombok.*;

@Getter
@Setter
public class OrderDTO {

    private Long id;
    private String customer;
    private Date orderDate;
    //dont return entity, return dto
    private List<Product> products;

}
