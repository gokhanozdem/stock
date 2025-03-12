package com.inghub.brokage.firm.stock.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;
    private Integer customerId;
    private String assetName;
    @Enumerated(EnumType.STRING)
    private OrderSide orderSide;
    private BigDecimal size;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime createDate;
}
