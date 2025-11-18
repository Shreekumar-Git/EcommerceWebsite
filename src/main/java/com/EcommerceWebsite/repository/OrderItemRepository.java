package com.EcommerceWebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EcommerceWebsite.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> { }
