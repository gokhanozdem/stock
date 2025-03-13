package com.inghub.brokage.firm.stock.controller;

import com.inghub.brokage.firm.stock.controller.dto.Order;
import com.inghub.brokage.firm.stock.repository.entity.Customer;
import com.inghub.brokage.firm.stock.service.impl.CustomerServiceImpl;
import com.inghub.brokage.firm.stock.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerServiceImpl customerServiceImpl;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Order order) {
        checkAuthorized(order.getCustomerId());
        return new ResponseEntity<>(orderService.saveOrder(order), HttpStatus.CREATED);
    }

    @GetMapping("/list/{customerId}/{date}")
    public ResponseEntity<List<Order>> listOrder(@PathVariable Integer customerId, @PathVariable LocalDate date) {
        checkAuthorized(customerId);
        return new ResponseEntity<>(orderService.getAllOrdersByCustomerIdAndDate(customerId, date), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Order> delete(@PathVariable Integer orderId) {
        Order order = orderService.getOrderByOrderId(orderId);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        checkAuthorized(order.getCustomerId());
        return new ResponseEntity<>(orderService.deleteOrder(orderId), HttpStatus.OK);
    }

    @PutMapping("/complete/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> complete(@PathVariable Integer orderId) {
        return new ResponseEntity<>(orderService.completeOrder(orderId), HttpStatus.OK);
    }

    private void checkAuthorized(Integer customerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (!isAdmin) {
            Customer currentCustomer = customerServiceImpl.getCustomerByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("Current user not found"));
            if (!currentCustomer.getId().equals(customerId)) {
                throw new AccessDeniedException("You are not authorized to access this customer's data");
            }
        }

        if (customerServiceImpl.getCustomerById(customerId).isEmpty()) {
            throw new RuntimeException("Customer not found");
        }
    }
}
