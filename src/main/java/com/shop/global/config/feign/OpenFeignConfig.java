package com.shop.global.config.feign;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;

@Configuration
class OpenFeignConfig {

	// retryer
	@Bean
	Retryer.Default retryer() {
		return new Retryer.Default();
		// 0.1초의 간격으로 시작해 최대 3초의 간격으로 점점 증가하며, 최대5번 재시도
		//return new Retryer.Default(100L, TimeUnit.SECONDS.toMillis(3L), 5);
	}

	// loggerLevel
	/*
		#NONE : 로깅 X (DEFAULT)
		#BASIC : Request Method와 URL 그리고 Reponse 상태 코드와 실행 시간을 로깅
		#HEADERS : Request, Response Header 정보와 함께 BASIC 정보를 로깅
		#FULL : Request와 Response의 Header, Body 그리고 메타데이터를 로깅
	*/
	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}

	// connectTimeout
	@Bean
	public RestTemplate connectTimeout(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder
			.setConnectTimeout(Duration.ofSeconds(5))
			.setReadTimeout(Duration.ofSeconds(1))
			.build();
	}

	// requestInterceptor
	@Bean
	public RequestInterceptor requestInterceptor() {
		return template -> {
			template.header("Authorization", "Bearer ");
			template.header("Content-Type", "application/json");
		};
	}

	// ErrorDecode
	@Bean
	public ErrorDecoder decoder() {
		return new FeignErrorDecoder();
	}

}