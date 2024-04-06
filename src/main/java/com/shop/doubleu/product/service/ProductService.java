package com.shop.doubleu.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shop.doubleu.product.entity.Product;
import com.shop.doubleu.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getProductList(String productId) {
        return productRepository.findById(productId);
    }

}
