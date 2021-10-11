package com.app.eventrade.auth.server.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.eventrade.auth.server.DTO.UserDTO;
import com.app.eventrade.auth.server.exception.CustomException;
import com.app.eventrade.auth.server.service.OTPService;
import com.app.eventrade.auth.server.service.UserService;
import com.app.eventrade.auth.server.util.AppConstants;

import io.swagger.annotations.Api;

@Api(tags = "Authorization Server API")
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private OTPService otpService;

	@ResponseBody
	@PostMapping("/signup")
	public ResponseEntity<Object> signUp(@RequestBody UserDTO userDTO) throws CustomException {
		if (userDTO == null) {
			throw new CustomException(HttpStatus.OK.value(),HttpStatus.BAD_REQUEST.value(),AppConstants.REQUEST_BODY_MISSING);
		}
		return new ResponseEntity<>(userService.signUp(userDTO), HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping("/signin")
	public ResponseEntity<Object> signIn(@RequestBody UserDTO userDTO) throws CustomException {
		if (userDTO == null) {
			throw new CustomException(HttpStatus.OK.value(),HttpStatus.BAD_REQUEST.value(),AppConstants.REQUEST_BODY_MISSING);
		}
		return new ResponseEntity<>(userService.signIn(userDTO), HttpStatus.OK);
	}

	@PostMapping("/signout")
	public ResponseEntity<String> signOut() {
		//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//String username = auth.getName();
		SecurityContextHolder.getContext().setAuthentication(null);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/generate/otp")
	@ResponseBody
	public ResponseEntity<Object> generateOTP(@RequestBody HashMap<String, String> userMap) throws CustomException {
		//Check Logger
		if (userMap == null) {
			throw new CustomException(HttpStatus.OK.value(),HttpStatus.BAD_REQUEST.value(), AppConstants.REQUEST_BODY_MISSING);
		}
		return new ResponseEntity<>(otpService.generateOTP(userMap.get("user")), HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping("/verify/otp")
	public ResponseEntity<Object> validateOTP(@RequestBody HashMap<String, String> userMap) throws CustomException {
		if (userMap == null) {
			throw new CustomException(HttpStatus.OK.value(),HttpStatus.BAD_REQUEST.value(),AppConstants.REQUEST_BODY_MISSING);
		}
		return new ResponseEntity<>(userService.activateAccount(userMap), HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping("/forgotpassword")
	public ResponseEntity<Object> forgotPassword(@RequestBody HashMap<String, String> userMap) throws CustomException {
		if (userMap == null) {
			throw new CustomException(HttpStatus.OK.value(),HttpStatus.BAD_REQUEST.value(),AppConstants.REQUEST_BODY_MISSING);
		}
		return new ResponseEntity<>(userService.forgotPassword(userMap), HttpStatus.OK);
	}
}
