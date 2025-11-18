package com.EcommerceWebsite.repository;

import com.EcommerceWebsite.model.Order;
import com.EcommerceWebsite.model.User;
import com.EcommerceWebsite.model.OrderStatus;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")   // uses application-test.properties
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;  // 👈 MUST BE ADDED!

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("pass123");  // required if @NotNull used
        userRepository.save(user);    // 👈 SAVE USER FIRST
    }

    @Test
    void testSaveAndFindByUser() {
        Order order = new Order();
        order.setUser(user);  // user already saved
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        orderRepository.save(order);

        List<Order> result = orderRepository.findByUser(user);
        assertEquals(1, result.size());
    }

    @Test
    void testCountByStatus() {
        Order order1 = new Order(); order1.setUser(user); order1.setStatus(OrderStatus.PENDING);
        Order order2 = new Order(); order2.setUser(user); order2.setStatus(OrderStatus.DELIVERED);

        orderRepository.save(order1);
        orderRepository.save(order2);

        assertEquals(1, orderRepository.countByStatus(OrderStatus.PENDING));
        assertEquals(1, orderRepository.countByStatus(OrderStatus.DELIVERED));
    }

    @Test
    void testFindAllSorted() {
        Order order1 = new Order(); order1.setUser(user); order1.setCreatedAt(LocalDateTime.now().minusDays(2));
        Order order2 = new Order(); order2.setUser(user); order2.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order1);
        orderRepository.save(order2);

        List<Order> result = orderRepository.findAllByOrderByCreatedAtDesc();
        assertEquals(2, result.size());
        assertEquals(order2.getCreatedAt(), result.get(0).getCreatedAt()); // newest first
    }
}
