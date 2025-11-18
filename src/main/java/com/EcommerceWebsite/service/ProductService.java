package com.EcommerceWebsite.service;

import com.EcommerceWebsite.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {


    Optional<Product> get(Long id);   

    Product getById(Long id);         

    Product save(Product product);

    void delete(Long id);

List<Product> listAllActive(); 


    
  long count();
    List<Product> searchByNameOrCategory(String keyword);
}

