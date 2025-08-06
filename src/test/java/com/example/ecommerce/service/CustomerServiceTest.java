package com.example.ecommerce.service;

import com.example.ecommerce.dto.CustomerDTO;
import com.example.ecommerce.models.Customer;
import com.example.ecommerce.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

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

    @Test
    void updateCustomerReturnsDataCorrectly(){
        CustomerRepository mockCustomerRepository = Mockito.mock(CustomerRepository.class);
        CustomerService customerService = new CustomerService(mockCustomerRepository);

        // new update dto data
        CustomerDTO dto = new CustomerDTO();
        dto.setName("Salma");
        dto.setEmail("salma@gmail.com");
        dto.setAddress("15 fareed street");

        // existing customer from DB
        Customer existingCustomer = new Customer();
        existingCustomer.setId(1L);
        existingCustomer.setName("ola");
        existingCustomer.setEmail("ola@email.com");
        existingCustomer.setAddress("12 Address");

        // updated customer after save
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(1L);
        updatedCustomer.setName("Salma");
        updatedCustomer.setEmail("salma@gmail.com");
        updatedCustomer.setAddress("15 Fareed Street");

        Mockito.when(mockCustomerRepository.findById(Mockito.any())).thenReturn(Optional.of(existingCustomer));
        Mockito.when(mockCustomerRepository.save(Mockito.any())).thenReturn(updatedCustomer);

        // prepare test results
        Optional<CustomerDTO> result = customerService.updateCustomer(existingCustomer.getId(), dto);

        assertTrue(result.isPresent());
        assertEquals("Salma", result.get().getName());
        assertEquals("salma@gmail.com", result.get().getEmail());
        assertEquals("15 Fareed Street", result.get().getAddress());

    }


}