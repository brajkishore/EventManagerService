/**
 * 
 */
package com.service.events.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author brakisho
 *
 */

@Document(collection = "credentials")
public class UserInfo {

	private String username;
	private String password;
	private String role;
	private boolean enabled;
	private String fullName;
	
	public String getUsername() {
		return username;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	
	
	
	
	
}
