package com.example.ecommerce.controllers;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void createProduct_ReturnsProduct() throws Exception {
        ProductDTO dto = new ProductDTO(1L, "Laptop", "Gaming laptop", BigDecimal.valueOf(40000));

        when(productService.createProduct(any(ProductDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Laptop")))
                .andExpect(jsonPath("$.description", is("Gaming laptop")))
                .andExpect(jsonPath("$.price", is(40000)));
    }

    @Test
    void getAllProducts_ReturnsList() throws Exception {
        ProductDTO p1 = new ProductDTO(1L, "laptop", "Gaming laptop", BigDecimal.valueOf(40000));
        ProductDTO p2 = new ProductDTO(2L, "iphone", "Grey 15 pro max", BigDecimal.valueOf(20000));

        when(productService.getAllProducts()).thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("laptop")))
                .andExpect(jsonPath("$[1].name", is("iphone")));
    }

    @Test
    void getProductById_Found() throws Exception {
        ProductDTO dto = new ProductDTO(1L, "Laptop", "Gaming laptop", BigDecimal.valueOf(40000));

        when(productService.getProductById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Laptop")))
                .andExpect(jsonPath("$.price", is(40000)));
    }

    @Test
    void updateProduct_Found() throws Exception {
        ProductDTO dto = new ProductDTO(1L, "samsung tv", "4k quality", BigDecimal.valueOf(45000));

        when(productService.updateProduct(eq(1L), any(ProductDTO.class)))
                .thenReturn(Optional.of(dto));

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("samsung tv")))
                .andExpect(jsonPath("$.price", is(45000)));
    }

    @Test
    void deleteProduct_Found() throws Exception {
        when(productService.deleteProduct(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

}
