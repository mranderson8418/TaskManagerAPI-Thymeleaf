package com.taskmanager.exceptions;

public class TaskNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L; // Recommended for serializable classes

	public TaskNotFoundException(String message) {

		super(message);

	}

}
