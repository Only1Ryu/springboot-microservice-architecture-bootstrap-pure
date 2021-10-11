package com.app.eventrade.auth.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "resource_id";
    
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID).stateless(false);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		  http
		  	.anonymous().disable().authorizeRequests()
	        .antMatchers("/user/**").hasRole("USER")
	        .and().authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
	        .and().authorizeRequests().antMatchers("/oauth/**","/api-docs/**").permitAll();
	}
}