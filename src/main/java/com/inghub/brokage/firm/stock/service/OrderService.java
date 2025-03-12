package com.inghub.brokage.firm.stock.service;

import com.inghub.brokage.firm.stock.controller.dto.Order;
import com.inghub.brokage.firm.stock.repository.entity.Status;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<Order> getAllOrdersByCustomerIdAndDate(Integer customerId, LocalDate date);

    Order getOrderByOrderId(Integer orderId);

    Order saveOrder(Order order);

    Order updateOrder(Order order, Status status);

    Order deleteOrder(Integer orderId);

    Order completeOrder(Integer orderId);
}
