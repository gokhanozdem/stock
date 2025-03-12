package com.inghub.brokage.firm.stock.controller.dto;

import java.math.BigDecimal;

public record Asset(Integer assetId, Integer customerId, String assetName, BigDecimal size, BigDecimal usableSize) {
}
