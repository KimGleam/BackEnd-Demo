package com.shop.doubleu.product.service;


import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.doubleu.product.dto.ProductDTO;
import com.shop.doubleu.product.entity.Product;
import com.shop.doubleu.product.entity.ProductDetail;
import com.shop.doubleu.product.entity.QProduct;
import com.shop.doubleu.product.entity.QProductDetail;
import com.shop.doubleu.product.repository.ProductDetailRepository;
import com.shop.doubleu.product.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public List<Product> getProductList(String productId) {
        return productRepository.findById(productId);
    }

    public Optional<Product> getProductInfo(long productId) {
        return productRepository.findById(productId);
    }

    public Optional<ProductDetail> getProductDetailInfo(long productDetailId) {
        return productDetailRepository.findById(productDetailId);
    }

    public List<ProductDTO> getProductWithDetailById(Long productId) {
        QProduct product = QProduct.product;
        QProductDetail productDetail = QProductDetail.productDetail;

        return queryFactory
                .select(Projections.bean(ProductDTO.class,
                        product.id,
                        product.productName,
                        product.productSubName,
                        product.categoryCode,
                        product.productImage,
                        product.regularPrice,
                        product.discountPrice,
                        product.discountRate,
                        productDetail.seller,
                        productDetail.packageType,
                        productDetail.weight,
                        productDetail.salesUnit,
                        productDetail.allergyInfo,
                        productDetail.deliveryInfo,
                        productDetail.livestockHistoryInfo,
                        productDetail.notification,
                        productDetail.carefulInfo,
                        productDetail.expirationDate
                        )
                )
                .from(product)
                .join(product.productDetail, productDetail)
                .where(product.id.eq(productId))
                .fetch();
    }

}
