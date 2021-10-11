package com.app.eventrade.roleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
@EnableFeignClients
@EnableJpaRepositories
public class RoleServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoleServiceApplication.class, args);
	}

	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.OAS_30)
				.apiInfo(new ApiInfoBuilder().title("Role Service API")
						.description("Role Service API Version 1").version("0.0.1-SNAPSHOT")
						.license("Only1Ryu").licenseUrl("https://github.com/Only1Ryu")
						.contact(new Contact("Rakesh Sahani", "", "rakeshsahani@live.in")).build())
				.tags(new Tag("Role Service API", "Endpoints for Role operations")).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).build();
	}

}