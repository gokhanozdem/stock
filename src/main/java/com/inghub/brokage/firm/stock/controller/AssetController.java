package com.inghub.brokage.firm.stock.controller;

import com.inghub.brokage.firm.stock.controller.dto.Asset;
import com.inghub.brokage.firm.stock.repository.entity.Customer;
import com.inghub.brokage.firm.stock.service.AssetService;
import com.inghub.brokage.firm.stock.service.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/asset")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Autowired
    private CustomerServiceImpl customerServiceImpl;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAsset(@RequestBody Asset asset) {
        assetService.saveAsset(asset);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/list/{customerId}")
    public ResponseEntity<?> getAllAssetsByCustomerId(@PathVariable Integer customerId) {
        checkAuthorized(customerId);
        return new ResponseEntity<>(assetService.getAllAssetsByCustomerId(customerId), HttpStatus.OK);
    }

    @GetMapping("/list/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Asset>> getAllAssetsByParam(@RequestParam(value = "assetName", required = false) String assetName,
                                                           @RequestParam(value = "size", required = false) BigDecimal size,
                                                           @RequestParam(value = "usableSize", required = false) BigDecimal usableSize) {
        return new ResponseEntity<>(assetService.getAllAssetsByParam(assetName, size, usableSize), HttpStatus.OK);
    }

    private void checkAuthorized(Integer customerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (!isAdmin) {
            Customer currentCustomer = customerServiceImpl.getCustomerByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("Current user not found"));
            if (!currentCustomer.getId().equals(customerId)) {
                throw new AccessDeniedException("You are not authorized to access this customer's data");
            }
        }

        if (isAdmin && customerId == 1) {
            throw new AccessDeniedException("It is not allowed to do any operation with customer id : 1 . Please change customer id and try it again");
        }

        if (customerServiceImpl.getCustomerById(customerId).isEmpty()) {
            throw new RuntimeException("Customer not found");
        }
    }

}
