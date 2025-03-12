package com.inghub.brokage.firm.stock.repository;

import com.inghub.brokage.firm.stock.repository.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    @Query(value = "SELECT * FROM Orders WHERE CUSTOMER_ID = ?1 AND CREATE_DATE >= ?2", nativeQuery = true)
    List<OrderEntity> findByCustomerIdAndCreateDate(Integer customerId, LocalDate date);
}
