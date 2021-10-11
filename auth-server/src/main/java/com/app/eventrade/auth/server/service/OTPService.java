package com.app.eventrade.auth.server.service;

public interface OTPService {
	public int generateOTP(String key);

	public int getOTP(String key);

	public void clearOTP(String key);
}
