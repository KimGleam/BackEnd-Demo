package com.shop.doubleu.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "CATEGORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Category {

    @Id
    @Column(name = "CATEGORY_CODE", nullable = false, unique = true)
    private String categoryCode;

    @Column(name = "CATEGORY_NAME", nullable = false)
    private String categoryName;

    @Column(name = "pcode")
    private String pCode;

    @Builder
    public Category(String categoryCode, String categoryName, String pCode) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.pCode = pCode;
    }
}
