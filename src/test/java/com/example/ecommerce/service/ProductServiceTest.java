package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Spy
    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_SavesAndReturnsCorrectDTO() {

        ProductDTO dto = new ProductDTO(null, "Laptop", "Gaming laptop", BigDecimal.valueOf(40000));
        Product savedProduct = new Product(1L, "Laptop", "Gaming laptop", BigDecimal.valueOf(40000), Collections.emptyList());

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductDTO result = productService.createProduct(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void getAllProducts_ReturnsList() {

        Product product = new Product(1L, "Laptop", "Gaming", BigDecimal.valueOf(40000), Collections.emptyList());
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductDTO> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
        verify(productRepository).findAll();
    }
    @Test
    void getProductById_Found() {
        Product product = new Product(1L, "Laptop", "Gaming", BigDecimal.valueOf(40000), Collections.emptyList());
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<ProductDTO> result = productService.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals("Laptop", result.get().getName());
        verify(productRepository).findById(1L);
    }
    @Test
    void updateProduct_UpdatesAndReturnsDTO() {
        Product existingProduct = new Product(1L, "Laptop", "Gaming", BigDecimal.valueOf(40000), Collections.emptyList());
        Product updatedProduct = new Product(1L, "Laptop Pro", "Gaming Pro", BigDecimal.valueOf(50000), Collections.emptyList());
        ProductDTO updateDto = new ProductDTO(1L, "Laptop Pro", "Gaming Pro", BigDecimal.valueOf(50000));

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Optional<ProductDTO> result = productService.updateProduct(1L, updateDto);

        assertTrue(result.isPresent());
        assertEquals("Laptop Pro", result.get().getName());
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void deleteProduct_WhenExists_ReturnsTrue() {
        when(productRepository.existsById(1L)).thenReturn(true);

        boolean deleted = productService.deleteProduct(1L);

        assertTrue(deleted);
        verify(productRepository).deleteById(1L);
    }
}