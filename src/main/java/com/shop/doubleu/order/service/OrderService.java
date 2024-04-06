package com.shop.doubleu.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shop.doubleu.order.entity.Order;
import com.shop.doubleu.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Order> getOrderList(String memberId) {
        return orderRepository.findByMemberId(memberId);
    }

}
