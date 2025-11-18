package com.EcommerceWebsite.controller;

import com.EcommerceWebsite.model.Order;
import com.EcommerceWebsite.model.OrderStatus;
import com.EcommerceWebsite.model.User;
import com.EcommerceWebsite.service.OrderService;
import com.EcommerceWebsite.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;
    private final UserService userService;

    // 1) PLACE ORDER (USER)
  @PostMapping("/place")
public Map<String, Object> placeOrder(@AuthenticationPrincipal UserDetails userDetails) {

    User user = userService.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

    Order order = orderService.placeOrder(user);

    return Map.of("orderId", order.getId());
}


    // 2) LIST MY ORDERS
    @GetMapping("/my")
    public List<Order> myOrders(@AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new RuntimeException("User not authenticated");
        }

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderService.listUserOrders(user);
    }

    // 3) GET ORDER BY ID
    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getById(id);
    }

    // 4) CANCEL ORDER (USER)
    @PostMapping("/cancel/{id}")
    public Order cancelOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new RuntimeException("User not authenticated");
        }

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderService.cancelOrder(id, user);
    }

    // 5) ADMIN ACCEPT ORDER
    @PostMapping("/accept/{id}")
    public Order acceptOrder(@PathVariable Long id) {
        return orderService.acceptOrder(id);
    }

    // 6) ADMIN MARK DELIVERED
    @PostMapping("/deliver/{id}")
    public Order deliverOrder(@PathVariable Long id) {

        Order order = orderService.getById(id);

        if (order != null && order.getStatus() == OrderStatus.PAID) {
            order.setStatus(OrderStatus.DELIVERED);
            return orderService.save(order);
        }

        return order;
    }

    // 7) LIST ALL ORDERS (ADMIN)
    @GetMapping("/all")
    public List<Order> allOrders() {
        return orderService.listAll();
    }
}
