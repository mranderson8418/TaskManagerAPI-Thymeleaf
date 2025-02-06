package com.taskmanager.dto;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class MyUserDto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank
	@Column(name = "username")
	private String username;

	@NotBlank
	@Column(name = "email")
	private String email;

	@NotBlank
	@Column(name = "password")
	private String password;

	@NotBlank
	@Column(name = "role")
	private String role;

	@NotBlank
	@Column(name = "dob")
	private String dob;

	@NotBlank
	@Column(name = "gender")
	private String gender;

	public List<Integer> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Integer> taskList) {
		this.taskList = taskList;
	}

	private List<Integer> taskList = new LinkedList<>();

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDob() {
		return dob;
	}

	public String getEmail() {
		return email;
	}

	public String getGender() {
		return gender;
	}

	public String getPassword() {
		return password;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return this.getEmail();
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "MyUser [id=" + id + ", username=" + username + ", dob=" + dob + ", email=" + email + ", password=" + password +
				", gender=" + gender + ", role=" + role + "]";
	}

}
