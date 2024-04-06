package com.shop.doubleu.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.doubleu.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findById(String productId);
}

