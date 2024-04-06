package com.shop.doubleu.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shop.doubleu.order.entity.OrderDetail;
import com.shop.doubleu.order.entity.Orders;
import com.shop.doubleu.order.repository.OrdersRepository;
import com.shop.doubleu.order.response.OrderList;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;

    public List<OrderList> getOrderList(Long memberId) {

        List<OrderList> orderList = new ArrayList<>();
        List<OrderDetail> getOrderList = ordersRepository.getOrderList(memberId);

        getOrderList.stream().forEach(order -> {
            orderList.add(
                OrderList.builder()
                    .productName(order.getProductName())
                    .orderCount(order.getOrderCount())
                    .orderPrice(order.getProductPrice())
                    .build()
            );
        });

        return orderList;
    }

}
