package com.example.ecommerce.service;

import com.example.ecommerce.dto.CustomerDTO;
import com.example.ecommerce.models.Customer;
import com.example.ecommerce.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void createCustomer_SavesAndReturnsDTO() {
        CustomerDTO dto = new CustomerDTO();
        dto.setName("John");
        dto.setEmail("john@gmail.com");
        dto.setAddress("street 19 maadi");

        Customer savedCustomer = new Customer();
        savedCustomer.setId(1L);
        savedCustomer.setName("John");
        savedCustomer.setEmail("john@gmail.com");
        savedCustomer.setAddress("street 19 maadi");

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerDTO result = customerService.createCustomer(dto);

        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("john@gmail.com", result.getEmail());
        assertEquals("street 19 maadi", result.getAddress());
        verify(customerRepository).save(any(Customer.class));
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

        when(mockCustomerRepository.findById(any())).thenReturn(Optional.of(existingCustomer));
        when(mockCustomerRepository.save(any())).thenReturn(updatedCustomer);

        // prepare test results
        Optional<CustomerDTO> result = customerService.updateCustomer(existingCustomer.getId(), dto);

        assertTrue(result.isPresent());
        assertEquals("Salma", result.get().getName());
        assertEquals("salma@gmail.com", result.get().getEmail());
        assertEquals("15 Fareed Street", result.get().getAddress());

    }

    @Test
    void getCustomerById_Found() {
        Customer c1 = new Customer();
        c1.setId(1L);
        c1.setName("John");
        c1.setEmail("john@gmail.com");
        c1.setAddress("street 19 maadi");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(c1));

        Optional<CustomerDTO> result = customerService.getCustomerById(1L);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getName());
        verify(customerRepository).findById(1L);
    }

    @Test
    void updateCustomer_UpdatesCorrectly() {
        Customer existing = new Customer();
        existing.setId(1L);
        existing.setName("Samir");
        existing.setEmail("samir@email.com");
        existing.setAddress("4 thawra street");

        Customer updated = new Customer();
        updated.setId(1L);
        updated.setName("Madonna");
        updated.setEmail("madonna@email.com");
        updated.setAddress("4 ard el golf");

        CustomerDTO dto = new CustomerDTO();
        dto.setName("Madonna");
        dto.setEmail("madonna@email.com");
        dto.setAddress("4 ard el golf");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(any(Customer.class))).thenReturn(updated);

        Optional<CustomerDTO> result = customerService.updateCustomer(1L, dto);

        assertTrue(result.isPresent());
        assertEquals("Madonna", result.get().getName());
        verify(customerRepository).findById(1L);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void deleteCustomer_Exists_ReturnsTrue() {
        when(customerRepository.existsById(1L)).thenReturn(true);

        boolean deleted = customerService.deleteCustomer(1L);

        assertTrue(deleted);
        verify(customerRepository).existsById(1L);
        verify(customerRepository).deleteById(1L);
    }
}