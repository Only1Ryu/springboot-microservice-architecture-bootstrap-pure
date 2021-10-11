package com.app.eventrade.roleservice.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.app.eventrade.roleservice.entity.RoleEntity;
import com.app.eventrade.roleservice.exception.CustomException;
import com.app.eventrade.roleservice.exception.NotFoundException;

public interface RoleService {
	public List<RoleEntity> getAllRole();

	public Optional<RoleEntity> getRoleById(UUID id) throws NotFoundException;
	
	public Optional<RoleEntity> getRoleByName(String roleName) throws NotFoundException;
	
	public String addRole(RoleEntity roleEntity) throws CustomException, NotFoundException;
	
	public String updateRole(RoleEntity roleEntity) throws CustomException, NotFoundException;
	
	public String deleteRoleById(UUID id) throws CustomException, NotFoundException;
}