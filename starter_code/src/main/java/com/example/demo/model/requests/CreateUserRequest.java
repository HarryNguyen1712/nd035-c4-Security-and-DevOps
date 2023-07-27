package com.example.demo.model.requests;

import com.example.demo.validation.PasswordConstraint;
import com.fasterxml.jackson.annotation.JsonProperty;

@PasswordConstraint
public class CreateUserRequest {

	@JsonProperty
	private String username;

	@JsonProperty
	private String password;

	@JsonProperty("confirm_password")
	private String confirmPassword;

	public CreateUserRequest() {
	}

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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
