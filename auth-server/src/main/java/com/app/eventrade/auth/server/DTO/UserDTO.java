package com.app.eventrade.auth.server.DTO;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class UserDTO {

	@JsonProperty("userId")
	private UUID id;

	@JsonProperty("email")
	private String email;

	@JsonProperty("mobile")
	private String mobile;

	@JsonProperty("password")
	private String password;

	@JsonProperty("otp")
	private String otp;

	@JsonProperty("role")
	private RoleDTO role;

	@JsonProperty("status")
	private String status;

}