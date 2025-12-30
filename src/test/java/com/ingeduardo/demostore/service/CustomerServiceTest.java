package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.model.Customer;
import com.ingeduardo.demostore.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId("1");
        customer.setName("John Doe");
        customer.setEmail("john@example.com");
        customer.setPhone("123456789");
        customer.setAddress("123 Main St");
    }

    @Test
    void findAll_Success() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));
        List<Customer> result = customerService.findAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(customerRepository).findAll();
    }

    @Test
    void search_WithQuery_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> page = new PageImpl<>(Arrays.asList(customer));
        when(customerRepository.searchByQuery("John", pageable)).thenReturn(page);

        Page<Customer> result = customerService.search("John", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(customerRepository).searchByQuery("John", pageable);
    }

    @Test
    void search_WithNullQuery_ReturnsAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> page = new PageImpl<>(Arrays.asList(customer));
        when(customerRepository.findAll(pageable)).thenReturn(page);

        Page<Customer> result = customerService.search(null, pageable);

        assertNotNull(result);
        verify(customerRepository).findAll(pageable);
    }

    @Test
    void findById_Success() {
        when(customerRepository.findById("1")).thenReturn(Optional.of(customer));
        Customer result = customerService.findById("1");
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void findById_NotFound_ThrowsException() {
        when(customerRepository.findById("2")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> customerService.findById("2"));
    }

    @Test
    void create_Success() {
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.create(customer);

        assertNotNull(result);
        verify(customerRepository).save(customer);
    }

    @Test
    void create_DuplicateEmail_ThrowsException() {
        when(customerRepository.existsByEmail(anyString())).thenReturn(true);
        assertThrows(RuntimeException.class, () -> customerService.create(customer));
    }

    @Test
    void update_Success() {
        when(customerRepository.findById("1")).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer updatedDetails = new Customer();
        updatedDetails.setName("John Updated");

        Customer result = customerService.update("1", updatedDetails);

        assertNotNull(result);
        assertEquals("John Updated", result.getName());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void delete_Success() {
        when(customerRepository.existsById("1")).thenReturn(true);
        doNothing().when(customerRepository).deleteById("1");

        customerService.delete("1");

        verify(customerRepository).deleteById("1");
    }

    @Test
    void delete_NotFound_ThrowsException() {
        when(customerRepository.existsById("2")).thenReturn(false);
        assertThrows(RuntimeException.class, () -> customerService.delete("2"));
    }
}
