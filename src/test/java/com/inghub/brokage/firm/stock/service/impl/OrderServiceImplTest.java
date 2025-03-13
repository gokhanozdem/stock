package com.inghub.brokage.firm.stock.service.impl;

import com.inghub.brokage.firm.stock.service.AssetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private AssetService assetServiceMock;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;


    @Test
    void calculateTotalPrice() {
        assertEquals(new BigDecimal("0.00"), orderServiceImpl.calculateTotalPrice(BigDecimal.ZERO, BigDecimal.ZERO));
        assertEquals(new BigDecimal("20.00"), orderServiceImpl.calculateTotalPrice(new BigDecimal("2.00"), new BigDecimal("10.00")));
        assertEquals(new BigDecimal("30.13"), orderServiceImpl.calculateTotalPrice(new BigDecimal("2.60"), new BigDecimal("11.59")));
    }
}