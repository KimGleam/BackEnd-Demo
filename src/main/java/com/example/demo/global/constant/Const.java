package com.example.demo.global.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Const {
	@Getter
	public enum ResponseCode {
		SUCCESS("200", "성공했습니다."),
		UNAUTHORIZED("401", "인증된 사용자가 아닙니다."),
		FORBIDDEN_ERROR("403", "인가된 사용자가 아닙니다."),
		INTERNAL_SERVER_ERROR("500", "내부 오류가 발생했습니다."),
		DELETE_ERROR("700", "삭제 중 내부 오류가 발생했습니다."),
		SAVE_ERROR("800", "저장시 내부 오류가 발생했습니다."),
		INPUT_CHECK_ERROR("900", "입력 값 무결성 오류 입니다."),
		CUSTOM_VALID_ERROR("950", "입력 값 무결성 오류 입니다.");

		private final String code;
		private final String message;

		ResponseCode(String code, String message) {
			this.code = code;
			this.message = message;
		}
	}
}
