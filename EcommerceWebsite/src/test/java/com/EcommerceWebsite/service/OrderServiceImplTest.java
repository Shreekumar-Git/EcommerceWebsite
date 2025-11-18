package com.EcommerceWebsite.service;

import com.EcommerceWebsite.model.*;
import com.EcommerceWebsite.repository.CartItemRepository;
import com.EcommerceWebsite.repository.OrderItemRepository;
import com.EcommerceWebsite.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private CartItem cartItem;
    private Order order;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        Product product = new Product();
        product.setPrice(1000.0);

        cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        order = new Order();
        order.setId(1L);
        order.setUser(user);
    }

    @Test
    void testPlaceOrderSuccess() {
        when(cartItemRepository.findByUser(user)).thenReturn(List.of(cartItem));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order savedOrder = orderService.placeOrder(user);

        assertNotNull(savedOrder);
        verify(cartService, times(1)).clear(user);
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
        verify(orderRepository, times(2)).save(any(Order.class));
    }

    @Test
    void testPlaceOrderEmptyCart() {
        when(cartItemRepository.findByUser(user)).thenReturn(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            orderService.placeOrder(user);
        });

        assertEquals("Cart is empty", ex.getMessage());
    }

    @Test
    void testCancelOrderSuccess() {
        order.setStatus(OrderStatus.PENDING);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.cancelOrder(1L, user);

        assertEquals(OrderStatus.CANCELLED, result.getStatus());
    }

    @Test
    void testCancelOrderWrongUser() {
        User anotherUser = new User();
        anotherUser.setId(99L);
        
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            orderService.cancelOrder(1L, anotherUser);
        });

        assertEquals("Not your order", ex.getMessage());
    }

    @Test
    void testAcceptOrderSuccess() {
        order.setStatus(OrderStatus.PENDING);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.acceptOrder(1L);
        assertEquals(OrderStatus.PAID, result.getStatus());
    }

    @Test
    void testDeliverOrderSuccess() {
        order.setStatus(OrderStatus.PAID);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.deliverOrder(1L);
        assertEquals(OrderStatus.DELIVERED, result.getStatus());
    }
}
