package com.app.eventrade.auth.server.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import com.app.eventrade.auth.server.DTO.Response;
import com.app.eventrade.auth.server.exception.CustomOAuthException;

@Component("exceptionTranslator")
public class Oauth2ExceptionTranslatorConfig implements WebResponseExceptionTranslator<Response> {
	private static LocalDateTime timeStamp = LocalDateTime.now();

	private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

	@Override
	public ResponseEntity<Response> translate(Exception e) throws Exception {

		// Try to extract a SpringSecurityException from the stacktrace
		Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);

		// Exception stack get OAuth2Exception exception
		Exception exception = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class,causeChain);

		// OAuth2Exception in exception stack
		if (exception != null) {
			return handleOAuth2Exception((OAuth2Exception) exception);
		}

		exception = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class,causeChain);
		if (exception != null) {
			return handleOAuth2Exception(new CustomOAuthException(HttpStatus.OK.value(),HttpStatus.UNAUTHORIZED.value(),exception.getMessage()));
		}

		exception = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class,causeChain);
		if (exception instanceof AccessDeniedException) {
			return handleOAuth2Exception(new CustomOAuthException(HttpStatus.OK.value(),HttpStatus.UNAUTHORIZED.value(),exception.getMessage()));
		}
		
		exception = (InvalidGrantException) throwableAnalyzer.getFirstThrowableOfType(InvalidGrantException.class,causeChain);
		if (exception instanceof InvalidGrantException) {
			return handleOAuth2Exception(new CustomOAuthException(HttpStatus.OK.value(),HttpStatus.UNAUTHORIZED.value(),exception.getMessage()));
		}

		exception = (InvalidTokenException) throwableAnalyzer.getFirstThrowableOfType(InvalidTokenException.class,causeChain);
		if (exception instanceof InvalidTokenException) {
			return handleOAuth2Exception(new CustomOAuthException(HttpStatus.OK.value(),HttpStatus.UNAUTHORIZED.value(),exception.getMessage()));
		}
		
		exception = (InternalAuthenticationServiceException) throwableAnalyzer.getFirstThrowableOfType(InternalAuthenticationServiceException.class,causeChain);
		if (exception instanceof InternalAuthenticationServiceException) {
			return handleOAuth2Exception(new CustomOAuthException(HttpStatus.OK.value(),HttpStatus.UNAUTHORIZED.value(),exception.getMessage()));
		}
		exception = (HttpRequestMethodNotSupportedException) throwableAnalyzer.getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class, causeChain);
		if (exception instanceof HttpRequestMethodNotSupportedException) {
			return handleOAuth2Exception(new CustomOAuthException(401,HttpStatus.UNAUTHORIZED.value(),exception.getMessage()));
		}

		exception = (UsernameNotFoundException) throwableAnalyzer.getFirstThrowableOfType(UsernameNotFoundException.class, causeChain);
		if (exception instanceof UsernameNotFoundException) {
			return handleOAuth2Exception(new CustomOAuthException(HttpStatus.OK.value(),HttpStatus.NOT_FOUND.value(), exception.getMessage()));
		}

		// Server internal error if the above exception is not included
		return handleOAuth2Exception(new CustomOAuthException(HttpStatus.OK.value(),HttpStatus.INTERNAL_SERVER_ERROR.value(),exception.getMessage()));
	}

	private ResponseEntity<Response> handleOAuth2Exception(OAuth2Exception e) throws IOException {

		int status = e.getHttpErrorCode();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Cache-Control", "no-store");
		headers.set("Pragma", "no-cache");
		if (status == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
			headers.set("WWW-Authenticate", String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
		}
		Response response = new Response();
		response.setResponse(e.getMessage());
		response.setTimeStamp(timeStamp);
		response.setStatus(HttpStatus.OK.value());
		response.setCode(e.getHttpErrorCode());
		return new ResponseEntity<Response>(response, headers, HttpStatus.OK);
	}
}