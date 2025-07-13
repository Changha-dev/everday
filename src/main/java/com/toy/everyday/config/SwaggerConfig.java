package com.toy.everyday.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("Everyday 프로젝트 API")
				.description("게시판, 댓글, 추천 등 토이 프로젝트의 API 명세서입니다.")
				.version("1.0.0"));
	}

}