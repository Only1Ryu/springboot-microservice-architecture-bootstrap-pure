package com.app.eventrade.auth.server.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.eventrade.auth.server.entity.UserEntity;
import com.app.eventrade.auth.server.exception.CustomException;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

	Optional<UserEntity> findByEmail(String email) throws CustomException;

	Optional<UserEntity> findByMobile(String mobile) throws CustomException;
	
	Optional<UserEntity> findByEmailOrMobile(String email,String mobile) throws CustomException;
}
