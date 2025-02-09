package com.taskmanager.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "mytasks")
@JsonPropertyOrder({ "id", "username", "content", "complete", "tasknumber" })
public class MyTask {

	@NotBlank
	@Column(name = "complete")
	private boolean complete;

	@NotBlank
	@Column(name = "content")
	private String content;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank
	@Column(name = "tasknumber")
	private int taskNumber;

	@NotBlank
	@Column(name = "username")
	private String username;

	public String getContent() {
		return content;
	}

	public int getId() {
		return id;
	}

	/**
	 * @return the taskNumber
	 */
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

	/**
	 * @param taskNumber the taskNumber to set
	 */
	public void setTaskNumber(int taskNumber) {
		this.taskNumber = taskNumber;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}