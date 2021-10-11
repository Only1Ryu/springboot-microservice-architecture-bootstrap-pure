package com.app.eventrade.auth.server.feignclient;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.app.eventrade.auth.server.DTO.RoleDTO;
import com.app.eventrade.auth.server.exception.CustomException;

@FeignClient(name = "role-service")
public interface RoleFeignClient {

	@GetMapping("/role/getRoleById/{id}")
	public ResponseEntity<RoleDTO> getRoleById(@PathVariable UUID id) throws CustomException;

}
