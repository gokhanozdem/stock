package com.inghub.brokage.firm.stock.repository;

import com.inghub.brokage.firm.stock.repository.entity.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<AssetEntity, Integer> {
    List<AssetEntity> findAllByCustomerId(Integer customerId);

    AssetEntity findByCustomerIdAndAssetName(Integer customerId, String assetName);
}
