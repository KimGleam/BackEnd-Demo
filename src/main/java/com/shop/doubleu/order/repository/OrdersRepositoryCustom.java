package com.shop.doubleu.order.repository;

import java.util.List;

import com.shop.doubleu.order.entity.OrderDetail;

public interface OrdersRepositoryCustom {
    List<OrderDetail> getOrderList(Long memberId);
}

