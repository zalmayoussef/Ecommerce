package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Test
    void createProductAndSavedCorrectly() {
        ProductRepository productRepository = Mockito.mock(ProductRepository.class);
        ProductService productService = new ProductService(productRepository);

        // prepare product dto
        ProductDTO dto = new ProductDTO();
        dto.setId(1L);
        dto.setName("laptop");
        dto.setDescription("gaming laptop");
        dto.setPrice(BigDecimal.valueOf(40000));

        // test saved product
        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("laptop");
        savedProduct.setDescription("gaming laptop");
        savedProduct.setPrice(BigDecimal.valueOf(40000));

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductDTO result = productService.createProduct(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());
        assertEquals("Gaming laptop", result.getDescription());
        assertEquals(40000, result.getPrice());
    }

    @Test
    void updateProductAndSavedCorrectly() {
    }
}