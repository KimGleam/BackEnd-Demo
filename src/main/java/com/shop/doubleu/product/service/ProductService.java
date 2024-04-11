package com.shop.doubleu.product.service;

import java.util.List;
import java.util.Optional;

import com.shop.doubleu.product.entity.ProductDetail;
import com.shop.doubleu.product.repository.ProductDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.shop.doubleu.product.entity.Product;
import com.shop.doubleu.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    public List<Product> getProductList(String productId) {
        return productRepository.findById(productId);
    }

    public Optional<ProductDetail> getProductDetailInfo(long productDetailId) {
        return productDetailRepository.findById(productDetailId);
    }
}
