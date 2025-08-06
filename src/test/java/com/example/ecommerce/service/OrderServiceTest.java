package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.models.Customer;
import com.example.ecommerce.models.Order;
import com.example.ecommerce.models.OrderItem;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.repositories.OrderRepository;
import com.example.ecommerce.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Test
    void createOrderReturnsDataCorrectly(){
        OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
        ProductRepository productRepository = Mockito.mock(ProductRepository.class);
        OrderService orderService = new OrderService(orderRepository,productRepository);

        // prepare customer
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("salma@email.com");
        customer.setName("Salma");

        // prepare product
        Product product = new Product();
        product.setId(10L);
        product.setName("Laptop");
        product.setPrice(BigDecimal.valueOf(5000));

        // DTOs
        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setProductId(10L);
        itemDTO.setQuantity(2);

        OrderDTO inputDto = new OrderDTO();
        inputDto.setCustomer(customer);
        inputDto.setOrderDate(LocalDateTime.now());
        inputDto.setItems(Collections.singletonList(itemDTO));

        // mock of product
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));

        // expected order & order items
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);
        orderItem.setPrice(BigDecimal.valueOf(10000)); // two laptops bought

        Order savedOrder = new Order();
        savedOrder.setId(99L);
        savedOrder.setCustomer(customer);
        savedOrder.setOrderDate(java.util.Date.from(inputDto.getOrderDate().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        savedOrder.setItems(Collections.singletonList(orderItem));

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // prepare result dto
        OrderDTO result = orderService.createOrder(inputDto);

        assertNotNull(result);
        assertEquals(customer.getId(), result.getCustomer().getId());
        assertEquals(1, result.getItems().size());
        assertEquals(10L, result.getItems().get(0).getProductId());
        assertEquals(2, result.getItems().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(10000), result.getItems().get(0).getPrice());
    }
}