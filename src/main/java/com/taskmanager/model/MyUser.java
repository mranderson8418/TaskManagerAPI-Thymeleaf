package com.taskmanager.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "myusers")
@JsonPropertyOrder({ "id", "username", "email", "password", "role", "dob", "gender", "taskCount" })
public class MyUser {

	@NotBlank
	@Column(name = "dob")
	private String dob;

	@NotBlank
	@Column(name = "email")
	private String email;

	@NotBlank
	@Column(name = "gender")
	private String gender;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank
	@Column(name = "password")
	private String password;

	@NotBlank
	@Column(name = "role")
	private String role;

	@Column(name = "taskCount")
	private int taskCount;

	private List<MyTask> taskList;

	@NotBlank
	@Column(name = "username")
	private String username;

	public MyUser() {
		this.id = 0;
		this.username = "";
		this.email = "";
		this.password = "";
		this.role = "";
		this.dob = "";
		this.gender = "";
		this.taskCount = 0;
		this.taskList = null;
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

	public int getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public String getRole() {
		return role;
	}

	public int getTaskCount() {
		return taskCount;
	}

	public List<MyTask> getTaskList() {
		return taskList;
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

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}

	public void setTaskList(List<MyTask> taskList) {
		this.taskList = taskList;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "MyUser [id=" + id + ", username=" + username + ", dob=" + dob + ", email=" + email + ", password="
				+ password + ", gender=" + gender + ", role=" + role + "]";
	}

}
