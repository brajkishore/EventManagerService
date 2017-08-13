package com.nroutes.events.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

//@Document(collection = "credentials")
public class LoginForm {

	private String id;
	
	@Indexed(unique=true)
	private String username;
	private String password;
	public String getUsername() {
		return username;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "LoginForm [id=" + id + ", username=" + username + ", password=" + password + "]";
	}
	
	
	
}
