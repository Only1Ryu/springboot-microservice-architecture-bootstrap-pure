package com.app.eventrade.auth.server.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

public class CustomOAuthException extends OAuth2Exception {

	private int status;
	private int code;
	private String message;
	private static final long serialVersionUID = 4134530760765747944L;

	public CustomOAuthException(String message) {
		super(message);
	}

	public CustomOAuthException(int status,int code, String message) {
		super(message);
		this.status = status;
		this.code = code;
		this.message=message;
	}

	public CustomOAuthException(String message, Throwable t) {
		super(message, t);
	}

	public int getStatus() {
		return this.status;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getMessage() {
		return this.message;
	}
}
