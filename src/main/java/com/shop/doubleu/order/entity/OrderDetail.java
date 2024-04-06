package com.shop.doubleu.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "ORDER_DETAIL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_DETAIL_ID", nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name="ORDER_ID")
    private Orders orders;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_PRICE")
    private int productPrice;

    @Column(name = "ORDER_COUNT")
    private int orderCount;

    @Builder
    public OrderDetail(Long id, Orders orders, String productName, int productPrice, int orderCount) {
        this.id = id;
        this.orders = orders;
        this.productName = productName;
        this.productPrice = productPrice;
        this.orderCount = orderCount;
    }
}
