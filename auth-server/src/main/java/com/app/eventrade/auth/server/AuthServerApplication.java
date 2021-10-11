package com.app.eventrade.auth.server;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
@EnableFeignClients
@EnableJpaRepositories
@EnableAuthorizationServer
@EnableResourceServer
public class AuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.OAS_30)
				.apiInfo(new ApiInfoBuilder().title("Authorization Service API")
						.description("Only1Ryu - Authorization Server API").version("0.0.1-SNAPSHOT")
						.license("Only1Ryu").licenseUrl("https://github.com/Only1Ryu")
						.contact(new Contact("Rakesh Sahani", "", "rakeshsahani@live.in")).build()).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).build();
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}