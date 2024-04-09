package com.shop.doubleu.product.dummy.crawling;

import lombok.Data;

@Data
public class ProductVO {
    // PRODUCT
    private Long productId;
    private String productName;
    private String productSubName;
    private String categoryCode;
    private String productDeliveryInfo;
    private int regularPrice;
    private int discountPrice;
    private int discountRate;
    private String productImage;
    private String productSeller;
    private String productPackageType;
    private String productWeight;
    private String productAllergyInfo;
    private String productNotification;
    private String productExpirationDate;
    private String productDetail;
    private String salesUnit;
}
