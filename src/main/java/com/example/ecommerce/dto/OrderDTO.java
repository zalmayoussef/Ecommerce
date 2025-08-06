package com.example.ecommerce.dto;

import com.example.ecommerce.models.Customer;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Customer customer;
    private LocalDateTime orderDate;
    private List<OrderItemDTO> items;
}
