package com.example.ecommerce.controllers;

import com.example.ecommerce.dto.CustomerDTO;
import com.example.ecommerce.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc; // simulate http reqs w/out real server

    @Autowired
    private ObjectMapper objectMapper; // java dtos objects -> json

    @MockBean
    private CustomerService customerService;

    EasyRandom easyRandom  = new EasyRandom();

    @Test
    void createCustomer_ReturnsCustomer() throws Exception {
        CustomerDTO dto = easyRandom.nextObject(CustomerDTO.class);

        when(customerService.createCustomer(any(CustomerDTO.class)))
                .thenReturn(dto);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON) // body is json
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.email", is(dto.getEmail())))
                .andExpect(jsonPath("$.address", is(dto.getAddress())));
    }

    @Test
    void getAllCustomers_ReturnsList() throws Exception {
        CustomerDTO dto1 = easyRandom.nextObject(CustomerDTO.class);
        CustomerDTO dto2 = easyRandom.nextObject(CustomerDTO.class);

        when(customerService.getAllCustomers())
                .thenReturn(Arrays.asList(dto1, dto2));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(dto1.getName())))
                .andExpect(jsonPath("$[1].name", is(dto2.getName())));
    }

    @Test
    void getCustomerById_Found() throws Exception {
        CustomerDTO dto = easyRandom.nextObject(CustomerDTO.class);

        when(customerService.getCustomerById(1L))
                .thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(dto.getName())));
    }

    @Test
    void updateCustomer_Found() throws Exception {
        CustomerDTO dto = easyRandom.nextObject(CustomerDTO.class);
        when(customerService.updateCustomer(eq(1L), any(CustomerDTO.class)))
                .thenReturn(Optional.of(dto));

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.email", is(dto.getEmail())));
    }

    @Test
    void deleteCustomer_Found() throws Exception {
        when(customerService.deleteCustomer(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());
    }

}
