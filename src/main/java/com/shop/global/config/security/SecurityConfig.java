package com.shop.global.config.security;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.shop.doubleu.common.model.ErrorMessage;
import com.shop.doubleu.common.repository.RefreshTokenRepository;
import com.shop.doubleu.member.service.MemberService;
import com.shop.global.config.jwt.TokenProvider;
import com.shop.global.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.shop.global.config.oauth.OAuth2SuccessHandler;
import com.shop.global.config.oauth.OAuth2UserCustomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

	private final OAuth2UserCustomService oAuth2UserCustomService;
	private final TokenProvider tokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;
	private final MemberService memberService;

	private static final String[] whiteList = new String[] {
		"/actuator/**",
		"/demo/**",
		// Swagger 관련
		"/v3/api-docs",
		"/configuration/ui",
		"/swagger-resources/**",
		"/configuration/security",
		"/swagger-ui.html",
		"/swagger-ui/**",
		"/webjars/**",
		"/v3/**",
		// API 인증
		"/api/token",
		"/member/**",
		"/order/**",
		"/product/**",
		"/goods/**"
	};

	@Bean
	public WebSecurityCustomizer configure() {
		return (web) -> web.ignoring()
			.requestMatchers(toH2Console())
			.requestMatchers("/img/**", "/css/**", "/js/**");
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.cors().and()	// CORS 설정
			.httpBasic().disable()
			.formLogin().disable()	// 기본 로그인 Disable
			.logout().disable();

		http.oauth2Login()
			.loginPage("/login")
			.authorizationEndpoint()
			.authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())	// OAuth 로그인 인증
			.and()
			.successHandler(oAuth2SuccessHandler())		// OAuth 로그인 인증 실행 Handling
			.userInfoEndpoint()
			.userService(oAuth2UserCustomService);		// OAuth 로그인 인증 성공 후 Member 정보 처리

		http.logout()
			.logoutSuccessUrl("/login");

		http.authorizeRequests()
			.requestMatchers(whiteList).permitAll()	// 해당 URI 인증 필요 X
			.anyRequest().authenticated();	// 다른 URI 인증 필요

		http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);	// Rest API 요청시 인증 실행

		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()		// Session 사용 X
			.exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint()).and();	// Rest API 인증 실패 시 Handling

		http.exceptionHandling()
			.defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
				new AntPathRequestMatcher("/api/**"));

		return http.build();
	}

	@Bean
	public OAuth2SuccessHandler oAuth2SuccessHandler() {
		return new OAuth2SuccessHandler(tokenProvider,
			refreshTokenRepository,
			oAuth2AuthorizationRequestBasedOnCookieRepository(),
			memberService
		);
	}

	@Bean
	public TokenAuthenticationFilter tokenAuthenticationFilter() {
		return new TokenAuthenticationFilter(tokenProvider);
	}

	@Bean
	public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
		return new OAuth2AuthorizationRequestBasedOnCookieRepository();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//Spring Security 인증 오류 발생시 리턴 메세지 설정
	private AuthenticationEntryPoint authenticationEntryPoint() {
		return (request, response, authException) -> {
			ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

			log.error("Unauthorized error: {}", authException.getMessage());
			log.error("Requested path    : {}", request.getServletPath());

			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());

			ErrorMessage errorMessage = ErrorMessage.builder()
				.statusCode(HttpStatus.UNAUTHORIZED.value())
				.message(authException.getMessage())
				.description("uri=" + request.getContextPath() + request.getServletPath())
				.timestamp(LocalDateTime.now())
				.build();

			mapper.writeValue(response.getOutputStream(), errorMessage);
		};
	}

	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		// OAuth2 클라이언트 등록 정보를 설정
		List<ClientRegistration> registrations = new ArrayList<>();
		registrations.add(CommonOAuth2Provider.GOOGLE.getBuilder("google")
			.clientId("595510502221-38jib7lkar5digis4oath7kr6dlglp9v.apps.googleusercontent.com")
			.clientSecret("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")
			.scope("email", "profile")
			.build());

		return new InMemoryClientRegistrationRepository(registrations);
	}
}
