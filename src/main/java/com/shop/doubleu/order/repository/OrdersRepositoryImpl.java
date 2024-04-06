package com.shop.doubleu.order.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.doubleu.order.entity.Orders;
import com.shop.doubleu.order.entity.QOrders;

import lombok.RequiredArgsConstructor;

/**
 * com.shop.doubleu.order.repository.OrdersRepositoryImpl
 * <p>
 * OrdersRepositoryImpl
 *
 * @author 김태욱
 * @version 1.0
 * @since 2024/04/06
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *     수정일        수정자           수정내용
 *  ----------    --------        ---------------------------
 *  2024/04/06    김태욱            최초 생성
 * </pre>
 */

@Repository
@RequiredArgsConstructor
public class OrdersRepositoryImpl implements OrdersRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	QOrders qOrders = QOrders.orders;

	@Override
	public List<Orders> getOrderList(Long memberId) {
		return queryFactory
			.selectFrom(qOrders)
			.where(qOrders.member.id.eq(memberId))
			.fetch();
	}

}
