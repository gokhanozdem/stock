package com.inghub.brokage.firm.stock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inghub.brokage.firm.stock.controller.dto.Order;
import com.inghub.brokage.firm.stock.repository.entity.Customer;
import com.inghub.brokage.firm.stock.repository.entity.OrderSide;
import com.inghub.brokage.firm.stock.repository.entity.Role;
import com.inghub.brokage.firm.stock.repository.entity.Status;
import com.inghub.brokage.firm.stock.service.OrderService;
import com.inghub.brokage.firm.stock.service.impl.CustomerServiceImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    Order order = new Order(1, 2, "BIMAS", OrderSide.BUY, new BigDecimal("10.00"), new BigDecimal("7.00"), Status.PENDING, LocalDateTime.now());

    @Test
    @WithMockUser(roles = "ADMIN")
    void create() throws Exception {
        given(orderServiceMock.saveOrder(any())).willReturn(order);
        Customer customer = new Customer();
        customer.setId(1);
        customer.setUsername("admin");
        customer.setPassword(passwordEncoder.encode("admin"));
        customer.setFullName("Admin");
        customer.setEnabled(true);
        customer.setRoles(List.of(new Role("ADMIN")));
        given(customerServiceImpl.getCustomerByUsername(anyString())).willReturn(Optional.of(customer));
        given(customerServiceImpl.getCustomerById(any())).willReturn(Optional.of(customer));
        ResultActions response = mockMvc.perform(post("/order/create").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}