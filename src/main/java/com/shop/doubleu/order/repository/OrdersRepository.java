package com.shop.doubleu.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.doubleu.order.entity.Orders;

/**
 * com.shop.doubleu.order.repository.OrdersRepository
 * <p>
 * OrdersRepository
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
public interface OrdersRepository extends JpaRepository<Orders, Long>, OrdersRepositoryCustom {
}
