package com.shop.doubleu.order.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.doubleu.order.entity.Orders;
import com.shop.doubleu.order.response.OrderList;
import com.shop.doubleu.order.service.OrdersService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * com.shop.doubleu.order.controller.OrdersController
 * <p>
 * OrdersController
 *
 * @author 김태욱
 * @version 1.0
 * @since 2024/02/24
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *     수정일        수정자           수정내용
 *  ----------    --------        ---------------------------
 *  2024/04/06    김태욱            최초 생성
 * </pre>
 */

@OpenAPIDefinition(
	info = @Info(
		title = "ORDER API",
		version = "1.0",
		description = "ORDER API"
	)
)
@Tag(name = "ORDER API", description = "ORDER API Controller")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class OrdersController {

	private final OrdersService ordersService;

	/**
	 *
	 * @return
	 */
	@Operation(summary = "주문 목록", description = "주문 목록 조회")
	@GetMapping("/list")
	public List<OrderList> getOrderList(@RequestHeader Long memberId){
		return ordersService.getOrderList(memberId);
	}

}
