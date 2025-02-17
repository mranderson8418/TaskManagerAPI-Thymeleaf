package com.taskmanager.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class MyUserDto {

	private String dob;

	private String email;

	private String gender;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// private String myUsernumber;

	private String password;

	private String role;

	private String username;

	public String getDob() {
		return dob;
	}

	public String getEmail() {
		return email;
	}

	public String getGender() {
		return gender;
	}

	public int getId() {
		return id;
	}

	// public String getMyUserNumber() {
	// return myUsernumber;
	// }

	public String getPassword() {
		return password;
	}

	public String getRole() {
		return role;
	}

	public String getUsername() {
		return this.getEmail();
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setId(int id) {
		this.id = id;
	}

	// public void setMyUserNumber(String myUserNumber) {
	// this.myUsernumber = myUserNumber;
	// }

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "MyUser [id=" + id + ", username=" + username + ", dob=" + dob + ", email=" + email + ", password=" +
				password + ", gender=" + gender + ", role=" + role + "]";
	}

}
