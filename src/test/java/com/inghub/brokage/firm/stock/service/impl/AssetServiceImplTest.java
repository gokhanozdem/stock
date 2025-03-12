package com.inghub.brokage.firm.stock.service.impl;

import com.inghub.brokage.firm.stock.repository.AssetRepository;
import com.inghub.brokage.firm.stock.repository.entity.AssetEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetServiceImplTest {

    @Mock
    private AssetRepository assetRepositoryMock;

    @InjectMocks
    private AssetServiceImpl assetServiceImpl;

    List<AssetEntity> assetEntityList = List.of(
            new AssetEntity(1,1,"TRY",new BigDecimal("100000.00"),new BigDecimal("100000.00")),
            new AssetEntity(2,1,"ASELS",new BigDecimal("100.00"),new BigDecimal("100.00")),
            new AssetEntity(3,2,"MGROS",new BigDecimal("5.00"),new BigDecimal("5.00"))
            );

    List<AssetEntity> assetEntityListOneValue = List.of(
            new AssetEntity(4,3,"FROTO",new BigDecimal("50.00"),new BigDecimal("15.00"))
    );

    @Test
    void getAllAssetsByCustomerId_Success() {
        when(assetRepositoryMock.findAllByCustomerId(anyInt())).thenReturn(assetEntityList);
        assertEquals("TRY", assetServiceImpl.getAllAssetsByCustomerId(anyInt()).getFirst().assetName());
        assertEquals(3, assetServiceImpl.getAllAssetsByCustomerId(anyInt()).size());
        assertEquals(2, assetServiceImpl.getAllAssetsByCustomerId(anyInt()).getLast().customerId());
    }

    @Test
    void getAllAssetsByCustomerId_OneValue() {
        when(assetRepositoryMock.findAllByCustomerId(anyInt())).thenReturn(assetEntityListOneValue);
        assertEquals("FROTO", assetServiceImpl.getAllAssetsByCustomerId(anyInt()).getFirst().assetName());
        assertEquals(1, assetServiceImpl.getAllAssetsByCustomerId(anyInt()).size());
        assertEquals(3, assetServiceImpl.getAllAssetsByCustomerId(anyInt()).getFirst().customerId());
        assertEquals(3, assetServiceImpl.getAllAssetsByCustomerId(anyInt()).getLast().customerId());
    }

}