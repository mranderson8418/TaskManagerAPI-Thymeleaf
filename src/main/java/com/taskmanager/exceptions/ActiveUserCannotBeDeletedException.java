package com.taskmanager.exceptions;

public class ActiveUserCannotBeDeletedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ActiveUserCannotBeDeletedException(String message) {

		super(message);

	}

}
