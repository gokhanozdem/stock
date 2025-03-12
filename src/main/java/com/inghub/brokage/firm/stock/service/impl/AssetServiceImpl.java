package com.inghub.brokage.firm.stock.service.impl;

import com.inghub.brokage.firm.stock.controller.dto.Asset;
import com.inghub.brokage.firm.stock.repository.AssetRepository;
import com.inghub.brokage.firm.stock.repository.entity.AssetEntity;
import com.inghub.brokage.firm.stock.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private Logger logger = Logger.getLogger(AssetServiceImpl.class.getName());


    @Override
    public List<Asset> getAllAssetsByCustomerId(Integer customerId) {
        List<AssetEntity> listOfAssetEntity = assetRepository.findAllByCustomerId(customerId);
        if (listOfAssetEntity == null || listOfAssetEntity.isEmpty()) {
            throw new RuntimeException("No assets found for customer id " + customerId);
        }
        return listOfAssetEntity.stream().map(assetEntity ->
                new Asset(assetEntity.getAssetId(),
                        assetEntity.getCustomerId(),
                        assetEntity.getAssetName(),
                        assetEntity.getSize(),
                        assetEntity.getUsableSize())).collect(Collectors.toList());
    }

    @Override
    public Asset getAssetByCustomerIdAndAssetName(Integer customerId, String assetName) {
        AssetEntity assetEntity = assetRepository.findByCustomerIdAndAssetName(customerId, assetName);
        if (assetEntity == null) {
            throw new RuntimeException("You can't do any operation with this asset as you don't have. Asset name: " + assetName);
        }
        return new Asset(assetEntity.getAssetId(),
                assetEntity.getCustomerId(),
                assetEntity.getAssetName(),
                assetEntity.getSize(),
                assetEntity.getUsableSize());
    }

    @Override
    public void saveAsset(Asset asset) {
        assetRepository.save(AssetEntity.builder()
                .customerId(asset.customerId())
                .assetName(asset.assetName())
                .size(asset.size())
                .usableSize(asset.usableSize())
                .build());
    }

    @Override
    public void updateAsset(Asset asset) {
        assetRepository.save(AssetEntity.builder()
                .assetId(asset.assetId())
                .customerId(asset.customerId())
                .assetName(asset.assetName())
                .size(asset.size())
                .usableSize(asset.usableSize())
                .build());
    }

    @Override
    public void deleteAsset(Asset asset) {
        assetRepository.delete(AssetEntity.builder()
                .assetId(asset.assetId())
                .customerId(asset.customerId())
                .assetName(asset.assetName())
                .size(asset.size())
                .usableSize(asset.usableSize())
                .build());
    }

    @Override
    public List<Asset> getAllAssetsByParam(String assetName, BigDecimal size, BigDecimal usableSize) {
        ArrayList<AssetEntity> assetArrayList = new ArrayList<>();
        if (!(assetName == null || assetName.isEmpty())) {
            assetArrayList = assetRepository.findAll().stream()
                    .filter(assetEntity -> assetName.equalsIgnoreCase(assetEntity.getAssetName()))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        if (size != null) {
            assetArrayList = assetArrayList.stream()
                    .filter(assetEntity -> size.compareTo(assetEntity.getSize()) > 0)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        if (usableSize != null) {
            assetArrayList = assetArrayList.stream()
                    .filter(assetEntity -> usableSize.compareTo(assetEntity.getUsableSize()) > 0)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        return assetArrayList.stream().map(assetEntity -> new Asset(assetEntity.getAssetId(),
                        assetEntity.getCustomerId(),
                        assetEntity.getAssetName(),
                        assetEntity.getSize(),
                        assetEntity.getUsableSize()))
                .collect(Collectors.toList());
    }
}
