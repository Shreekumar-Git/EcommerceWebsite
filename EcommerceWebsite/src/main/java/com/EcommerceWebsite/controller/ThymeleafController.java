package com.EcommerceWebsite.controller;

import com.EcommerceWebsite.model.Product;
import com.EcommerceWebsite.model.User;
import com.EcommerceWebsite.model.Order;
import com.EcommerceWebsite.model.OrderStatus;
import com.EcommerceWebsite.service.CartService;
import com.EcommerceWebsite.service.OrderService;
import com.EcommerceWebsite.service.ProductService;
import com.EcommerceWebsite.service.UserService;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ThymeleafController {

    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;
    private final OrderService orderService;

    /* ===================== HOME PAGE ===================== */

    @GetMapping("/")
    public String home(Model model) {
        List<Product> products = productService.listAllActive()
                .stream()
                .filter(Objects::nonNull)
                .toList();

        model.addAttribute("products", products);
        return "index";
    }

    /* ===================== AUTH PAGES ===================== */

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/register")
    public String register(Model m) {
        m.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute User user) {
        userService.register(user);
        return "redirect:/login";
    }

   
    /* ===================== SEARCH ===================== */

    @GetMapping("/search")
    public String searchProducts(@RequestParam String keyword, Model model) {
        List<Product> results = productService.searchByNameOrCategory(keyword);
        model.addAttribute("products", results);
        model.addAttribute("keyword", keyword);
        return "index";
    }

    /* ===================== PRODUCT PAGE ===================== */

    @GetMapping("/product/{id}")
    public String productPage(@PathVariable Long id, Model model) {

        Product product = productService.get(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        model.addAttribute("product", product);

        return "product-page";
    }

    /* ===================== CART PAGE ===================== */

    @GetMapping("/cart")
    public String cartPage(
            @AuthenticationPrincipal UserDetails ud,
            Model model) {

        if (ud == null) return "redirect:/login";

        User user = userService.findByEmail(ud.getUsername()).orElseThrow();

        model.addAttribute("cart", cartService.getCartForUser(user));

        return "cart";
    }

    /* ===================== CHECKOUT PAGE ===================== */

    @GetMapping("/checkout")
    public String checkout(@AuthenticationPrincipal UserDetails ud, Model model) {
        if (ud == null) return "redirect:/login";

        User user = userService.findByEmail(ud.getUsername()).orElseThrow();
        var cart = cartService.getCartForUser(user);

        double total = cart.stream()
                .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
                .sum();

        model.addAttribute("cart", cart);
        model.addAttribute("total", total);

        return "checkout";
    }

    /* ===================== ORDER CONFIRMATION ===================== */

    @GetMapping("/order/confirmation")
    public String confirmation(@RequestParam Long id, Model model) {
        Order order = orderService.getById(id);

        model.addAttribute("order", order);
        return "order-confirmation";
    }

    /* ===================== USER ORDERS ===================== */

    @GetMapping("/orders")
    public String myOrdersPage(@AuthenticationPrincipal UserDetails ud, Model model) {

        if (ud == null) return "redirect:/login";

        User user = userService.findByEmail(ud.getUsername()).orElseThrow();
        List<Order> orders = orderService.listUserOrders(user);

        model.addAttribute("orders", orders);
        return "orders";
    }

    /* ===================== CANCEL ORDER ===================== */

    @GetMapping("/order/cancel/{id}")
    public String cancelOrder(@PathVariable Long id, Principal principal) {

        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderService.getById(id);

        if (order == null) {
            return "redirect:/orders?error=notfound";
        }

        if (!order.getUser().getId().equals(user.getId())) {
            return "redirect:/orders?error=unauthorized";
        }

        // cancel ONLY if not paid or delivered
        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CANCELLED);
            orderService.save(order);
        }

        return "redirect:/orders?cancelled=true";
    }

    /* ===================== ADMIN PRODUCT EDIT PAGE ===================== */

    @GetMapping("/admin/edit-product/{id}")
    public String editProductPage(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.get(id).orElse(null));
        return "edit-product";
    }
}
