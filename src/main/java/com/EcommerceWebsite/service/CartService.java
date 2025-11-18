package com.EcommerceWebsite.service;

import com.EcommerceWebsite.model.CartItem;
import com.EcommerceWebsite.model.Product;
import com.EcommerceWebsite.model.User;
import com.EcommerceWebsite.repository.CartItemRepository;
import com.EcommerceWebsite.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartRepo;
    private final ProductRepository productRepo;

    public List<CartItem> getCartForUser(User user) { return cartRepo.findByUser(user); }

     @Transactional
    public CartItem addToCart(User user, Long productId, int qty) {

        Product product = productRepo.findById(productId).orElseThrow();

        // Check if product already exists in cart
        CartItem existing = cartRepo.findByUserAndProduct(user, product);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + qty);
            return cartRepo.save(existing);
        }

        // Create new cart item
        CartItem item = new CartItem();
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(qty);

        return cartRepo.save(item);
    }

    @Transactional
    public void remove(Long id) { cartRepo.deleteById(id); }

    @Transactional
public void clear(User user) {
    List<CartItem> items = cartRepo.findByUser(user);
    cartRepo.deleteAll(items);
}
}
