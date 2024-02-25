package com.example.demo.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FailResponse {
	@Schema(description = "결과코드", type = "String")
	private String resultCode;
	@Schema(description = "결과메세지", type = "String")
	private String resultMessage;
}
