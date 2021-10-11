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
public class RoleDTO {

	@JsonProperty("roleId")
	private UUID id;

	@JsonProperty("roleName")
	private String roleName;
}