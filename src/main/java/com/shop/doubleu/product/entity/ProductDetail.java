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

    @Column(name = "PRODUCT_DETAIL", nullable = false)
    private String detailImage;

    @Column(name = "PRODUCT_SELLER")
    private String seller;

    @Column(name = "PRODUCT_PACKAGE_TYPE")
    private String packageType;

    @Column(name = "PRODUCT_WEIGHT")
    private String weight;

    @Column(name = "PRODUCT_SALES_UNIT")
    private String salesUnit;

    @Column(name = "PRODUCT_ALLERGY_INFO")
    private String allergyInfo;

    @Column(name = "PRODUCT_DELIVERY_INFO")
    private String deliveryInfo;

    @Column(name = "PRODUCT_LIVESTOCK_HISTORY_INFO")
    private String livestockHistoryInfo;

    @Column(name = "PRODUCT_NOTIFICATION")
    private String notification;

    @Column(name = "PRODUCT_CAREFUL_INFO")
    private String carefulInfo;

    @Column(name = "PRODUCT_EXPIRATION_DATE")
    private String expirationDate;

    @OneToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @Builder
    public ProductDetail (Long id, Long productId, String seller,
                          String packageType, String weight, String allergyInfo,
                          String notification, String expirationDate, String detailImage,
                          String  salesUnit, String deliveryInfo, String livestockHistoryInfo, String carefulInfo) {
        this.id = id;
        this.productId = productId;
        this.detailImage = detailImage;
        this.seller = seller;
        this.packageType = packageType;
        this.weight = weight;
        this.salesUnit = salesUnit;
        this.allergyInfo = allergyInfo;
        this.deliveryInfo = deliveryInfo;
        this.livestockHistoryInfo = livestockHistoryInfo;
        this.notification = notification;
        this.carefulInfo = carefulInfo;
        this.expirationDate = expirationDate;
    }
}
