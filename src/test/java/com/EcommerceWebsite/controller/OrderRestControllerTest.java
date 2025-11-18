package com.EcommerceWebsite.controller;

import com.EcommerceWebsite.model.Order;
import com.EcommerceWebsite.model.User;
import com.EcommerceWebsite.service.OrderService;
import com.EcommerceWebsite.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "test@mail.com", roles = { "USER" })
    void testPlaceOrderSuccess() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@mail.com");

        Order mockOrder = new Order();
        mockOrder.setId(1L);

        when(userService.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(orderService.placeOrder(any(User.class))).thenReturn(mockOrder);

        mockMvc.perform(post("/api/orders/place").with(csrf())) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1L));
    }
}
