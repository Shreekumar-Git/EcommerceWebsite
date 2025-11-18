package com.EcommerceWebsite.service;

import com.EcommerceWebsite.model.*;
import com.EcommerceWebsite.repository.CartItemRepository;
import com.EcommerceWebsite.repository.OrderItemRepository;
import com.EcommerceWebsite.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;

    // ========================================
    // PLACE ORDER
    // ========================================
    @Override
    public Order placeOrder(User user) {

        List<CartItem> cart = cartItemRepository.findByUser(user);

        if (cart.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        double total = 0;

        order = orderRepository.save(order);

        for (CartItem ci : cart) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(ci.getProduct());
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getProduct().getPrice());

            total += oi.getPrice() * oi.getQuantity();

            orderItemRepository.save(oi);
        }

        order.setTotalAmount(total);
        orderRepository.save(order);

        cartService.clear(user); // clear cart after order placed

        return order;
    }

    // ========================================
    // LIST USER ORDERS
    // ========================================
    @Override
    public List<Order> listUserOrders(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    // ========================================
    // LIST ALL (ADMIN)
    // ========================================
    @Override
    public List<Order> listAll() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    // ========================================
    // GET BY ID
    // ========================================
    @Override
    public Order getById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    // ========================================
    // USER CANCEL ORDER
    // ========================================
    @Override
    public Order cancelOrder(Long id, User user) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not your order");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order cannot be cancelled now");
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    // ========================================
    // ADMIN ACCEPT ORDER → PAID
    // ========================================
    @Override
    public Order acceptOrder(Long id) {
        Order order = getById(id);

        if (order.getStatus() != OrderStatus.PENDING)
            throw new RuntimeException("Only pending orders can be accepted");

        order.setStatus(OrderStatus.PAID);
        return orderRepository.save(order);
    }

    // ========================================
    // SAVE (used by deliverOrder)
    // ========================================
    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    // ========================================
    // COUNT BY STATUS
    // ========================================
    @Override
    public long countByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }
    @Override
public Order deliverOrder(Long id) {
    Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    if (order.getStatus() != OrderStatus.PAID) {
        throw new RuntimeException("Only PAID orders can be marked as delivered");
    }

    order.setStatus(OrderStatus.DELIVERED);
    return orderRepository.save(order);
}
}
