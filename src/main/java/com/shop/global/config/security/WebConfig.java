package com.shop.global.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:3000")  // 허용할 origin을 지정
			.allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드를 지정
			.allowCredentials(true);  // 인증정보를 함께 전송할 것인지 여부
	}
}
