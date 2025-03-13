package com.inghub.brokage.firm.stock.service;

import com.inghub.brokage.firm.stock.repository.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer registerNewCustomer(Customer customer);
    List<Customer> getAllCustomers();
    Optional<Customer> getCustomerById(Integer id);
    Optional<Customer> getCustomerByUsername(String username);
}
