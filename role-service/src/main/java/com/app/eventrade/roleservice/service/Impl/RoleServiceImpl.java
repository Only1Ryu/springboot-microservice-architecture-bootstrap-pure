package com.app.eventrade.roleservice.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.eventrade.roleservice.entity.RoleEntity;
import com.app.eventrade.roleservice.exception.CustomException;
import com.app.eventrade.roleservice.exception.NotFoundException;
import com.app.eventrade.roleservice.repository.RoleRepository;
import com.app.eventrade.roleservice.service.RoleService;
import com.app.eventrade.roleservice.util.AppConstants;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public List<RoleEntity> getAllRole() {
		return roleRepository.findAll();
	}

	@Override
	public Optional<RoleEntity> getRoleById(UUID id) throws NotFoundException {
		if (id != null) {
			return roleRepository.findById(id);
		}
		throw new NotFoundException(AppConstants.NO_ROLE);
	}

	@Override
	public Optional<RoleEntity> getRoleByName(String roleName) throws NotFoundException {
		if (!roleName.isBlank()) {
			return roleRepository.findByRoleName(roleName);
		}
		throw new NotFoundException(AppConstants.NO_ROLE);
	}

	@Override
	public String addRole(RoleEntity roleEntity) throws CustomException, NotFoundException {
		if (!getRoleByName(roleEntity.getRoleName()).isPresent()) {
			roleRepository.save(roleEntity);
			return AppConstants.ROLE_CREATED;
		}
		throw new CustomException(AppConstants.ROLE_ALREADY_EXISTS);
	}

	@Override
	public String updateRole(RoleEntity roleEntity) throws CustomException, NotFoundException {
		if (getRoleById(roleEntity.getId()).isPresent()) {
			roleRepository.save(roleEntity);
			return AppConstants.ROLE_UPDATED;
		}
		throw new CustomException(AppConstants.ROLE_ALREADY_EXISTS);
	}

	@Override
	public String deleteRoleById(UUID id) throws CustomException, NotFoundException {
		if (getRoleById(id).isPresent()) {
			roleRepository.deleteById(id);
			return AppConstants.ROLE_DELETED;
		}
		throw new CustomException(AppConstants.NO_ROLE);
	}

}