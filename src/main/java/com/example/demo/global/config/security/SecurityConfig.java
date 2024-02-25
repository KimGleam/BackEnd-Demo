package com.example.demo.global.config.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests((authorizeRequests) ->
                authorizeRequests
                    .requestMatchers(PathRequest.toH2Console()).permitAll()     // H2 Console 접근 허용
                    .requestMatchers("/**").permitAll()                       // /** 경로에 접근 허용
                    .anyRequest().authenticated()                               // 나머지 경로는 인증 필요
					// 추후 인증 설정 추가 필요
			);
		return http.build();
	}
	
}
