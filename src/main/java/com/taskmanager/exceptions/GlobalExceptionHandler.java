package com.taskmanager.exceptions;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
			return "admin-exception-task-not-found";
		} else {
			return "user-exception-task-not-found";
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
			return "admin-exception-email-not-masked";
		} else {
			return "user-exception-email-not-masked";
		}
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public String handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request, Model model) {

		// String username = verifyLoggedInUser();

		// MyUserDto myUserDto = myUserService.findByUsername(username);

		ErrorObject errorObject = new ErrorObject();
		errorObject.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED.value());
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());

		model.addAttribute("message", errorObject.getMessage());
		model.addAttribute("statusCode", errorObject.getStatusCode());
		model.addAttribute("timeStamp", errorObject.getTimestamp());

		return "exception-user-already-exists";
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

			return "admin-exception-null-pointer";
		}
		return "user-exception-null-pointer";

	}

	// this will bring in the data that is being received
	@ExceptionHandler(MyUserNotFoundException.class)
	public String handleUserNotFoundException(MyUserNotFoundException ex, WebRequest request, Model model) {

		ErrorObject errorObject = new ErrorObject();
		errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());

		model.addAttribute("message", errorObject.getMessage());
		model.addAttribute("statusCode", errorObject.getStatusCode());
		model.addAttribute("timeStamp", errorObject.getTimestamp());

		MyUserDto myUserDto = myUserService.currentUser();

		if (myUserDto.getRole().contains("ADMIN")) {

			return "admin-exception-user-not-found";
		}
		return "user-exception-user-not-found";

	}

	@ExceptionHandler(ActiveUserCannotBeDeletedException.class)
	public String handleActiveUserCannotBeDeleted(ActiveUserCannotBeDeletedException ex, WebRequest request, Model model) {

		ErrorObject errorObject = new ErrorObject();
		errorObject.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED.value());
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());

		model.addAttribute("message", errorObject.getMessage());
		model.addAttribute("statusCode", errorObject.getStatusCode());
		model.addAttribute("timeStamp", errorObject.getTimestamp());

		MyUserDto myUserDto = myUserService.currentUser();

		if (myUserDto.getRole().contains("ADMIN")) {

			return "admin-exception-cannot-delete-active-user";
		}
		return "user-exception-cannot-delete-active-user";

	}

}
