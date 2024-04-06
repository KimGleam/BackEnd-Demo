package com.shop.doubleu.order.entity;

import java.time.LocalDateTime;

import com.shop.doubleu.member.entity.Member;

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

@Table(name = "ORDERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID", nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    @Column(name = "ORDER_REQUEST_DATE")
    private LocalDateTime orderRequestDate;

    @Builder
    public Orders(Long id, Member member, LocalDateTime orderRequestDate) {
        this.id = id;
        this.member = member;
        this.orderRequestDate = orderRequestDate;
    }
}
