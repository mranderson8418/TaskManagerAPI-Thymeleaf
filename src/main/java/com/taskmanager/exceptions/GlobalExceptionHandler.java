package com.taskmanager.exceptions;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.taskmanager.dto.MyUserDto;
import com.taskmanager.service.MyUserService;

@ControllerAdvice
public class GlobalExceptionHandler {

	// @ExceptionHandler(TaskNotFoundException.class)
	// public ResponseEntity<ErrorObject>
	// handleTaskNotFoundException(TaskNotFoundException ex, WebRequest request) {
	//
	// ErrorObject errorObject = new ErrorObject();
	// errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
	// errorObject.setMessage(ex.getMessage());
	// errorObject.setTimestamp(new Date());
	//
	// return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
	// }

	@Autowired
	private MyUserService myUserService;

	@ExceptionHandler(TaskNotFoundException.class)
	public String handleTaskNotFoundException(TaskNotFoundException ex, WebRequest request, Model model) {

		ErrorObject errorObject = new ErrorObject();
		errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());

		model.addAttribute("message", errorObject.getMessage());
		model.addAttribute("statusCode", errorObject.getStatusCode());
		model.addAttribute("timeStamp", errorObject.getTimestamp());

		return "task-not-found-exception";
	}

	@ExceptionHandler(EmailNotMaskedAppropriately.class)
	public String handleTaskNotFoundException(EmailNotMaskedAppropriately ex, WebRequest request, Model model) {

		ErrorObject errorObject = new ErrorObject();
		errorObject.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED.value());
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());

		model.addAttribute("message", errorObject.getMessage());
		model.addAttribute("statusCode", errorObject.getStatusCode());
		model.addAttribute("timeStamp", errorObject.getTimestamp());

		return "email-not-masked-appropriately";
	}

	@ExceptionHandler(NullPointerException.class)
	public String handleNullPointerException(NullPointerException ex, WebRequest request, Model model) {

		ErrorObject errorObject = new ErrorObject();
		errorObject.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED.value());
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());

		model.addAttribute("message", errorObject.getMessage());
		model.addAttribute("statusCode", errorObject.getStatusCode());
		model.addAttribute("timeStamp", errorObject.getTimestamp());

		MyUserDto myUserDto = myUserService.currentUser();

		System.out.println("ROLE ===== " + myUserDto.getRole());

		if (myUserDto.getRole().contains("ADMIN")) {

			return "admin-null-pointer-exception";
		}
		return "user-null-pointer-exception";

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
