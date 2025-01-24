package com.taskmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "message")
public class MyTask {

	private boolean complete;

	@NotBlank
	private String content;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank
	private String username;

	public String getContent() {
		return content;
	}

	public int getId() {
		return id;
	}

	public boolean isComplete() {
		return complete;
	}

	public String getUsername() {
		return username;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}