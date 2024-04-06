package com.shop.global.support;

import com.shop.global.constant.Const;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * com.shop.global.support.SuccessResponse
 * <p>
 * SuccessResponse
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
@Getter
@Setter
@Builder
public class SuccessResponse {

	@Schema(description = "결과코드", type = "String")
	private int resultCode;
	@Schema(description = "결과메세지", type = "String")
	private String resultMessage;
	@Schema(description = "결과값", type = "Object")
	private Object result;

	public SuccessResponse() {
		this.resultCode = Const.ResponseCode.SUCCESS.getCode();
		this.resultMessage = Const.ResponseCode.SUCCESS.getMessage();
	}

	public SuccessResponse(Object result) {
		this.resultCode = Const.ResponseCode.SUCCESS.getCode();
		this.resultMessage = Const.ResponseCode.SUCCESS.getMessage();
		this.result = result;
	}

	public SuccessResponse(int resultCode, String resultMessage, Object result) {
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
		this.result = result;
	}
}
