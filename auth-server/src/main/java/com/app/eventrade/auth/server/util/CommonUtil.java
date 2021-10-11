package com.app.eventrade.auth.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;

import com.app.eventrade.auth.server.exception.CustomException;

public class CommonUtil {
	public static boolean isEmail(String email) throws CustomException {
		if (email.contains("@")) {
			return true;
		}
		throw new CustomException(200, HttpStatus.BAD_REQUEST.value(), "Email is not Valid");
	}

	public static boolean isMobile(String mobile) throws CustomException {
		Pattern mobilePattern = Pattern.compile("^\\d{10}$");
		Matcher numberSize = mobilePattern.matcher(mobile);
		if (numberSize.matches()) {
			return true;
		}
		throw new CustomException(200, HttpStatus.BAD_REQUEST.value(), "Number is not Valid");
	}
}
