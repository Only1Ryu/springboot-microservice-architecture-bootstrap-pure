package com.app.eventrade.roleservice.entity;

import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Entity
@Table(name = "roleInfo")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@DynamicUpdate
public class RoleEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")	
	@JsonProperty("roleId")
	private UUID id;
	
	@JsonProperty("roleName")
	private String roleName;	
}
