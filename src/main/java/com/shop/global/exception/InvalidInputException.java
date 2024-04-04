package com.shop.global.exception;

import java.io.Serializable;

import com.shop.global.exception.code.CustomErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidInputException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 1L;

	private final transient CustomErrorCode customErrorCode;

}
