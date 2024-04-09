package com.shop.doubleu.product.entity;

import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "PRODUCT_OPTION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_DETAIL_ID", nullable = false, unique = true)
    private Long id;

    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;

    @Column(name = "PRODUCT_SUBNAME", nullable = false)
    private String productSubName;

    @Column(name = "PRODUCT_REGULAR_PRICE")
    @Description("상품 정가")
    private int regularPrice;

    @Column(name = "PRODUCT_DISCOUNT_PRICE")
    @Description("상품 할인 가격")
    private int discountPrice;

    @Column(name = "PRODUCT_DISCOUNT_RATE")
    @Description("할인율")
    private int discountRate;

    @Builder
    public ProductOption (Long id, Long productId, String productSubName, int regularPrice, int discountPrice, int discountRate) {
        this.id = id;
        this.productId = productId;
        this.productSubName = productSubName;
        this.regularPrice = regularPrice;
        this.discountPrice = discountPrice;
        this.discountRate = discountRate;
    }

}
