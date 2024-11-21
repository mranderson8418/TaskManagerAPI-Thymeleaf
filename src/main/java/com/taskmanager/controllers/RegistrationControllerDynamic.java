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
	public String userRegistration(@ModelAttribute MyUserDto myUserDto, Model model) {

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println("ENTERED...................................@PostMapping('/register')");
		logger.trace("ENTERED……………………………………	@PostMapping(\"/register\")------");

		myUserDto.setUsername(myUserDto.getUsername());

		Optional<MyUser> foundUser = myUserRepository.findByUsername(myUserDto.getUsername());

		// The username already exist in the database
		if (foundUser.isPresent()) {

			model.addAttribute(myUserDto.getUsername());

			System.out.println("User exists-------------------" + myUserDto.getUsername());

			return "index-user-exists";

		}

		if (myUserDto.getGender() == null) {
			myUserDto.setGender("male");

		}

		if (myUserDto.getRole() == null) {
			myUserDto.setRole("USER");

		}

		if (myUserDto.getRole().equals("ADMIN")) {

			myUserDto.setRole("ADMIN, USER");
		}

		System.out.println("Username: " + myUserDto.getUsername());

		System.out.println("DOB: " + myUserDto.getDob());
		System.out.println("Email: " + myUserDto.getEmail());
		System.out.println("Password: " + passwordEncoder.encode(myUserDto.getPassword()));
		System.out.println("Role: " + myUserDto.getRole());
		System.out.println("Gender: " + myUserDto.getGender());

		MyUserDto user_inserted = new MyUserDto();

		user_inserted.setDob(myUserDto.getDob());

		user_inserted.setUsername(myUserDto.getEmail());

		user_inserted.setEmail(myUserDto.getEmail().toUpperCase());

		user_inserted.setRole(myUserDto.getRole().toUpperCase());

		user_inserted.setGender(myUserDto.getGender().toUpperCase());

		user_inserted.setPassword(passwordEncoder.encode(myUserDto.getPassword()));

		MyUser myUser = myUserService.convertMyUserDtoToMyUser(user_inserted);

		myUserRepository.save(myUser);

		model.addAttribute("username", myUserDto.getUsername());

		model.addAttribute("gender", myUserDto.getGender());
		model.addAttribute("dob", myUserDto.getDob());
		model.addAttribute("email", myUserDto.getEmail());
		model.addAttribute("password", passwordEncoder.encode(myUserDto.getPassword()));
		model.addAttribute("role", myUserDto.getRole());

		System.out.println("User = " + user_inserted.getUsername() + " is added to the database");

		List<MyUser> users = myUserRepository.findAll();

		model.addAttribute("users", users);

		return "welcome";

	}
}
// logger.trace("ENTERED…………………………………… @PostMapping(\"/register\")------");
//
// // MyUser myUser = myUserService.convertMyUserDtoToMyUser(myUserDto);
//
// Optional<MyUser> foundUser =
// myUserRepository.findByUsername(myUserDto.getUsername());
//
// if (foundUser.isPresent()) {
//
// // model.addAttribute(myUser.getUsername());
//
// System.out.println("User exists-------------------" +
// myUserDto.getUsername());
//
// model.addAttribute("myUserDto", myUserDto);
//
// return "index_user_exists";
//
// }
//
// if (myUserDto.getRole().equals("")) {
// myUserDto.setRole("USER");
//
// }
//
// if (myUserDto.getRole().equals("ADMIN")) {
// myUserDto.setRole("ADMIN,USER");
//
// }
//
// MyUserDto newMyUserDto = myUserService.createUser(myUserDto);
//
// System.out.println("Username: " + myUserDto.getUsername());
//
// System.out.println("Password: " + myUserDto.getPassword());
// System.out.println("Role: " + myUserDto.getRole());
//
// model.addAttribute("username", myUserDto.getUsername());
//
// model.addAttribute("password", myUserDto.getPassword());
// model.addAttribute("role", myUserDto.getRole());
//
// System.out.println("User = " + myUserDto.getUsername() + " is added to the
// database");
//
// return "welcome";
// }

// }
