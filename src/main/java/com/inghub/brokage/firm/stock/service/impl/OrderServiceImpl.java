package com.inghub.brokage.firm.stock.service.impl;

import com.inghub.brokage.firm.stock.controller.dto.Asset;
import com.inghub.brokage.firm.stock.controller.dto.Order;
import com.inghub.brokage.firm.stock.repository.OrderRepository;
import com.inghub.brokage.firm.stock.repository.entity.OrderEntity;
import com.inghub.brokage.firm.stock.repository.entity.OrderSide;
import com.inghub.brokage.firm.stock.repository.entity.Status;
import com.inghub.brokage.firm.stock.service.AssetService;
import com.inghub.brokage.firm.stock.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    public static final String TRY = "TRY";
    private final OrderRepository orderRepository;
    @Autowired
    private final AssetService assetService;

    @Override
    public Order saveOrder(Order order) {
        validationForCreateOrder(order);
        checkAssetForCreateOrder(order);
        OrderEntity orderEntity = orderRepository.save(OrderEntity.builder()
                .customerId(order.customerId())
                .assetName(order.assetName())
                .orderSide(order.orderSide())
                .price(order.price())
                .size(order.size())
                .createDate(LocalDateTime.now())
                .status(Status.PENDING)
                .build());

        return new Order(orderEntity.getOrderId(),
                orderEntity.getCustomerId(),
                orderEntity.getAssetName(),
                orderEntity.getOrderSide(),
                orderEntity.getSize(),
                orderEntity.getPrice(),
                orderEntity.getStatus(),
                orderEntity.getCreateDate());
    }

    @Override
    public List<Order> getAllOrdersByCustomerIdAndDate(Integer customerId, LocalDate date) {
        List<OrderEntity> listOfOrderEntity = orderRepository.findByCustomerIdAndCreateDate(customerId, date);
        if (listOfOrderEntity.isEmpty()) {
            throw new RuntimeException("There is no order with customer id: " + customerId + " and date between now and entered date: " + date);
        }
        return listOfOrderEntity.stream().map(orderEntity ->
                new Order(orderEntity.getOrderId(),
                        orderEntity.getCustomerId(),
                        orderEntity.getAssetName(),
                        orderEntity.getOrderSide(),
                        orderEntity.getSize(),
                        orderEntity.getPrice(),
                        orderEntity.getStatus(),
                        orderEntity.getCreateDate())).collect(Collectors.toList());
    }

    @Override
    public Order getOrderByOrderId(Integer orderId) {
        if (orderRepository.findById(orderId).isPresent()) {
            OrderEntity orderEntity = orderRepository.findById(orderId).get();
            return new Order(orderEntity.getOrderId(),
                    orderEntity.getCustomerId(),
                    orderEntity.getAssetName(),
                    orderEntity.getOrderSide(),
                    orderEntity.getSize(),
                    orderEntity.getPrice(),
                    orderEntity.getStatus(),
                    orderEntity.getCreateDate());
        } else {
            throw new RuntimeException("Order not found with id:" + orderId);
        }
    }

    @Override
    public Order updateOrder(Order order, Status status) {
        OrderEntity orderEntity = orderRepository.save(OrderEntity.builder()
                .orderId(order.orderId())
                .customerId(order.customerId())
                .assetName(order.assetName())
                .orderSide(order.orderSide())
                .price(order.price())
                .size(order.size())
                .createDate(LocalDateTime.now())
                .status(status)
                .build());
        return new Order(orderEntity.getOrderId(),
                orderEntity.getCustomerId(),
                orderEntity.getAssetName(),
                orderEntity.getOrderSide(),
                orderEntity.getSize(),
                orderEntity.getPrice(),
                orderEntity.getStatus(),
                orderEntity.getCreateDate());
    }

    @Override
    public Order deleteOrder(Integer orderId) {
        Order currentOrder = getOrderByOrderId(orderId);
        if (currentOrder == null) {
            throw new RuntimeException("Order not found with id: " + orderId);
        }
        validationForOthers(currentOrder);

        if (currentOrder.orderSide().equals(OrderSide.BUY)) {
            BigDecimal totalPrice = new BigDecimal(String.valueOf(currentOrder.size().multiply(currentOrder.price())));
            Asset tryAsset = getAssetByCustomerIdAndAssetName(currentOrder.customerId(), TRY);
            updateAsset(new Asset(tryAsset.assetId(),
                    tryAsset.customerId(),
                    tryAsset.assetName(),
                    tryAsset.size(),
                    tryAsset.usableSize().add(totalPrice)));
        } else {
            Asset sellingAsset = getAssetByCustomerIdAndAssetName(currentOrder.customerId(), currentOrder.assetName());
            updateAsset(new Asset(sellingAsset.assetId(),
                    sellingAsset.customerId(),
                    sellingAsset.assetName(),
                    sellingAsset.size(),
                    sellingAsset.usableSize().add(currentOrder.size())));
        }

        return updateOrder(currentOrder, Status.CANCELLED);
    }

    @Override
    public Order completeOrder(Integer orderId) {
        Order currentOrder = getOrderByOrderId(orderId);
        if (currentOrder == null) {
            throw new RuntimeException("Order not found with id: " + orderId);
        }
        validationForOthers(currentOrder);
        BigDecimal totalPrice = calculateTotalPrice(currentOrder.size(), currentOrder.price());

        if (currentOrder.orderSide().equals(OrderSide.BUY)) {
            assetService.saveAsset(new Asset(null,
                    currentOrder.customerId(),
                    currentOrder.assetName(),
                    currentOrder.size(),
                    currentOrder.size()));

            Asset tryAsset = getAssetByCustomerIdAndAssetName(currentOrder.customerId(), TRY);
            updateAsset(new Asset(tryAsset.assetId(),
                    tryAsset.customerId(),
                    tryAsset.assetName(),
                    tryAsset.size().subtract(totalPrice),
                    tryAsset.usableSize()));
        } else { //SELL
            Asset sellingAsset = getAssetByCustomerIdAndAssetName(currentOrder.customerId(), currentOrder.assetName());
            BigDecimal assetSizeMinusUsable = sellingAsset.size().subtract(sellingAsset.usableSize());
            int diffBetweenOrderSizeAndAssetSize = currentOrder.size().compareTo(assetSizeMinusUsable);

            if (diffBetweenOrderSizeAndAssetSize < 0) {
                throw new RuntimeException("You can just match " + assetSizeMinusUsable + " size for " + sellingAsset.assetName() + " asset.");
            } else {
                int assetUsableZero = sellingAsset.usableSize().compareTo(new BigDecimal("0.00"));
                if (diffBetweenOrderSizeAndAssetSize == 0 && assetUsableZero == 0) {
                    assetService.deleteAsset(sellingAsset);
                } else if (diffBetweenOrderSizeAndAssetSize == 0 && assetUsableZero > 0) {
                    updateAsset(new Asset(sellingAsset.assetId(),
                            sellingAsset.customerId(),
                            sellingAsset.assetName(),
                            sellingAsset.usableSize(),
                            sellingAsset.usableSize()));
                }
            }

            Asset tryAsset = getAssetByCustomerIdAndAssetName(currentOrder.customerId(), TRY);
            updateAsset(new Asset(tryAsset.assetId(),
                    tryAsset.customerId(),
                    tryAsset.assetName(),
                    tryAsset.size().add(totalPrice),
                    tryAsset.usableSize().add(totalPrice)));
        }
        return updateOrder(currentOrder, Status.MATCHED);
    }

    public void checkAssetForCreateOrder(Order order) {
        if (order.orderSide().equals(OrderSide.BUY)) {
            Asset tryAsset = getAssetByCustomerIdAndAssetName(order.customerId(), TRY);
            BigDecimal totalPrice = calculateTotalPrice(order.size(), order.price());
            if (tryAsset.usableSize().compareTo(totalPrice) < 0) {
                throw new RuntimeException("Not Enough TRY Asset. TRY Asset: " + tryAsset.size() + ", Order Size: " + order.size() + ", Order Price: " + order.price() + ", Total: " + totalPrice);
            }
            updateAsset(new Asset(tryAsset.assetId(),
                    tryAsset.customerId(),
                    tryAsset.assetName(),
                    tryAsset.size(),
                    tryAsset.usableSize().subtract(totalPrice)));
        } else if (order.orderSide().equals(OrderSide.SELL)) {
            Asset sellingAsset = getAssetByCustomerIdAndAssetName(order.customerId(), order.assetName());
            if (sellingAsset.usableSize().compareTo(order.size()) < 0) {
                throw new RuntimeException("Not Enough" + order.assetName() + " Asset. " + sellingAsset.assetName() + " Asset: " + sellingAsset.size() + ", Order Size: " + order.size());
            }
            updateAsset(new Asset(sellingAsset.assetId(),
                    sellingAsset.customerId(),
                    sellingAsset.assetName(),
                    sellingAsset.size(),
                    sellingAsset.usableSize().subtract(order.size())));
        }
    }

    public Asset getAssetByCustomerIdAndAssetName(Integer customerId, String assetName) {
        return assetService.getAssetByCustomerIdAndAssetName(customerId, assetName);
    }

    public BigDecimal calculateTotalPrice(BigDecimal size, BigDecimal price) {
        return price.multiply(size).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void updateAsset(Asset asset) {
        assetService.updateAsset(asset);
    }

    public void validationForCreateOrder(Order order) {
        if (!(order.orderSide().equals(OrderSide.BUY) || order.orderSide().equals(OrderSide.SELL))) {
            throw new RuntimeException("Order side is not valid. Order side is " + order.orderSide());
        }
    }

    public void validationForOthers(Order order) {
        validationForCreateOrder(order);
        if (!order.status().equals(Status.PENDING)) {
            throw new RuntimeException("Order status should be PENDING in order to complete. Status is " + order.status());
        }
    }
}
