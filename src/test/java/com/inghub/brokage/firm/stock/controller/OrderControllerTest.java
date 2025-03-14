package com.inghub.brokage.firm.stock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.inghub.brokage.firm.stock.controller.dto.Order;
import com.inghub.brokage.firm.stock.repository.entity.Customer;
import com.inghub.brokage.firm.stock.repository.entity.OrderSide;
import com.inghub.brokage.firm.stock.repository.entity.Role;
import com.inghub.brokage.firm.stock.repository.entity.Status;
import com.inghub.brokage.firm.stock.service.OrderService;
import com.inghub.brokage.firm.stock.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderServiceMock;
    @MockitoBean
    private CustomerServiceImpl customerServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    private Order order;
    private Customer customer;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        order = new Order();
        order.setOrderId(1);
        order.setCustomerId(2);
        order.setAssetName("BIMAS");
        order.setOrderSide(OrderSide.BUY);
        order.setSize(new BigDecimal("10.00"));
        order.setPrice(new BigDecimal("7.00"));
        order.setCreateDate(LocalDateTime.now());
        order.setStatus(Status.PENDING);

        customer = new Customer();
        customer.setId(1);
        customer.setUsername("admin");
        customer.setPassword(passwordEncoder.encode("admin"));
        customer.setFullName("Admin");
        customer.setEnabled(true);
        customer.setRoles(List.of(new Role("ADMIN")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create() throws Exception {
        when(orderServiceMock.saveOrder(any())).thenReturn(order);
        when(customerServiceImpl.getCustomerByUsername(anyString())).thenReturn(Optional.of(customer));
        when(customerServiceImpl.getCustomerById(any())).thenReturn(Optional.of(customer));
        ResultActions response = mockMvc.perform(post("/order/create").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId", is(1)))
                .andExpect(jsonPath("$.customerId", is(2)))
                .andExpect(jsonPath("$.assetName", is("BIMAS")))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteOrder_Success() throws Exception {
        when(customerServiceImpl.getCustomerByUsername(anyString())).thenReturn(Optional.of(customer));
        when(customerServiceImpl.getCustomerById(any())).thenReturn(Optional.of(customer));
        when(orderServiceMock.getOrderByOrderId(any())).thenReturn(order);
        when(orderServiceMock.deleteOrder(1)).thenReturn(order);

        mockMvc.perform(delete("/order/{orderId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId", is(1)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteOrder_NotFound() throws Exception {
        when(orderServiceMock.getOrderByOrderId(any())).thenReturn(null);

        mockMvc.perform(delete("/order/{orderId}", 999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}