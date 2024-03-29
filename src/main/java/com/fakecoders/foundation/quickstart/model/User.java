package com.fakecoders.foundation.quickstart.model;

import org.hibernate.envers.Audited;

import com.fakecoders.foundation.quickstart.model.audit.BaseEntity;
import com.fakecoders.foundation.quickstart.model.audit.DateAudit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Audited
public class User extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userid;

	private String firstname;

	private String lastname;
	
	private String username;

	private String email;

	private String password;

	private String mobile;

	@Enumerated(EnumType.STRING)
	private Role role;

	private Boolean enable;

	private String verificationCode;

	@Column(nullable = false)
	private Boolean isEmailVerified;

	
}
