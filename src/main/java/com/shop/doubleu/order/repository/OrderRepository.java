package com.shop.doubleu.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.doubleu.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMemberId(String memberId);
}

