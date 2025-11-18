package com.EcommerceWebsite.service;

import com.EcommerceWebsite.model.Product;
import com.EcommerceWebsite.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> listAllActive() {
    return productRepository.findByActiveTrue();
}


    @Override
    public Optional<Product> get(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
       Product product = productRepository.findById(id).orElse(null);
    if(product != null){
        product.setActive(false);  // Soft delete
        productRepository.save(product);
    }
}

    @Override
    public long count() {
        return productRepository.count();
    }

    @Override
    public List<Product> searchByNameOrCategory(String keyword) {
        return productRepository
                .findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(keyword, keyword);
    }
}


