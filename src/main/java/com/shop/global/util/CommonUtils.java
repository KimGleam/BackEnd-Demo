package com.shop.global.util;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class CommonUtils {

	private static MessageSource messageSource;
	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	// 페이징처리를 위한 Offset 정보 조회
	public static int getOffset(int pageNumber, int recordSize) {
		return (pageNumber - 1) * recordSize;
	}

	// 고유아이디 생성
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid.replace("-", "");
	}

	// // 다국어 요청정보 확인
	public static String getLocalLanguage() {
		String locale = ((ServletRequestAttributes)Objects.requireNonNull(
			RequestContextHolder.getRequestAttributes())).getRequest().getHeader("Accept-Language");
		return locale == null ? "ko" : locale;
	}

	public static String getValidMessage(String code) {
		Locale locale = null;

		switch (getLocalLanguage()) {
			case "ko":
				locale = Locale.KOREAN;
				break;

			case "en":
				locale = Locale.ENGLISH;
				break;

			case "ja":
				locale = Locale.JAPANESE;
				break;

			case "zh_CN":
				locale = Locale.SIMPLIFIED_CHINESE;
				break;

			case "zh_TW":
				locale = Locale.TRADITIONAL_CHINESE;
				break;
		}

		return messageSource.getMessage(code, null, locale);
	}

	public static Double getDiscountRate(Double normalPrice, Double sellPrice) {
		//        ROUND(((IFNULL(TGGD.GDS_NML_PRC,0)-IFNULL(TGGD.SLL_NTSL_PRC,0))/(IFNULL(TGGD.GDS_NML_PRC,0)))*100) AS DSCNT_RT,
		return (normalPrice - sellPrice) * 0.01;
	}

}
