package com.taskmanager.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(TaskNotFoundException.class)
	public ResponseEntity<ErrorObject> handleTaskNotFoundException(TaskNotFoundException ex, WebRequest request) {

		ErrorObject errorObject = new ErrorObject();
		errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());

		return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MyUserNotFoundException.class)
	public ResponseEntity<ErrorObject> handleUserNotFoundException(MyUserNotFoundException ex, WebRequest request) {

		ErrorObject errorObject = new ErrorObject();
		errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());

		return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ActiveUserCannotBeDeletedException.class)
	public ResponseEntity<ErrorObject> handleActiveUserCannotBeDeleted(ActiveUserCannotBeDeletedException ex,
			WebRequest request) {

		ErrorObject errorObject = new ErrorObject();
		errorObject.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED.value());
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());

		return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
	}

}
