package com.example.demo.global.exception;

import java.time.LocalDateTime;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.common.model.ErrorMessage;
import com.example.demo.common.model.FailResponse;
import com.example.demo.global.constant.Const;
import com.example.demo.global.exception.code.CustomErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	//@Value("${spring.profiles.active}")
	private String activeProfile;

	@ExceptionHandler(AccessDeniedException.class)
	public FailResponse accessDeninedExceptionHandler(AccessDeniedException e) {
		log.error(ExceptionUtils.getMessage(e));
		return new FailResponse(Const.ResponseCode.UNAUTHORIZED.getCode(), e.getMessage());
	}

	// 사용자 validation 에 대한 예외처리 : Pathvariable 대응
	@ExceptionHandler(ConstraintViolationException.class)
	public FailResponse constraintViolationExceptionHandler(ConstraintViolationException e) {
		log.error(ExceptionUtils.getMessage(e));
		return new FailResponse(Const.ResponseCode.INPUT_CHECK_ERROR.getCode(), e.getMessage());
	}

	// 사용자 validation 에 대한 예외처리 : Dto 대응
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public FailResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
		log.error(ExceptionUtils.getMessage(e));
		// 여러개의 Valid 오류가 올 수 있지만 하나씩 해결해야 하기 때문에 첫번째 항목만 Return
		String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		return new FailResponse(Const.ResponseCode.INPUT_CHECK_ERROR.getCode(), message);
	}

	@ExceptionHandler(BindException.class)
	public FailResponse bindExceptionHandler(BindException e) {
		log.error(ExceptionUtils.getMessage(e));
		// 여러개의 Valid 오류가 올 수 있지만 하나씩 해결해야 하기 때문에 첫번째 항목만 Return
		String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		return new FailResponse(Const.ResponseCode.INPUT_CHECK_ERROR.getCode(), message);
	}

	@ExceptionHandler(Exception.class)
	public FailResponse exceptionHandler(Exception e) {
		log.error(ExceptionUtils.getStackTrace(e));
		return new FailResponse(Const.ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
			Const.ResponseCode.INTERNAL_SERVER_ERROR.getMessage());
	}

	/*
	 * ================================================================= 커스텀 Exception =================================================================
	 */
	@ExceptionHandler(InvalidInputException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorMessage handleException(final InvalidInputException e, WebRequest request) {
		log.error("[handleException] Error : ", e);

		final CustomErrorCode customErrorCode = e.getCustomErrorCode();
		return handleCustomException(customErrorCode, request);
	}

	private ErrorMessage handleCustomException(final CustomErrorCode ce, WebRequest request) {
		return ErrorMessage.builder()
			.statusCode(ce.getCode())
			.message(ce.getMessage())
			.description(request.getDescription(false))
			.timestamp(LocalDateTime.now())
			.build();
	}
}
