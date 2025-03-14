package com.inghub.brokage.firm.stock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.inghub.brokage.firm.stock.controller.dto.Asset;
import com.inghub.brokage.firm.stock.controller.dto.Order;
import com.inghub.brokage.firm.stock.repository.entity.Customer;
import com.inghub.brokage.firm.stock.repository.entity.OrderSide;
import com.inghub.brokage.firm.stock.repository.entity.Role;
import com.inghub.brokage.firm.stock.repository.entity.Status;
import com.inghub.brokage.firm.stock.service.AssetService;
import com.inghub.brokage.firm.stock.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AssetController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AssetControllerTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssetService assetServiceMock;
    @MockitoBean
    private CustomerServiceImpl customerServiceImpl;

    private ArrayList<Asset> assets = new ArrayList<>();
    private Customer customer;

    @BeforeEach
    public void setup() {
        assets.add(new Asset(1, 2, "TRY", new BigDecimal("100000.00"), new BigDecimal("70000.00")));
        assets.add(new Asset(2, 3, "TRY", new BigDecimal("200000.00"), new BigDecimal("150000.00")));
        assets.add(new Asset(3, 4, "TRY", new BigDecimal("300000.00"), new BigDecimal("275000.00")));
        assets.add(new Asset(4, 5, "TRY", new BigDecimal("400000.00"), new BigDecimal("400000.00")));
        assets.add(new Asset(5, 6, "TRY", new BigDecimal("500000.00"), new BigDecimal("500000.00")));
        assets.add(new Asset(6, 7, "TRY", new BigDecimal("600000.00"), new BigDecimal("600000.00")));
        assets.add(new Asset(7, 8, "TRY", new BigDecimal("700000.00"), new BigDecimal("700000.00")));
        assets.add(new Asset(8, 9, "TRY", new BigDecimal("800000.00"), new BigDecimal("800000.00")));
        assets.add(new Asset(9, 10, "TRY", new BigDecimal("900000.00"), new BigDecimal("900000.00")));
        assets.add(new Asset(10, 11, "TRY", new BigDecimal("1000000.00"), new BigDecimal("1000000.00")));
        assets.add(new Asset(11, 2, "MGROS", new BigDecimal("100.00"), new BigDecimal("100.00")));
        assets.add(new Asset(12, 2, "FROTO", new BigDecimal("200.00"), new BigDecimal("120.00")));
        assets.add(new Asset(13, 3, "FROTO", new BigDecimal("300.00"), new BigDecimal("300.00")));
        assets.add(new Asset(14, 3, "ASELS", new BigDecimal("400.00"), new BigDecimal("350.00")));
        assets.add(new Asset(15, 4, "ASELS", new BigDecimal("500.00"), new BigDecimal("500.00")));

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
    void getAllAssetsByCustomerId() throws Exception {
        when(assetServiceMock.getAllAssetsByCustomerId(anyInt())).thenReturn(assets.stream().filter(selection -> selection.customerId().equals(2)).collect(Collectors.toList()));
        when(customerServiceImpl.getCustomerByUsername(anyString())).thenReturn(Optional.of(customer));
        when(customerServiceImpl.getCustomerById(any())).thenReturn(Optional.of(customer));
        ResultActions response = mockMvc.perform(get("/asset/list/{customerId}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(content().string("[{\"assetId\":1,\"customerId\":2,\"assetName\":\"TRY\",\"size\":100000.00,\"usableSize\":70000.00},{\"assetId\":11,\"customerId\":2,\"assetName\":\"MGROS\",\"size\":100.00,\"usableSize\":100.00},{\"assetId\":12,\"customerId\":2,\"assetName\":\"FROTO\",\"size\":200.00,\"usableSize\":120.00}]"))
                .andDo(print());
    }

}