package com.shop.doubleu.order.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "ORDER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID", nullable = false, unique = true)
    private Long id;

    @Column(name = "MEMBER_ID")
    private String memberId;

    @Column(name = "ORDER_REQUEST_DATE")
    private LocalDateTime orderRequestDate;

    @Builder
    public Order(Long id, String memberId, LocalDateTime orderRequestDate) {
        this.id = id;
        this.memberId = memberId;
        this.orderRequestDate = orderRequestDate;
    }
}
