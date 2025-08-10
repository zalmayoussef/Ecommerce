package com.example.ecommerce.controllers;

import com.example.ecommerce.dto.CustomerDTO;
import com.example.ecommerce.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    void createCustomer_ReturnsCustomer() throws Exception {
        CustomerDTO dto = new CustomerDTO();
        dto.setName("test");
        dto.setEmail("test@example.com");
        dto.setAddress("street 12");

        when(customerService.createCustomer(any(CustomerDTO.class)))
                .thenReturn(dto);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("test")))
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.address", is("street 12")));
    }

    @Test
    void getAllCustomers_ReturnsList() throws Exception {
        CustomerDTO dto1 = new CustomerDTO();
        dto1.setName("test1");
        dto1.setEmail("test1@example.com");
        dto1.setAddress("street 12");

        CustomerDTO dto2 = new CustomerDTO();
        dto2.setName("test2");
        dto2.setEmail("test2@example.com");
        dto2.setAddress("street 14");

        when(customerService.getAllCustomers())
                .thenReturn(Arrays.asList(dto1, dto2));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("test1")))
                .andExpect(jsonPath("$[1].name", is("test2")));
    }

    @Test
    void getCustomerById_Found() throws Exception {
        CustomerDTO dto = new CustomerDTO();
        dto.setName("test");
        dto.setEmail("test@example.com");
        dto.setAddress("streey 1");

        when(customerService.getCustomerById(1L))
                .thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("test")));
    }

    @Test
    void updateCustomer_Found() throws Exception {
        CustomerDTO dto = new CustomerDTO();
        dto.setName("john");
        dto.setEmail("john@example.com");
        dto.setAddress("4 fareed street");

        when(customerService.updateCustomer(eq(1L), any(CustomerDTO.class)))
                .thenReturn(Optional.of(dto));

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("john")))
                .andExpect(jsonPath("$.email", is("john@example.com")));
    }

    @Test
    void deleteCustomer_Found() throws Exception {
        when(customerService.deleteCustomer(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());
    }

}
