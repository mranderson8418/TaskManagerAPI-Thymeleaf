package com.taskmanager.exceptions;

public class MyUserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MyUserNotFoundException(String message) {

		super(message);

	}

}
