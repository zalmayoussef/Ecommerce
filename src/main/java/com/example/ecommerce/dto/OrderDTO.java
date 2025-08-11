package com.example.ecommerce.dto;

import com.example.ecommerce.models.Customer;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long customerId;
    private LocalDateTime orderDate;
    private List<OrderItemDTO> items;
}
