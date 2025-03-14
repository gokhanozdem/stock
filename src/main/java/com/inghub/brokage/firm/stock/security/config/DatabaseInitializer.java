package com.inghub.brokage.firm.stock.security.config;

import com.inghub.brokage.firm.stock.controller.dto.Asset;
import com.inghub.brokage.firm.stock.repository.CustomerRepository;
import com.inghub.brokage.firm.stock.repository.RoleRepository;
import com.inghub.brokage.firm.stock.repository.entity.Customer;
import com.inghub.brokage.firm.stock.repository.entity.Role;
import com.inghub.brokage.firm.stock.service.AssetService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AssetService assetService;

    public DatabaseInitializer(RoleRepository roleRepository,
                               CustomerRepository customerRepository,
                               PasswordEncoder passwordEncoder,
                               AssetService assetService) {
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.assetService = assetService;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.saveAll(Arrays.asList(new Role("ADMIN"), new Role("CUSTOMER")));
        }

        if (!customerRepository.existsByUsername("admin")) {
            Customer admin = new Customer("admin", passwordEncoder.encode("admin"), "Admin admin");

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));

            admin.setRoles(List.of(adminRole));
            customerRepository.save(admin);
        }

        BigDecimal sizes = BigDecimal.ZERO;
        assetService.saveAsset(new Asset(null, 1, "ADMIN", sizes, sizes));

    }
}