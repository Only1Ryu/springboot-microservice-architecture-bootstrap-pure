package com.app.eventrade.auth.server.service.Impl;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.app.eventrade.auth.server.service.OTPService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class OTPServiceImpl implements OTPService {
	private final Logger log = LoggerFactory.getLogger(OTPServiceImpl.class);

	private static final Integer OTP_EXPIRE_MINS = 5;

	private LoadingCache<String, Integer> otpCache;

	public OTPServiceImpl() {
		super();
		otpCache = CacheBuilder.newBuilder().expireAfterWrite(OTP_EXPIRE_MINS, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Integer>() {
					public Integer load(String key) {
						return 0;
					}
				});
	}
	
	//Key -> UserName
	@Override
	public int generateOTP(String key) {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		otpCache.put(key, otp);
		log.info("------------------------------------------------------------"+otp);
		return otp;
	}

	@Override
	public int getOTP(String key) {
		try {
			return otpCache.get(key);
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public void clearOTP(String key) {
		otpCache.invalidate(key);
	}

}
