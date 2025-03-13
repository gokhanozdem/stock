package com.inghub.brokage.firm.stock.controller;

import com.inghub.brokage.firm.stock.repository.entity.Customer;
import com.inghub.brokage.firm.stock.service.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerServiceImpl customerServiceImpl;

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerServiceImpl.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (!isAdmin) {
            Customer currentCustomer = customerServiceImpl.getCustomerByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("Current user not found"));
            if (!currentCustomer.getId().equals(id)) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "You are not authorized to access this customer's data");
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }
        }

        Customer customer = customerServiceImpl.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
}