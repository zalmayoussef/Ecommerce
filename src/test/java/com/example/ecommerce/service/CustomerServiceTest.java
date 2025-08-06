package com.example.ecommerce.service;

import com.example.ecommerce.dto.CustomerDTO;
import com.example.ecommerce.models.Customer;
import com.example.ecommerce.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    @Test
    void createCustomerReturnsDataCorrectly(){
        CustomerRepository mockCustomerRepository = Mockito.mock(CustomerRepository.class);
        CustomerService customerService = new CustomerService(mockCustomerRepository);
        CustomerDTO dto = new CustomerDTO();
        dto.setName("John");
        dto.setEmail("john@gmail.com");
        dto.setAddress("street 19 maadi");

        Customer testCustomer = new Customer();
        testCustomer.setName("John");
        testCustomer.setId(1L);
        testCustomer.setEmail("john@gmail.com");
        testCustomer.setAddress("street 19 maadi");

        Mockito.when(mockCustomerRepository.save(Mockito.any())).thenReturn(testCustomer);
        CustomerDTO customerDTO = customerService.createCustomer(dto);

        assertEquals("John",customerDTO.getName());
        assertEquals("john@gmail.com", customerDTO.getEmail());
        assertEquals("street 19 maadi", customerDTO.getAddress());

    }
}