package com.taskmanager.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.taskmanager.auth.AuthenticationAccessHandler;
import com.taskmanager.dto.MyUserDto;
import com.taskmanager.exceptions.UserAlreadyExistsException;
import com.taskmanager.model.MyUser;
import com.taskmanager.repository.MyUserRepository;
import com.taskmanager.service.MyUserService;

@Controller
public class RegistrationControllerDynamic {

	Logger logger = LoggerFactory.getLogger(AuthenticationAccessHandler.class.getName());
	@Autowired

	MyUserService myUserService;
	@Autowired
	private MyUserRepository myUserRepository;

	/*
	 * When the class has only one constructor argument, and the argument matches
	 * the field name -- then spring can automatically identify the dependency to be
	 * injected public RegistrationController(MyUserRepository myUserRepository) {
	 * this.myUserRepository = myUserRepository; }
	 */

	@Autowired
	PasswordEncoder passwordEncoder;

	@GetMapping("/register")
	public String handleRegisterUser(Model model) {

		model.addAttribute("myUser", new MyUserDto());

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());

		System.out.println("ENTERED...................................@GetMapping('/register')");
		logger.trace("ENTERED……………………………………	@GetMapping(\"/register\")------");

		return "register-form";
	}

	/* NEED TO USE INTERFACE USERSERVICEIMPL.JAVA TO REGISTER USERS */
	@PostMapping("/register")
	// @ModelAttribute - this will bind the annotated object with the model class
	public String userRegistration(@ModelAttribute MyUserDto myUserDto, Model model) throws UserAlreadyExistsException {

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println("ENTERED...................................@PostMapping('/register')");
		logger.trace("ENTERED……………………………………	@PostMapping(\"/register\")------");

		myUserDto.setUsername(myUserDto.getUsername());

		Optional<MyUser> foundUser = myUserRepository.findByUsername(myUserDto.getUsername());

		// The username already exist in the database
		if (foundUser.isPresent()) {
			throw new UserAlreadyExistsException("The User already Exists.");

		}

		if (myUserDto.getGender() == "") {
			myUserDto.setGender("NONE");

		}

		if (myUserDto.getRole() == "") {
			myUserDto.setRole("USER");

		}

		if (myUserDto.getDob() == "") {
			myUserDto.setDob("00/00/0000");

		}
		if (myUserDto.getRole().equals("ADMIN")) {

			myUserDto.setRole("ADMIN, USER");
		}

		// sends the new user credentials and saves them in the MyUserRepository
		myUserDto = myUserService.createUser(myUserDto);

		model.addAttribute("id", myUserDto.getId());
		model.addAttribute("username", myUserDto.getUsername());
		model.addAttribute("userNumber", myUserDto.getUserNumber());
		model.addAttribute("gender", myUserDto.getGender());
		model.addAttribute("dob", myUserDto.getDob());
		model.addAttribute("email", myUserDto.getEmail());
		model.addAttribute("password", passwordEncoder.encode(myUserDto.getPassword()));
		model.addAttribute("role", myUserDto.getRole());

		System.out.println("User = " + myUserDto.getUsername() + " is added to the database");

		List<MyUser> users = myUserRepository.findAll();

		// model.addAttribute("users", users);

		return "created-new-user";

	}
}
