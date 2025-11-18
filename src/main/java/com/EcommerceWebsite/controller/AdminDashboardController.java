package com.EcommerceWebsite.controller;

import com.EcommerceWebsite.model.Product;
import com.EcommerceWebsite.model.Order;
import com.EcommerceWebsite.model.OrderStatus;
import com.EcommerceWebsite.service.ProductService;
import com.EcommerceWebsite.service.OrderService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminDashboardController {

    private final ProductService productService;
    private final OrderService orderService;

    // Admin dashboard
    @GetMapping("/dashboard")
public String dashboard(Model model) {

    List<Product> products = productService.listAllActive();
    List<Order> orders = orderService.listAll();

    long pending = orders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count();
    long completed = orders.stream().filter(o -> o.getStatus() == OrderStatus.DELIVERED).count();

    model.addAttribute("totalProducts", products.size());
    model.addAttribute("products", products);
    model.addAttribute("orders", orders);
    model.addAttribute("pendingCount", pending);
    model.addAttribute("completedCount", completed);

    return "admin-dashboard";
}


    //  Add Product Page
    @GetMapping("/product/add")
    public String addProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "product-add";
    }

    // Save new product
    @PostMapping("/product/add")
    public String saveProduct(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/admin/dashboard?added=true";
    }

    // Edit product page
    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.get(id).orElse(null));
        return "product-edit";
    }

    @PostMapping("/product/edit/{id}")
    public String updateProduct(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/admin/dashboard?updated=true";
    }

    // Delete product
    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/admin/dashboard?deleted=true";
    }

    // Accept order
    @PostMapping("/orders/accept/{id}")
    public String acceptOrder(@PathVariable Long id) {
        orderService.acceptOrder(id);
        return "redirect:/admin/dashboard?accepted=true";
    }

    // Deliver order
    @PostMapping("/orders/deliver/{id}")
    public String deliverOrder(@PathVariable Long id) {
        orderService.deliverOrder(id);
        return "redirect:/admin/dashboard?delivered=true";
    }
    @GetMapping("/orders/view/{id}")
public String viewOrder(@PathVariable Long id, Model model) {

    Order order = orderService.getById(id);

    if (order == null) {
        return "redirect:/admin/dashboard?error=notfound";
    }

    model.addAttribute("order", order);
    model.addAttribute("items", order.getItems());

    return "admin-order-view"; // Must match your HTML file
}

}
