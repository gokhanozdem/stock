package com.inghub.brokage.firm.stock.service.impl;

import com.inghub.brokage.firm.stock.controller.dto.Asset;
import com.inghub.brokage.firm.stock.repository.AssetRepository;
import com.inghub.brokage.firm.stock.repository.entity.AssetEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetServiceImplTest {

    @Mock
    private AssetRepository assetRepositoryMock;

    @InjectMocks
    private AssetServiceImpl assetServiceImpl;

    List<AssetEntity> assetEntityList = List.of(
            new AssetEntity(1, 2, "TRY", new BigDecimal("100000.00"), new BigDecimal("70000.00")),
            new AssetEntity(2, 3, "TRY", new BigDecimal("200000.00"), new BigDecimal("150000.00")),
            new AssetEntity(3, 4, "TRY", new BigDecimal("300000.00"), new BigDecimal("275000.00")),
            new AssetEntity(4, 5, "TRY", new BigDecimal("400000.00"), new BigDecimal("400000.00")),
            new AssetEntity(5, 6, "TRY", new BigDecimal("500000.00"), new BigDecimal("500000.00")),
            new AssetEntity(6, 7, "TRY", new BigDecimal("600000.00"), new BigDecimal("600000.00")),
            new AssetEntity(7, 8, "TRY", new BigDecimal("700000.00"), new BigDecimal("700000.00")),
            new AssetEntity(8, 9, "TRY", new BigDecimal("800000.00"), new BigDecimal("800000.00")),
            new AssetEntity(9, 10, "TRY", new BigDecimal("900000.00"), new BigDecimal("900000.00")),
            new AssetEntity(10, 11, "TRY", new BigDecimal("1000000.00"), new BigDecimal("1000000.00")),
            new AssetEntity(11, 2, "MGROS", new BigDecimal("100.00"), new BigDecimal("100.00")),
            new AssetEntity(12, 2, "FROTO", new BigDecimal("200.00"), new BigDecimal("120.00")),
            new AssetEntity(13, 3, "FROTO", new BigDecimal("300.00"), new BigDecimal("300.00")),
            new AssetEntity(14, 3, "ASELS", new BigDecimal("400.00"), new BigDecimal("350.00")),
            new AssetEntity(15, 4, "ASELS", new BigDecimal("500.00"), new BigDecimal("500.00"))
    );

    @Test
    void getAllAssetsByCustomerId_Success() {
        when(assetRepositoryMock.findAllByCustomerId(anyInt())).thenReturn(assetEntityList.stream().filter(asset -> asset.getCustomerId().equals(2)).collect(Collectors.toList()));
        List<Asset> assetList = assetServiceImpl.getAllAssetsByCustomerId(anyInt());
        assertEquals("TRY", assetList.getFirst().assetName());
        assertEquals(3, assetList.size());
    }

    @Test
    void getAllAssetsByParam_Success_WithAllParam_Case1() {
        when(assetRepositoryMock.findAll()).thenReturn(assetEntityList);
        List<Asset> assetList = assetServiceImpl.getAllAssetsByParam("TRY", new BigDecimal("550000.00"), new BigDecimal("175000.00"));
        assertEquals(2, assetList.size());
    }

    @Test
    void getAllAssetsByParam_Success_WithAllParam_Case2() {
        when(assetRepositoryMock.findAll()).thenReturn(assetEntityList);
        List<Asset> assetList = assetServiceImpl.getAllAssetsByParam("FROTO", new BigDecimal("350.00"), new BigDecimal("310.00"));
        assertEquals(2, assetList.size());
    }

    @Test
    void getAllAssetsByParam_Success_WithAllParam_Case3() {
        when(assetRepositoryMock.findAll()).thenReturn(assetEntityList);
        List<Asset> assetList = assetServiceImpl.getAllAssetsByParam("ASELS", new BigDecimal("600.00"), new BigDecimal("400.00"));
        assertEquals(1, assetList.size());
    }

    @Test
    void getAllAssetsByParam_Success_WithOneParam_Case1() {
        when(assetRepositoryMock.findAll()).thenReturn(assetEntityList);
        List<Asset> assetList = assetServiceImpl.getAllAssetsByParam("TRY", null, null);
        assertEquals(10, assetList.size());
    }

    @Test
    void getAllAssetsByParam_Success_WithOneParam_Case2() {
        when(assetRepositoryMock.findAll()).thenReturn(assetEntityList);
        List<Asset> assetList = assetServiceImpl.getAllAssetsByParam(null, new BigDecimal("600.00"), null);
        assertEquals(5, assetList.size());
    }

    @Test
    void getAllAssetsByParam_Success_WithOneParam_Case3() {
        when(assetRepositoryMock.findAll()).thenReturn(assetEntityList);
        List<Asset> assetList = assetServiceImpl.getAllAssetsByParam(null, null, new BigDecimal("125000.00"));
        assertEquals(6, assetList.size());
    }

    @Test
    void getAllAssetsByParam_Success_WithoutParam() {
        when(assetRepositoryMock.findAll()).thenReturn(assetEntityList);
        List<Asset> assetList = assetServiceImpl.getAllAssetsByParam(null, null, null);
        assertEquals(15, assetList.size());
    }

    @Test
    void getAllAssetsByParam_EmptyList_WithOneParam() {
        when(assetRepositoryMock.findAll()).thenReturn(assetEntityList);
        List<Asset> assetList = assetServiceImpl.getAllAssetsByParam("BIMAS", null, null);
        assertEquals(0, assetList.size());
    }

}