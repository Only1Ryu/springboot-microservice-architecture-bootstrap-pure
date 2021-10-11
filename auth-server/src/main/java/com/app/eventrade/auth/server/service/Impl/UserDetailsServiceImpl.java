package com.app.eventrade.auth.server.service.Impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.app.eventrade.auth.server.DTO.Response;
import com.app.eventrade.auth.server.DTO.RoleDTO;
import com.app.eventrade.auth.server.DTO.UserDTO;
import com.app.eventrade.auth.server.entity.UserEntity;
import com.app.eventrade.auth.server.exception.CustomException;
import com.app.eventrade.auth.server.exception.CustomOAuthException;
import com.app.eventrade.auth.server.feignclient.RoleFeignClient;
import com.app.eventrade.auth.server.repository.UserRepository;
import com.app.eventrade.auth.server.service.OTPService;
import com.app.eventrade.auth.server.service.UserService;
import com.app.eventrade.auth.server.util.AppConstants;
import com.app.eventrade.auth.server.util.CommonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class UserDetailsServiceImpl implements UserService, UserDetailsService {

	private final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleFeignClient roleFeignClient;
	
	@Autowired
	private OTPService otpService;
	
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private ModelMapper modelMapper;
	
	@Value("${client.id}")
	private String CLIENT_ID;
	
	@Value("${client.secret}")
	private String CLIENT_SECRET;

	@Value("${token-url}")
	private String TOKEN_URL;
	
	private static LocalDateTime timeStamp = LocalDateTime.now();

	@Override
	public UserDetails loadUserByUsername(final String loginName) {
		log.debug("Authenticating {}", loginName);
		UserDTO user = null;
		if (!loginName.isEmpty()) {
			user = getUserLoginDetail(loginName);
			if (user == null) {
				throw new CustomOAuthException(HttpStatus.OK.value(),HttpStatus.NOT_FOUND.value(),"User " + loginName + " was not found in the database");
			} else if (user.getStatus().equals("DEACTIVE")) {
				throw new CustomOAuthException(HttpStatus.OK.value(),HttpStatus.BAD_REQUEST.value(),"User " + loginName + " is not activated");
			}
		}
		return new User(user.getEmail(), user.getPassword(), getAuthority(user.getRole().getRoleName()));
	}

	private List<SimpleGrantedAuthority> getAuthority(String roleName) {
		return Arrays.asList(new SimpleGrantedAuthority(roleName));
	}

	@Override
	public UserDTO getUserLoginDetail(String loginName) {
		Optional<UserEntity> userData = null;
		UserDTO userDTO = null;
		try {
			if (loginName.contains("@")) {
				userData = userRepository.findByEmail(loginName);
			} else {
				userData = userRepository.findByMobile(loginName);
			}
			if (userData.isPresent()) {
				userDTO = modelMapper.map(userData.get(), UserDTO.class);
				RoleDTO roleName = getRoleList(userData.get().getRoleId());
				if (roleName != null) {
					userDTO.setRole(roleName);
				}
			}
		} catch (CustomException e) {
			// TODO: handle exception
		}
		return userDTO;
	}

	public RoleDTO getRoleList(UUID roleId) throws CustomException {
		RoleDTO listData;
		if (roleId != null) {
			ResponseEntity<RoleDTO> roleName = roleFeignClient.getRoleById(roleId);
			listData = roleName.getBody();
			return listData;
		}
		throw new CustomException(HttpStatus.OK.value(),HttpStatus.NOT_FOUND.value(), "ROLE NOT FOUND");
	}

	@Override
	public Response signIn(UserDTO user) {
		String url = TOKEN_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("grant_type", "password");
		if (user.getEmail() != null) {
			map.add("username", user.getEmail());
		} else {
			map.add("username", user.getMobile());
		}
		map.add("password", user.getPassword());
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		ResponseEntity<Object> response = restTemplate.postForEntity(url, request, Object.class);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(response.getBody());
		JSONObject responseObject = new JSONObject(json);
		if(responseObject.has("token_type"))
		{
				return new Response(timeStamp,HttpStatus.OK.value(),HttpStatus.OK.value(),response.getBody());
		}
		return new Response(timeStamp,HttpStatus.OK.value(),HttpStatus.CONFLICT.value(),responseObject.get("response"));
	}
    
	@Override
	public Response signUp(UserDTO user) throws CustomException {
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);
		if (userEntity.getEmail() != null && CommonUtil.isEmail(userEntity.getEmail())&& !userRepository.findByEmail(userEntity.getEmail()).isPresent()) {
			userEntity.setRoleId(UUID.fromString("8d56380a-ead6-4a0d-8934-0cadbb7d1ba4"));
			userEntity.setStatus("DEACTIVE");
			userRepository.save(userEntity);
			if(otpService.generateOTP(userEntity.getEmail())>0) 
			{
				return new Response(timeStamp,HttpStatus.OK.value(),HttpStatus.OK.value(),AppConstants.USER_CREATED);
			}
			return new Response(timeStamp,HttpStatus.OK.value(),HttpStatus.OK.value(),AppConstants.USER_CREATED);
		} else if (userEntity.getMobile() != null && CommonUtil.isMobile(userEntity.getMobile()) && !userRepository.findByMobile(userEntity.getMobile()).isPresent()) {
			userEntity.setRoleId(UUID.fromString("8d56380a-ead6-4a0d-8934-0cadbb7d1ba4"));
			userEntity.setStatus("DEACTIVE");
			userRepository.save(userEntity);
			if(otpService.generateOTP(userEntity.getMobile())>0) 
			{
				return new Response(timeStamp,HttpStatus.OK.value(),HttpStatus.OK.value(),AppConstants.USER_CREATED);
			}
		}
		throw new CustomException(HttpStatus.OK.value(),HttpStatus.CONFLICT.value(),AppConstants.USER_ALREADY_EXISTS);
	}

	@Override
	public Response activateAccount(HashMap<String, String> userMap) throws CustomException {
		final String SUCCESS = "Entered OTP is valid";
		final String FAIL = "Entered OTP is NOT valid. Please Retry!";
		Optional<UserEntity> userEntity = null;
		int otp = Integer.parseInt(userMap.get("otp"));
		if (userMap != null) {
			if (userMap.get("status").equals("ACTIVE")) {
				int serverOtp = otpService.getOTP(userMap.get("user"));
				if (serverOtp > 0 && otp == serverOtp) {
					otpService.clearOTP(userMap.get("user"));
					return new Response(timeStamp,HttpStatus.OK.value(),HttpStatus.OK.value(),SUCCESS);
				}
			} else {
				int serverOtp = otpService.getOTP(userMap.get("user"));
				if (serverOtp > 0 && otp == serverOtp) {
						userEntity = userRepository.findByEmailOrMobile(userMap.get("user"),userMap.get("user"));
						if (userEntity.isPresent()) {
							userEntity.get().setStatus("ACTIVE");
							userRepository.save(userEntity.get());
						} 
					otpService.clearOTP(userMap.get("user"));
					return new Response(timeStamp,HttpStatus.OK.value(),HttpStatus.OK.value(),SUCCESS);
				}
			}
		}
		throw new CustomException(HttpStatus.OK.value(),HttpStatus.NOT_ACCEPTABLE.value(),FAIL);
	}

	@Override
	public Response forgotPassword(HashMap<String, String> userMap) throws CustomException {
		final String SUCCESS = "Password has been changed successfully";
		final String FAIL = "Entered OTP is NOT valid. Please Retry!";
		Optional<UserEntity> userEntity = null;
		int otp = Integer.parseInt(userMap.get("otp"));
		if (userMap != null && userMap.get("password")!=null && userMap.get("otp")!=null && userMap.get("user")!=null ) {
				int serverOtp = otpService.getOTP(userMap.get("user"));
				if (serverOtp > 0 && otp == serverOtp) {
						userEntity = userRepository.findByEmailOrMobile(userMap.get("user"),userMap.get("user"));
						if (userEntity.isPresent()) {
							userEntity.get().setPassword(userMap.get("password"));
							userRepository.save(userEntity.get());
						} 
					otpService.clearOTP(userMap.get("user"));
					return new Response(timeStamp,HttpStatus.OK.value(),HttpStatus.OK.value(),SUCCESS);
				}
			}
		throw new CustomException(HttpStatus.OK.value(),HttpStatus.NOT_ACCEPTABLE.value(),FAIL);	
	}
}