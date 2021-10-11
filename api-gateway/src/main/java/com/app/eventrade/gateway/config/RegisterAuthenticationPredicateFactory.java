package com.app.eventrade.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.app.eventrade.gateway.predicates.AuthenticationCustomRoutePredicateFactory;

@Configuration
public class RegisterAuthenticationPredicateFactory {
	@Bean
	public AuthenticationCustomRoutePredicateFactory authenticationCustom() {
		return new AuthenticationCustomRoutePredicateFactory();
	}
}