package com.taskmanager.exceptions;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.taskmanager.dto.MyUserDto;
import com.taskmanager.service.MyUserService;

@ControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	private MyUserService myUserService;

	@ExceptionHandler(TaskNotFoundException.class)
	public String handleTaskNotFoundException(TaskNotFoundException ex, WebRequest request, Model model) {

		String username = verifyLoggedInUser();

		MyUserDto myUserDto = myUserService.findByUsername(username);

		ErrorObject errorObject = new ErrorObject();
		errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());

		model.addAttribute("message", errorObject.getMessage());
		model.addAttribute("statusCode", errorObject.getStatusCode());
		model.addAttribute("timeStamp", errorObject.getTimestamp());

		if (myUserDto.getRole().contains("ADMIN")) {
			return "admin-task-not-found-exception";
		} else {
			return "user-task-not-found-exception";
		}
	}

	public String verifyLoggedInUser() {

		System.out.println("Entered...........................verifyLoggedInUser()");

		// Get Authentication from the security context
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// from the UserDetails get the "principal" or user currently logged in
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		// Get user name from the userDetails
		String username = userDetails.getUsername();
		System.out.println("Exited...........................verifyLoggedInUser()");
		System.out.println("\n\n");
		return username;
	}

	// @ExceptionHandler(TaskNotFoundException.class)
	// public String handleUserExists(TaskNotFoundException ex, WebRequest request,
	// Model model) {
	//
	// String username = verifyLoggedInUser();
	//
	// MyUserDto myUserDto = myUserService.findByUsername(username);
	//
	// ErrorObject errorObject = new ErrorObject();
	// errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
	// errorObject.setMessage(ex.getMessage());
	// errorObject.setTimestamp(new Date());
	//
	// model.addAttribute("message", errorObject.getMessage());
	// model.addAttribute("statusCode", errorObject.getStatusCode());
	// model.addAttribute("timeStamp", errorObject.getTimestamp());
	//
	// if (myUserDto.getRole().contains("ADMIN")) {
	// return "admin-user-exists-exception";
	// } else {
	// return "user-user-exists-exception";
	// }
	// }

	@ExceptionHandler(EmailNotMaskedAppropriately.class)
	public String handleTaskNotFoundException(EmailNotMaskedAppropriately ex, WebRequest request, Model model) {

		String username = verifyLoggedInUser();

		MyUserDto myUserDto = myUserService.findByUsername(username);

		ErrorObject errorObject = new ErrorObject();
		errorObject.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED.value());
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());

		model.addAttribute("message", errorObject.getMessage());
		model.addAttribute("statusCode", errorObject.getStatusCode());
		model.addAttribute("timeStamp", errorObject.getTimestamp());

		if (myUserDto.getRole().contains("ADMIN")) {
			return "admin-email-not-masked";
		} else {
			return "user-email-not-masked";
		}
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

	// this will bring in the data that is being received
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
