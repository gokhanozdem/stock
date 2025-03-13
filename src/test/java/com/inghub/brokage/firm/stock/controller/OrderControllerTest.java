package com.inghub.brokage.firm.stock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inghub.brokage.firm.stock.controller.dto.Order;
import com.inghub.brokage.firm.stock.repository.entity.OrderSide;
import com.inghub.brokage.firm.stock.repository.entity.Status;
import com.inghub.brokage.firm.stock.service.AssetService;
import com.inghub.brokage.firm.stock.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    Order order = new Order(1,1,"BIMAS", OrderSide.BUY, new BigDecimal("10.00"), new BigDecimal("7.00"), Status.PENDING, LocalDateTime.now());

    @Test
    void create() throws Exception {
        given(orderServiceMock.saveOrder(any())).willReturn(order);
        ResultActions response = mockMvc.perform(post("/order/create").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}