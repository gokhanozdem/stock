package com.inghub.brokage.firm.stock.service.impl;

import com.inghub.brokage.firm.stock.repository.CustomerRepository;
import com.inghub.brokage.firm.stock.repository.RoleRepository;
import com.inghub.brokage.firm.stock.repository.entity.Customer;
import com.inghub.brokage.firm.stock.repository.entity.Role;
import com.inghub.brokage.firm.stock.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Customer registerNewCustomer(Customer customer) {
        if (customerRepository.existsByUsername(customer.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        customer.setRoles(Collections.singletonList(customerRole));

        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> getCustomerByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

}