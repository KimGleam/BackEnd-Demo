package com.shop.doubleu.product.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDTO {
    // PRODUCT
    private Long productId;
    private String productName;
    private String productSubName;
    private String categoryCode;
    private String productImage;
    private int regularPrice;
    private int discountPrice;
    private int discountRate;
    private String seller;
    private String packageType;
    private String weight;
    private String salesUnit;
    private String allergyInfo;
    private String deliveryInfo;
    private String livestockHistoryInfo;
    private String noAntibioticsCertificationNumber;
    private String stockingInfo;
    private String afterServiceInfo;
    private String sugarContent;
    private String detailImage;
    private String notification;
    private String carefulInfo;
    private String expirationDate;
}
