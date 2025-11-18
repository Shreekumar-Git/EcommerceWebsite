package com.EcommerceWebsite.controller;

import com.EcommerceWebsite.model.CartItem;
import com.EcommerceWebsite.model.User;
import com.EcommerceWebsite.service.CartService;
import com.EcommerceWebsite.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartRestController {
    private final CartService cartService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(@AuthenticationPrincipal UserDetails ud) {
        User user = userService.findByEmail(ud.getUsername()).orElseThrow();
        return ResponseEntity.ok(cartService.getCartForUser(user));
    }

    @PostMapping("/add")
public String addToCart(@AuthenticationPrincipal UserDetails ud,
                        @RequestParam Long productId,
                        @RequestParam int qty) {

    User user = userService.findByEmail(ud.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

    cartService.addToCart(user, productId, qty);

    return "redirect:/cart";
}


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) { cartService.remove(id); return ResponseEntity.ok().build(); }
}
