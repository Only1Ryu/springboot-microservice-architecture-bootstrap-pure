package com.app.eventrade.auth.server.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.app.eventrade.auth.server.util.EncryptDecryptDB;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@Table(name = "userInfo")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 1063814935596064907L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "ID")
	@JsonProperty("userId")
	private UUID id;

	@JsonProperty("firstName")
	@Column(name = "firstName")
	@Convert(converter = EncryptDecryptDB.class)
	private String firstName;

	@JsonProperty("middleName")
	@Column(name = "middleName")
	@Convert(converter = EncryptDecryptDB.class)
	private String middleName;

	@JsonProperty("lastName")
	@Column(name = "lastName")
	@Convert(converter = EncryptDecryptDB.class)
	private String lastName;

	@JsonProperty("email")
	@Column(name = "email")
	@Convert(converter = EncryptDecryptDB.class)
	private String email;

	@JsonProperty("password")
	@Column(name = "password")
	@Convert(converter = EncryptDecryptDB.class)
	private String password;

	@JsonProperty("mobile")
	@Column(name = "mobile")
	@Convert(converter = EncryptDecryptDB.class)
	private String mobile;

	@JsonProperty("kycId")
	@Column(name = "kycId")
	private UUID kycId;

	@JsonProperty("roleId")
	@Column(name = "roleId")
	private UUID roleId;

	@JsonProperty("status")
	@Column(name = "status")
	@Convert(converter = EncryptDecryptDB.class)
	private String status;

	@JsonProperty("creationTime")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "creationTime")
	private Date creationTime;

	@JsonProperty("updatedTime")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "updatedTime")
	private Date updatedTime;

	@JsonProperty("dateofBirth")
	@Temporal(value = TemporalType.DATE)
	@Convert(converter = EncryptDecryptDB.class)
	@Column(name = "DOB")
	private Date dateofBirth;

	@JsonProperty("profilePic")
	@Lob
	@Column(name = "profilePic")
	private byte[] profilePic;
}
