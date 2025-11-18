package com.EcommerceWebsite.service;

import com.EcommerceWebsite.model.Order;
import com.EcommerceWebsite.model.OrderStatus;
import com.EcommerceWebsite.model.User;

import java.util.List;

public interface OrderService {

    Order placeOrder(User user);

    List<Order> listUserOrders(User user);

    List<Order> listAll();

    Order getById(Long id);


    Order deliverOrder(Long id);

    Order cancelOrder(Long id, User user);

    Order acceptOrder(Long id);

    Order save(Order order);

    long countByStatus(OrderStatus status);
}
