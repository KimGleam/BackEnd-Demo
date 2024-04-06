package com.shop.doubleu.order.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * com.shop.doubleu.order.response.OrderList
 * <p>
 * OrderList
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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderList {

	@JsonProperty
	@Schema(description = "상품명", example = "딥디크(플레르드뽀)")
	String productName;

	@JsonProperty
	@Schema(description = "주문 수량", example = "3")
	String orderCount;

	@JsonProperty
	@Schema(description = "주문 가격", example = "600000")
	String orderPrice;

}
