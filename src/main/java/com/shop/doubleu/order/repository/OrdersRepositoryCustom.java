package com.shop.doubleu.order.repository;

import java.util.List;

import com.shop.doubleu.order.entity.Orders;

public interface OrdersRepositoryCustom {
    List<Orders> getOrderList(Long memberId);
}

