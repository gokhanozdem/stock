package com.inghub.brokage.firm.stock.service;

import com.inghub.brokage.firm.stock.controller.dto.Asset;

import java.math.BigDecimal;
import java.util.List;

public interface AssetService {
    List<Asset> getAllAssetsByCustomerId(Integer customerId);

    Asset getAssetByCustomerIdAndAssetName(Integer customerId, String assetName);

    void saveAsset(Asset asset);

    void updateAsset(Asset asset);

    void deleteAsset(Asset asset);

    List<Asset> getAllAssetsByParam(String assetName, BigDecimal size, BigDecimal usableSize);
}
