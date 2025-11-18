package com.EcommerceWebsite.repository;

import com.EcommerceWebsite.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String q);
    List<Product> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String name, String category);

    Optional<Product> findById(Long id);

    List<Product> findByActiveTrue();

    @Query("SELECT p FROM Product p WHERE p.active = true")
List<Product> listAllActive();






}
