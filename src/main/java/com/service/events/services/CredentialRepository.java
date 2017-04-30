package com.service.events.services;

import org.springframework.data.repository.CrudRepository;

import com.service.events.model.LoginForm;

public interface CredentialRepository extends CrudRepository<LoginForm, String> {

	public LoginForm findByUsername(String username);
}
