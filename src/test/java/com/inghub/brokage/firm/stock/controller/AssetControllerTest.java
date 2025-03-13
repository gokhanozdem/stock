package com.inghub.brokage.firm.stock.controller;

import com.inghub.brokage.firm.stock.controller.dto.Asset;
import com.inghub.brokage.firm.stock.service.AssetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AssetController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssetService assetServiceMock;

    List<Asset> assetList = List.of(
            new Asset(1, 1, "TRY", new BigDecimal("100000.00"), new BigDecimal("70000.00")),
            new Asset(2, 2, "TRY", new BigDecimal("200000.00"), new BigDecimal("150000.00")),
            new Asset(3, 3, "TRY", new BigDecimal("300000.00"), new BigDecimal("275000.00")),
            new Asset(4, 4, "TRY", new BigDecimal("400000.00"), new BigDecimal("400000.00")),
            new Asset(5, 5, "TRY", new BigDecimal("500000.00"), new BigDecimal("500000.00")),
            new Asset(6, 6, "TRY", new BigDecimal("600000.00"), new BigDecimal("600000.00")),
            new Asset(7, 7, "TRY", new BigDecimal("700000.00"), new BigDecimal("700000.00")),
            new Asset(8, 8, "TRY", new BigDecimal("800000.00"), new BigDecimal("800000.00")),
            new Asset(9, 9, "TRY", new BigDecimal("900000.00"), new BigDecimal("900000.00")),
            new Asset(10, 10, "TRY", new BigDecimal("1000000.00"), new BigDecimal("1000000.00")),
            new Asset(11, 1, "MGROS", new BigDecimal("100.00"), new BigDecimal("100.00")),
            new Asset(12, 1, "FROTO", new BigDecimal("200.00"), new BigDecimal("120.00")),
            new Asset(13, 2, "FROTO", new BigDecimal("300.00"), new BigDecimal("300.00")),
            new Asset(14, 2, "ASELS", new BigDecimal("400.00"), new BigDecimal("350.00")),
            new Asset(15, 3, "ASELS", new BigDecimal("500.00"), new BigDecimal("500.00"))
    );

    @Test
    void getAllAssetsByCustomerId() throws Exception {
        given(assetServiceMock.getAllAssetsByCustomerId(anyInt())).willReturn(assetList.stream().filter(selection -> selection.customerId().equals(1)).collect(Collectors.toList()));
        ResultActions response = mockMvc.perform(get("/asset/list/{customerId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"assetId\":1,\"customerId\":1,\"assetName\":\"TRY\",\"size\":100000.00,\"usableSize\":70000.00},{\"assetId\":11,\"customerId\":1,\"assetName\":\"MGROS\",\"size\":100.00,\"usableSize\":100.00},{\"assetId\":12,\"customerId\":1,\"assetName\":\"FROTO\",\"size\":200.00,\"usableSize\":120.00}]"))
                .andDo(print());
    }

}