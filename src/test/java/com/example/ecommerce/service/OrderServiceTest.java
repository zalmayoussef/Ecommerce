package com.example.ecommerce.service;

import com.example.ecommerce.dto.CustomerDTO;
import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.models.Customer;
import com.example.ecommerce.models.Order;
import com.example.ecommerce.models.OrderItem;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.repositories.OrderRepository;
import com.example.ecommerce.repositories.ProductRepository;
import com.example.ecommerce.repositories.CustomerRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Spy
    @InjectMocks
    private OrderService orderService;

    private Customer customer;
    private Product laptop;
    private Product phone;

    EasyRandom easyRandom;

    @BeforeEach
    void setUp() {
        easyRandom = new EasyRandom();
        MockitoAnnotations.openMocks(this);

        customer = easyRandom.nextObject(Customer.class);
        laptop = easyRandom.nextObject(Product.class);
        phone = easyRandom.nextObject(Product.class);
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

        assertEquals(updatedCustomer.getName(), result.get().getName());
        assertEquals(updatedCustomer.getEmail(), result.get().getEmail());
        assertEquals(updatedCustomer.getAddress(), result.get().getAddress());

        assertThat(result.get().getEmail()).isNotEmpty();
        assertThat(result.get().getName()).isNotEmpty();
        assertThat(result.get().getAddress()).isNotEmpty();
    }

    @Test
    void getAllOrders_ReturnsList() {
        Order order = easyRandom.nextObject(Order.class);

        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderDTO> result = orderService.getAllOrders();

        assertEquals(1, result.size());
        assertEquals(order.getCustomer().getId(), result.get(0).getCustomerId());
        assertEquals(order.getItems().size(), result.get(0).getItems().size());
    }

    @Test
    void getOrderById_Found_ReturnsOrderDTO() {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .collectionSizeRange(1, 1);
        easyRandom = new EasyRandom(parameters);

        Order order = easyRandom.nextObject(Order.class);
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDTO result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());

    }

    @Test
    void deleteOrder_Exists_ReturnsTrue() {
        when(orderRepository.existsById(1L)).thenReturn(true);

        boolean result = orderService.deleteOrder(1L);

        assertTrue(result);
        verify(orderRepository).deleteById(1L);
    }
}
