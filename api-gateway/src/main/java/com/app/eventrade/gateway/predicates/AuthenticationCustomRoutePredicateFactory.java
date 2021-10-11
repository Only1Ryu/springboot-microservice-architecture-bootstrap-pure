package com.app.eventrade.gateway.predicates;

import java.util.function.Predicate;

import javax.validation.constraints.NotEmpty;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AuthenticationCustomRoutePredicateFactory
		extends AbstractRoutePredicateFactory<AuthenticationCustomRoutePredicateFactory.Config> {

	public AuthenticationCustomRoutePredicateFactory() {
		super(AuthenticationCustomRoutePredicateFactory.Config.class);
	}

	@Autowired
	RestTemplate restTemplate;
	
	@Value("${check.token.url}")
	private String CHECK_TOKEN_URL;
	
	@Override
	public Predicate<ServerWebExchange> apply(Config config) {
		return (ServerWebExchange serverWebExchange) -> 
				{
					ServerHttpRequest request = serverWebExchange.getRequest();
					if (request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) 
					{
						String authorizationHeaderValue = request.getHeaders().get("Authorization").get(0);
						String token = authorizationHeaderValue.substring(7, authorizationHeaderValue.length());
						ResponseEntity<Object> responseEntity = restTemplate.getForEntity(CHECK_TOKEN_URL+token, Object.class);
						Gson gson = new GsonBuilder().setPrettyPrinting().create();
						String json = gson.toJson(responseEntity.getBody());
						JSONObject responseObject = new JSONObject(json);
						if(responseObject.has("active")) 
						{
							return true;
						}
					}
					return false;
				};
	}	

	@Validated
	public static class Config {
		boolean isAuthentication = true;
		@NotEmpty
		String userIdCookie = "userId";
		// ...constructors and mutators omitted
	}
}