package com.EcommerceWebsite.service;

import com.EcommerceWebsite.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    // List<Product> listAll();

    Optional<Product> get(Long id);   // Optional version

    Product getById(Long id);         // Direct version

    Product save(Product product);

    void delete(Long id);

List<Product> listAllActive(); 


    
  long count();
    List<Product> searchByNameOrCategory(String keyword);
}

