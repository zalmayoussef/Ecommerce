package com.example.ecommerce.controllers;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    EasyRandom easyRandom = new EasyRandom();

    @Test
    void createOrder_ReturnsOrder() throws Exception {
        OrderDTO dto = easyRandom.nextObject(OrderDTO.class);

        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId", is(dto.getCustomerId())))
                .andExpect(jsonPath("$.items", hasSize(dto.getItems().size())))
                .andExpect(jsonPath("$.items[0].productId", is(dto.getItems().get(0).getProductId())));
    }

    @Test
    void getAllOrders_ReturnsList() throws Exception {
        OrderDTO dto = easyRandom.nextObject(OrderDTO.class);

        when(orderService.getAllOrders()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerId", is(dto.getCustomerId())));
    }

    @Test
    void getOrder_Found() throws Exception {
        OrderDTO dto = easyRandom.nextObject(OrderDTO.class);

        when(orderService.getOrderById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId", is(dto.getCustomerId())));
    }

    @Test
    void deleteOrder_Found() throws Exception {
        when(orderService.deleteOrder(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());
    }
}
