package com.EcommerceWebsite.repository;

import com.EcommerceWebsite.model.Order;
import com.EcommerceWebsite.model.OrderStatus;
import com.EcommerceWebsite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);

    List<Order> findByUserOrderByCreatedAtDesc(User user);

List<Order> findAllByOrderByCreatedAtDesc();


    long countByStatus(OrderStatus status);

}
