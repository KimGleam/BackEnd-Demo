package com.shop.doubleu.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shop.doubleu.order.entity.Orders;
import com.shop.doubleu.order.repository.OrdersRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;

    public List<Orders> getOrderList(Long memberId) {
        return ordersRepository.getOrderList(memberId);
    }

}
