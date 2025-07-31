package com.example.ecommerce.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String customer;
    private LocalDateTime orderDate;
    private List<OrderItemDTO> items;
}
