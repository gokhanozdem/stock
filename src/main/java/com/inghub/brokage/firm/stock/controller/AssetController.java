package com.inghub.brokage.firm.stock.controller;

import com.inghub.brokage.firm.stock.controller.dto.Asset;
import com.inghub.brokage.firm.stock.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/asset")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @GetMapping("/list/{customerId}")
    public ResponseEntity<List<Asset>> getAllAssetsByCustomerId(@PathVariable Integer customerId) {
        return new ResponseEntity<>(assetService.getAllAssetsByCustomerId(customerId), HttpStatus.OK);
    }

    @GetMapping("/list/search")
    public ResponseEntity<List<Asset>> getAllAssetsByParam(@RequestParam(value = "assetName", required = false) String assetName,
                                @RequestParam(value = "size", required = false) BigDecimal size,
                                @RequestParam(value = "usableSize", required = false) BigDecimal usableSize) {
        return new ResponseEntity<>(assetService.getAllAssetsByParam(assetName, size, usableSize), HttpStatus.OK);
    }

}
