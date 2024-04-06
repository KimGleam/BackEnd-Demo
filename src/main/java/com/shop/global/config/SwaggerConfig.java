package com.shop.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
	
	// Swagger Security 설정
	/*@Bean
	public OpenAPI demoOpenAPI(SwaggerProperties swaggerProperties) {
		SecurityScheme securityScheme = new SecurityScheme()
				.type(HTTP)
				.in(HEADER)
				.scheme(BEARER)
				.bearerFormat("JWT")
				.name("Authorization");
		
		SecurityRequirement securityRequirement = new SecurityRequirement()
				.addList(BEARER)
				.addList("cloudfront-viewer-address")
				.addList("cloudfront-viewer-city")
				.addList("cloudfront-viewer-country")
				.addList("cloudfront-viewer-time-zone");
		
		Info info = new Info()
				.title("WISDOM-INSILICO-BACKEND OPEN API")
				.description("WISDOM-INSILICO-BACKEND OPEN API Description입니다.")
				.version("v0.0.1");
		
		Server server = new Server();
		server.setUrl(swaggerProperties.getUrl());
		server.setDescription(swaggerProperties.getDescription());
		
		return new OpenAPI()
				.components(new Components().addSecuritySchemes(BEARER, securityScheme))
				.info(info)
				.servers(Collections.singletonList(server))
				.security(Collections.singletonList(securityRequirement));
	}*/
	
	@Bean
	public OpenAPI openAPI() {
		
		Info info = new Info()
				.version("v1.0.0")
				.title("DEMO-PROJECT-BACKEND OPEN API")
				.description("DEMO-PROJECT-BACKEND Open API");
		
		return new OpenAPI()
				.info(info);
	}


	@Bean
	public GroupedOpenApi member() {
		String[] paths = {"/member/**"};
		return GroupedOpenApi.builder().group("member").pathsToMatch(paths)
			.build();
	}

	@Bean
	public GroupedOpenApi order() {
		String[] paths = {"/order/**"};
		return GroupedOpenApi.builder().group("order").pathsToMatch(paths)
			.build();
	}

	@Bean
	public GroupedOpenApi product() {
		String[] paths = {"/product/**"};
		return GroupedOpenApi.builder().group("product").pathsToMatch(paths)
			.build();
	}

}

