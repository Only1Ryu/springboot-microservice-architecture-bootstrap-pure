package com.app.eventrade.roleservice.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.eventrade.roleservice.dto.Response;
import com.app.eventrade.roleservice.entity.RoleEntity;
import com.app.eventrade.roleservice.exception.CustomException;
import com.app.eventrade.roleservice.exception.InvalidDataException;
import com.app.eventrade.roleservice.exception.NotFoundException;
import com.app.eventrade.roleservice.service.RoleService;
import com.app.eventrade.roleservice.util.AppConstants;

import io.swagger.annotations.Api;

@RestController
@RefreshScope
@Api(tags = "Role Service API")
@RequestMapping(AppConstants.ROLE)
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	private static LocalDateTime timeStamp = LocalDateTime.now();
	
	@ResponseBody
	@GetMapping("/getAllRole")
	public ResponseEntity<Object> getAll()
	{
		return new ResponseEntity<>(roleService.getAllRole(),HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping("/getRoleById/{id}")
	public ResponseEntity<Object> getByRoleId(@PathVariable UUID id) throws InvalidDataException, NotFoundException
	{
		if(id == null)
		{
			throw new InvalidDataException(AppConstants.NO_UUID);
		}
		return new ResponseEntity<>(roleService.getRoleById(id),HttpStatus.OK);
	}	
	
	@ResponseBody
	@GetMapping("/getRoleByName/{roleName}")
	public ResponseEntity<Object> getByRoleId(@PathVariable String roleName) throws InvalidDataException, NotFoundException
	{
		if(roleName.isBlank())
		{
			throw new InvalidDataException(AppConstants.NO_UUID);
		}
		return new ResponseEntity<>(roleService.getRoleByName(roleName),HttpStatus.OK);
	}	
	
	@ResponseBody
	@PostMapping(value = "/addRole",produces = "application/json")
	public ResponseEntity<Object> addRole(@RequestBody RoleEntity roleEntity) throws InvalidDataException, CustomException, NotFoundException
	{
		if(roleEntity == null)
		{
			throw new InvalidDataException(AppConstants.REQUEST_BODY_MISSING);
		}
		Response response = new Response();
		response.setResponse(roleService.addRole(roleEntity));
		response.setStatusCode(HttpStatus.OK.value());
		response.setTimeStamp(timeStamp);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@ResponseBody
	@PutMapping("/updateRole")
	public ResponseEntity<Object> updateRole(@RequestBody RoleEntity roleEntity) throws InvalidDataException, CustomException, NotFoundException
	{
		if(roleEntity == null)
		{
			throw new InvalidDataException(AppConstants.REQUEST_BODY_MISSING);
		}
		Response response = new Response();
		response.setResponse(roleService.updateRole(roleEntity));
		response.setStatusCode(HttpStatus.OK.value());	
		response.setTimeStamp(timeStamp);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping("/deleteRoleById/{id}")
	public ResponseEntity<Object> deleteRoleById(@PathVariable UUID id) throws InvalidDataException, CustomException, NotFoundException
	{
		if(id == null)
		{
			throw new InvalidDataException(AppConstants.NO_UUID);
		}
		Response response = new Response();
		response.setResponse(roleService.deleteRoleById(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.setTimeStamp(timeStamp);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
}
