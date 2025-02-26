package com.taskmanager.dto;

public class MyUserDto {

	private String dob;

	private String email;

	private String gender;

	private int id;

	private int userNumber;

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

	public int getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(int userNumber) {
		this.userNumber = userNumber;
	}

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
		return "MyUserDto [" + "dob=" + dob + ", email='" + email + '\'' + ", gender='" + gender + '\'' + ", id=" + id +
				", myUserDtoNumber='" + userNumber + '\'' + ", password='" + password + '\'' + ", role='" + role + '\'' +
				", username='" + username + '\'' + ']';
	}

}
