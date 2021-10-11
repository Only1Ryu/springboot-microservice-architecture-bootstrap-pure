package com.app.eventrade.auth.server.util;

import java.util.HashMap;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AppConstants {
	public static final String USER = "/user";
	public static final String NO_UUID = "Required UUID is not passed";
	public static final String REQUEST_BODY_MISSING = "Requestbody is missing";
	public static final String USER_ALREADY_EXISTS = "User is already exists";
	public static final String USER_CREATED = "User is Created";
	public static final String USER_UPDATED = "User Name is Updated";
	public static final String USER_DELETED = "User is  Deleted";
	public static final String NO_USER = "No User is Found";
	public static final String PENDING = "PENDING";
	public static final String ACTIVE = "ACTIVE";
	public static final String DEACTIVE = "DEACTIVE";
	public static final String FILE_UPLOADED = "File is Uploaded";	
	public static final String FILE_UPDATED = "File is Updated";		
	public static final String FILE_DELETED = "File is Deleted";
	public static final String FILE_SIZE = "File size is too large";
	public static final String NO_FILE = "No File is Found";
	public static final String INVALID_PAYLOAD = "Invalid Request";
	public static final String INVALID_TOKEN = "Invalid Token";
	public static final String TOKEN_ERROR = "Token Error";
	public static final String TOKEN_GENERATE = "Token Generation Failure";
	public static final String SENT_OTP = "OTP has been sent";
	public static final String VERIFIED_OTP = "OTP is verified";
	public static final String NOT_VERIFIED_OTP = "OTP is not verified";
	
    public static final HashMap<Integer, String> RESPONSE_MAP = new HashMap<>();

    static {
    	RESPONSE_MAP.put(202,USER_CREATED);
    	RESPONSE_MAP.put(204,USER_ALREADY_EXISTS);
    	RESPONSE_MAP.put(206,SENT_OTP);
    	RESPONSE_MAP.put(208,TOKEN_GENERATE);
    	RESPONSE_MAP.put(210,VERIFIED_OTP);
    	RESPONSE_MAP.put(400,REQUEST_BODY_MISSING);
    	RESPONSE_MAP.put(402,NOT_VERIFIED_OTP);
    	RESPONSE_MAP.put(406,INVALID_PAYLOAD);
    	RESPONSE_MAP.put(502,INVALID_TOKEN);
    	RESPONSE_MAP.put(504,TOKEN_ERROR);    	
    }
	
	
}
