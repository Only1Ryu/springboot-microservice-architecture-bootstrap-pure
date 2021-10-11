package com.app.eventrade.auth.server.service;

import java.util.HashMap;

import com.app.eventrade.auth.server.DTO.Response;
import com.app.eventrade.auth.server.DTO.UserDTO;
import com.app.eventrade.auth.server.exception.CustomException;

public interface UserService {
	public UserDTO getUserLoginDetail(String loginName);

	public Response signUp(UserDTO user) throws CustomException;

	public Response activateAccount(HashMap<String, String> userMap) throws CustomException;

	public Response forgotPassword(HashMap<String, String> userMap) throws CustomException;
	
	public Response signIn(UserDTO user);
}

