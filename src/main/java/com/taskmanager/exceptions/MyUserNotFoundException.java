package com.taskmanager.exceptions;

public class MyUserNotFoundException extends RuntimeException {

	// implement serialization for good measure
	private static final long serialVersionUID = 1L;

	public MyUserNotFoundException(String message) {

		super(message);

	}

}
