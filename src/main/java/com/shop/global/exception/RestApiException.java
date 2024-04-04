package com.shop.global.exception;

import com.shop.global.exception.code.CustomErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException implements Serializable {

  private static final long serialVersionUID = 1L;

  private final transient CustomErrorCode customErrorCode;

}
