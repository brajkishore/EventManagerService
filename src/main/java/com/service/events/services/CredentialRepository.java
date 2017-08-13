package com.service.events.services;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.annotation.Secured;

import com.service.events.model.UserInfo;

public interface CredentialRepository extends CrudRepository<UserInfo, String> {
		
	public UserInfo findByUsername(String username);
}
