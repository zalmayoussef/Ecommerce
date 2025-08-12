package com.example.ecommerce.controllers;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
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

    EasyRandom easyRandom = new EasyRandom();

    @Test
    void createProduct_ReturnsProduct() throws Exception {
        ProductDTO dto = easyRandom.nextObject(ProductDTO.class);
        when(productService.createProduct(any(ProductDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId())))
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.price", is(dto.getPrice())));
    }

    @Test
    void getAllProducts_ReturnsList() throws Exception {
        ProductDTO p1 = easyRandom.nextObject(ProductDTO.class);
        ProductDTO p2 = easyRandom.nextObject(ProductDTO.class);
        when(productService.getAllProducts()).thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(p1.getName())))
                .andExpect(jsonPath("$[1].name", is(p2.getName())));
    }

    @Test
    void getProductById_Found() throws Exception {
        ProductDTO dto = easyRandom.nextObject(ProductDTO.class);
        when(productService.getProductById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.price", is(dto.getPrice())));
    }

    @Test
    void updateProduct_Found() throws Exception {
        ProductDTO dto = easyRandom.nextObject(ProductDTO.class);
        when(productService.updateProduct(eq(1L), any(ProductDTO.class)))
                .thenReturn(Optional.of(dto));

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.price", is(dto.getPrice())));
    }

    @Test
    void deleteProduct_Found() throws Exception {
        when(productService.deleteProduct(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

}
