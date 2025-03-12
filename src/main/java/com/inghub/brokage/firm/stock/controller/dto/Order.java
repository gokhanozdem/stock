package com.inghub.brokage.firm.stock.controller.dto;

import com.inghub.brokage.firm.stock.repository.entity.OrderSide;
import com.inghub.brokage.firm.stock.repository.entity.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Order(Integer orderId, Integer customerId, String assetName, OrderSide orderSide, BigDecimal size,
                    BigDecimal price, Status status, LocalDateTime createDate) {
}
