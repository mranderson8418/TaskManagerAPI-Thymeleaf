package com.taskmanager.exceptions;

public class EmailNotMaskedAppropriately extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmailNotMaskedAppropriately(String message) {

		super(message);

	}

}
