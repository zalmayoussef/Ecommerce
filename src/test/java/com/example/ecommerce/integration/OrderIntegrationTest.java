package com.example.ecommerce.integration;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.models.Customer;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.repositories.CustomerRepository;
import com.example.ecommerce.repositories.OrderRepository;
import com.example.ecommerce.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Transactional // roll back db edits after test
class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Customer customer;
    private Product laptop;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();

        EasyRandom easyRandom = new EasyRandom();

        customer = easyRandom.nextObject(Customer.class);
        customer.setOrders(null);
        customer = customerRepository.save(customer);

        laptop = easyRandom.nextObject(Product.class);
        laptop.setOrderItems(null);
        laptop = productRepository.save(laptop);
    }

    @Test
    void createOrderAndsavesOrderSuccessfully() throws Exception {
        OrderItemDTO item = new OrderItemDTO(laptop.getId(), 2, null);
        OrderDTO orderDTO = new OrderDTO(customer.getId(), LocalDateTime.now(), List.of(item));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON) // dto -> json string
                        .content(objectMapper.writeValueAsString(orderDTO))) // maps dto of customer order data
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(customer.getId().intValue()))
                .andExpect(jsonPath("$.items[0].productId").value(laptop.getId().intValue()))
                .andExpect(jsonPath("$.items[0].quantity").value(2));

        assertThat(orderRepository.count()).isEqualTo(1);
    }
}
