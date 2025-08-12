package com.example.ecommerce.service;

import com.example.ecommerce.dto.CustomerDTO;
import com.example.ecommerce.models.Customer;
import com.example.ecommerce.repositories.CustomerRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Spy
    @InjectMocks
    private CustomerService customerService;

    EasyRandom easyRandom = new EasyRandom();

    @Test
    void createCustomer_SavesAndReturnsDTO() {
        CustomerDTO dto = easyRandom.nextObject(CustomerDTO.class);
        Customer savedCustomer = easyRandom.nextObject(Customer.class);

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerDTO result = customerService.createCustomer(dto);

        assertNotNull(result);
        assertThat(result.getEmail()).isNotEmpty();
        assertThat(result.getName()).isNotEmpty();
        assertThat(result.getAddress()).isNotEmpty();
        verify(customerRepository).save(any(Customer.class));
    }


    @Test
    void updateCustomerReturnsDataCorrectly() {
        CustomerRepository mockCustomerRepository = Mockito.mock(CustomerRepository.class);
        CustomerService customerService = new CustomerService(mockCustomerRepository);

        CustomerDTO dto = easyRandom.nextObject(CustomerDTO.class);
        Customer existingCustomer = easyRandom.nextObject(Customer.class);
        Customer updatedCustomer = easyRandom.nextObject(Customer.class);

        when(mockCustomerRepository.findById(any())).thenReturn(Optional.of(existingCustomer));
        when(mockCustomerRepository.save(any())).thenReturn(updatedCustomer);

        Optional<CustomerDTO> result = customerService.updateCustomer(existingCustomer.getId(), dto);

        assertTrue(result.isPresent());

        CustomerDTO returnedDTO = result.get();

        assertEquals(updatedCustomer.getName(), returnedDTO.getName());
        assertEquals(updatedCustomer.getEmail(), returnedDTO.getEmail());
        assertEquals(updatedCustomer.getAddress(), returnedDTO.getAddress());
    }


    @Test
    void getCustomerById_Found() {
        Customer c1 = easyRandom.nextObject(Customer.class);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(c1));

        Optional<CustomerDTO> result = customerService.getCustomerById(1L);

        assertTrue(result.isPresent());
        assertThat(result.get().getName()).isNotEmpty();
        verify(customerRepository).findById(1L);
    }

    @Test
    void updateCustomer_UpdatesCorrectly() {
        Customer existing = easyRandom.nextObject(Customer.class);
        Customer updated = easyRandom.nextObject(Customer.class);
        CustomerDTO dto = easyRandom.nextObject(CustomerDTO.class);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(any(Customer.class))).thenReturn(updated);

        Optional<CustomerDTO> result = customerService.updateCustomer(1L, dto);

        assertTrue(result.isPresent());
        assertThat(result.get().getName()).isNotEmpty();
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