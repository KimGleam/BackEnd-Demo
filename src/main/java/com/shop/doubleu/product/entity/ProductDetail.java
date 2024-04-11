package com.shop.doubleu.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "PRODUCT_DETAIL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_DETAIL_ID", nullable = false, unique = true)
    private Long id;

    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;

    @Column(name = "PRODUCT_DELIVERY_INFO")
    private String productDeliveryInfo;

    @Column(name = "PRODUCT_SELLER")
    private String productSeller;

    @Column(name = "PRODUCT_PACKAGE_TYPE")
    private String productPackageType;

    @Column(name = "PRODUCT_WEIGHT")
    private String productWeight;

    @Column(name = "PRODUCT_SALES_UNIT")
    private String salesUnit;

    @Column(name = "PRODUCT_ALLERGY_INFO")
    private String productAllergyInfo;

    @Column(name = "PRODUCT_NOTIFICATION")
    private String productNotification;

    @Column(name = "PRODUCT_EXPIRATION_DATE")
    private String productExpirationDate;

    @Column(name = "PRODUCT_DETAIL", nullable = false)
    private String productDetail;

    @Builder
    public ProductDetail (Long id, Long productId, String productSeller,
                          String productPackageType, String productWeight, String productAllergyInfo,
                          String productNotification, String productExpirationDate, String productDetail,
                          String  salesUnit, String productDeliveryInfo) {
        this.id = id;
        this.productId = productId;
        this.productSeller = productSeller;
        this.productPackageType = productPackageType;
        this.productWeight = productWeight;
        this.productAllergyInfo = productAllergyInfo;
        this.productNotification = productNotification;
        this.productExpirationDate = productExpirationDate;
        this.productDetail = productDetail;
        this.salesUnit = salesUnit;
        this.productDeliveryInfo = productDeliveryInfo;
    }
}
