package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.models.Customer;
import com.example.ecommerce.models.Order;
import com.example.ecommerce.models.OrderItem;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.repositories.OrderRepository;
import com.example.ecommerce.repositories.ProductRepository;
import com.example.ecommerce.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Spy
    @InjectMocks
    private OrderService orderService;

    private Customer customer;
    private Product laptop;
    private Product phone;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Salma");

        laptop = new Product();
        laptop.setId(10L);
        laptop.setName("Laptop");
        laptop.setPrice(BigDecimal.valueOf(5000));

        phone = new Product();
        phone.setId(20L);
        phone.setName("Phone");
        phone.setPrice(BigDecimal.valueOf(2000));
    }

    @Test
    void createOrder_WithMultipleItems_ReturnsCorrectData() {
        OrderItemDTO item1 = new OrderItemDTO(10L, 2, BigDecimal.valueOf(10000));
        OrderItemDTO item2 = new OrderItemDTO(20L, 1, BigDecimal.valueOf(2000));

        OrderDTO inputDto = new OrderDTO(1L, LocalDateTime.now(), List.of(item1, item2));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(10L)).thenReturn(Optional.of(laptop));
        when(productRepository.findById(20L)).thenReturn(Optional.of(phone));

        Order savedOrder = new Order();
        savedOrder.setId(99L);
        savedOrder.setCustomer(customer);
        savedOrder.setOrderDate(java.util.Date.from(inputDto.getOrderDate()
                .atZone(java.time.ZoneId.systemDefault()).toInstant()));

        OrderItem orderItem1 = new OrderItem(10L, savedOrder, laptop, 2, BigDecimal.valueOf(10000));
        OrderItem orderItem2 = new OrderItem(20L, savedOrder, phone, 1, BigDecimal.valueOf(2000));
        savedOrder.setItems(List.of(orderItem1, orderItem2));

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        OrderDTO result = orderService.createOrder(inputDto);

        assertNotNull(result);
        assertEquals(2, result.getItems().size());
        assertEquals(BigDecimal.valueOf(10000), result.getItems().get(0).getPrice());
        assertEquals(BigDecimal.valueOf(2000), result.getItems().get(1).getPrice());
    }

    @Test
    void getAllOrders_ReturnsList() {
        Order order = new Order();
        order.setId(1L);
        order.setCustomer(customer);
        order.setOrderDate(java.util.Date.from(LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        order.setItems(Collections.singletonList(new OrderItem(null, order, laptop, 1, BigDecimal.valueOf(5000))));

        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderDTO> result = orderService.getAllOrders();

        assertEquals(1, result.size());
        assertEquals("Laptop", laptop.getName());
    }

    @Test
    void getOrderById_Found_ReturnsOrderDTO() {
        Order order = new Order();
        order.setId(1L);
        order.setCustomer(customer);
        order.setOrderDate(java.util.Date.from(LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        order.setItems(Collections.singletonList(new OrderItem(null, order, laptop, 1, BigDecimal.valueOf(5000))));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<OrderDTO> result = orderService.getOrderById(1L);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getItems().size());
    }

    @Test
    void deleteOrder_Exists_ReturnsTrue() {
        when(orderRepository.existsById(1L)).thenReturn(true);

        boolean result = orderService.deleteOrder(1L);

        assertTrue(result);
        verify(orderRepository).deleteById(1L);
    }
}
