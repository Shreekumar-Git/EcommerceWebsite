package com.EcommerceWebsite.controller;

import com.EcommerceWebsite.model.Product;
import com.EcommerceWebsite.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductRestController {
    private final ProductService productService;

    @GetMapping
    public List<Product> list() { return productService.listAllActive(); }

    @GetMapping("/{id}")
    public ResponseEntity<Product> get(@PathVariable Long id) {
        return productService.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Product create(@RequestBody Product p) { return productService.save(p); }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product p) {
        if (!productService.get(id).isPresent()) return ResponseEntity.notFound().build();
        p.setId(id);
        return ResponseEntity.ok(productService.save(p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { productService.delete(id); return ResponseEntity.ok().build(); }
}
