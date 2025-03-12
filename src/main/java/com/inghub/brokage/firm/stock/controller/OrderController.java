package com.inghub.brokage.firm.stock.controller;

import com.inghub.brokage.firm.stock.controller.dto.Order;
import com.inghub.brokage.firm.stock.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Order> create(@RequestBody Order order) {
        return new ResponseEntity<>(orderService.saveOrder(order), HttpStatus.CREATED);
    }

    @GetMapping("/list/{customerId}/{date}")
    public ResponseEntity<List<Order>> listOrder(@PathVariable Integer customerId, @PathVariable LocalDate date) {
        return new ResponseEntity<>(orderService.getAllOrdersByCustomerIdAndDate(customerId, date), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Order> delete(@PathVariable Integer orderId) {
        return new ResponseEntity<>(orderService.deleteOrder(orderId), HttpStatus.OK);
    }

    @PutMapping("/complete/{orderId}")
    public ResponseEntity<Order> complete(@PathVariable Integer orderId) {
        return new ResponseEntity<>(orderService.completeOrder(orderId), HttpStatus.OK);
    }
}
