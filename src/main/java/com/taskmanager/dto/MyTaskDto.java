package com.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;

public class MyTaskDto {

	private boolean complete;

	@NotBlank
	private String content;

	private int id;

	private int taskNumber;

	@NotBlank
	private String username;

	public MyTaskDto() {

		this.complete = false;
		this.content = "";
		this.taskNumber = 0;
		this.username = "";

	}

	public String getContent() {
		return content;
	}

	public int getId() {
		return id;
	}

	public int getTaskNumber() {
		return taskNumber;
	}

	public String getUsername() {

		return username;
	}

	public boolean isComplete() {
		return complete;
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

	public void setTaskNumber(int taskNumber) {
		this.taskNumber = taskNumber;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "MyTaskDto [complete=" + complete + ", content=" + content + ", id=" + id + ", username=" + username +
				"]";
	}

}