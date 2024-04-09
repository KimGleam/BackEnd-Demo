package com.shop.doubleu.product.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jdk.jfr.Description;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

@Table(name = "PRODUCT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID", nullable = false, unique = true)
    private Long id;

    @Column(name = "PRODUCT_NAME", nullable = false)
    @Description("상품명")
    private String productName;

    @Column(name = "PRODUCT_SUBNAME")
    private String productSubName;

    @Column(name = "CATEGORY_CODE", nullable = false)
    private String categoryCode;

    @Column(name = "PRODUCT_IMAGE", nullable = false)
    private String productImage;

    @Column(name = "REGISTRATION_DATE", nullable = false)
    @Description("상품 등록 일자")
    private LocalDateTime registrationDate;

    @Column(name = "UPDATE_DATE")
    @Description("상품 정보 수정 일자")
    private LocalDateTime updateDate;

    @Column(name = "PRODUCT_REGULAR_PRICE", nullable = false)
    @Description("상품 정가")
    private int regularPrice;

    @Column(name = "PRODUCT_DISCOUNT_PRICE")
    @Description("상품 할인 가격")
    private int discountPrice;

    @Column(name = "PRODUCT_DISCOUNT_RATE")
    @Description("할인율")
    private int discountRate;

    @Column(name = "PRODUCT_OPTION")
    private Boolean productOption;

    @Column(name = "PRODUCT_SALES_COUNT")
    private Long salesCount;

    @Builder
    public Product(Long id, String productName, String productSubName, String categoryCode,
                   String productImage, LocalDateTime registrationDate, LocalDateTime updateDate,
                   int regularPrice, int discountPrice, int discountRate, Boolean productOption,
                   Long salesCount) {
        this.id = id;
        this.productName = productName;
        this.productSubName = productSubName;
        this.categoryCode = categoryCode;
        this.productImage = productImage;
        this.registrationDate = registrationDate;
        this.updateDate = updateDate;
        this.regularPrice = regularPrice;
        this.discountPrice = discountPrice;
        this.discountRate = discountRate;
        this.productOption = productOption;
        this.salesCount = salesCount;
    }
}
