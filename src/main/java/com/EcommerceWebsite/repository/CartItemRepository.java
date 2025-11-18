package com.EcommerceWebsite.repository;

import com.EcommerceWebsite.model.CartItem;
import com.EcommerceWebsite.model.Product;
import com.EcommerceWebsite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    void deleteByUser(User user);
    CartItem findByUserAndProduct(User user, Product product);

}
