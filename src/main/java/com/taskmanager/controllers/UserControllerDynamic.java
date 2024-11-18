package com.taskmanager.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.taskmanager.dto.MyUserDto;
import com.taskmanager.model.MyUser;
import com.taskmanager.repository.MyUserRepository;
import com.taskmanager.security.MyUserDetailService;
import com.taskmanager.service.MyUserService;

@Controller
public class UserControllerDynamic {

	// The AuthenticationManager will help us authenticate by username and
	// Initialize a logger for the class
	Logger logger = LoggerFactory.getLogger(UserControllerDynamic.class.getName());

	@Autowired
	private MyUserService myUserService;

	@Autowired
	private MyUserDetailService myUserDetailService;

	@Autowired
	private MyUserRepository myUserRepository;

	@Autowired
	MyUserService userService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	public UserControllerDynamic(MyUserService userService) {
		this.userService = userService;
	}

	@GetMapping("/admin/home")
	public String handleAdminHome() {
		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println("ENTERED...................................@GetMapping('/admin/home')");
		logger.trace("ENTERED……………………………………	@GetMapping(\"/admin/home\")------");

		return "admin-home";
	}

	@GetMapping("/user/home")
	public String handleUserHome() {

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println("ENTERED...................................@GetMapping('/user/home')");
		logger.trace("ENTERED……………………………………	@GetMapping(\"/user/home\")------");

		return "user-home";
	}

	@DeleteMapping("/admin/user/delete/{id}")
	public String deleteUser(@PathVariable int id, @ModelAttribute Optional<MyUser> myUser,
			Model model) throws NotFoundException {
		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		logger.trace("ENTERED……………………………………deleteUser()");

		userService.deleteMyUserById(id);

		logger.trace("EXITED……………………………………deleteUser()");

		myUser = myUserRepository.findById(id);

		model.addAttribute("myUser", myUser);

		return "user-deleted";
	}

	@GetMapping("/login")
	public String handleLogin() {
		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println("ENTERED...................................@GetMapping('/login')");
		logger.trace("ENTERED…………………………………… @GetMapping(\"/login\")------");
		return "login";
	}

	@GetMapping("/admin/userList")
	public String createObject(@ModelAttribute MyUserDto myUserDto, Model model) {
		// Validate object data if necessary
		// Save object to database
		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		logger.trace("ENTERED……………………………………/admin/userList");
		logger.trace("EXITED……………………………………/admin/userList");

		List<MyUser> users = myUserRepository.findAll();

		for (int i = 0; i < users.size(); i++) {

			System.out.println("users.get(i).getId()= " + users.get(i).getId());

			System.out.println("users.get(i).getUsername() = " + users.get(i).getUsername());

			System.out.println("users.get(i).getPassword() " + users.get(i).getPassword());

			System.out.println("users.get(i).getRole()" + users.get(i).getRole());

			System.out.println("users.get(i).getDob()" + users.get(i).getDob());

			System.out.println("users.get(i).getGender()" + users.get(i).getGender());

			System.out.println();
		}

		List<MyUserDto> myUserDtoList = new ArrayList<>();

		// converts the MyUser list into MyUserDto list to add a layer of security
		for (int i = 0; i < users.size(); i++) {

			myUserDto = myUserService.mapToDto(users.get(i));

			myUserDtoList.add(myUserDto);

		}

		for (int i = 0; i < users.size(); i++) {

			System.out.println(myUserDtoList.get(i).getId());

			System.out.println(myUserDtoList.get(i).getUsername());

			System.out.println(myUserDtoList.get(i).getEmail());

			System.out.println(myUserDtoList.get(i).getPassword());

			System.out.println(myUserDtoList.get(i).getRole());

			System.out.println(myUserDtoList.get(i).getDob());

			System.out.println(myUserDtoList.get(i).getGender());

			System.out.println();
		}

		model.addAttribute("users", myUserDtoList);

		return "admin-user-list";
	}

	@GetMapping("/admin/logout")
	public String handleLogOutAdmin(@ModelAttribute MyUserDto myUserDto, Model model) {

		myUserDto = myUserService.currentUser();

		model.addAttribute("username", myUserDto.getUsername());

		System.out.println("myUser.getUsername()= " + myUserDto.getUsername());
		SecurityContextHolder.clearContext();

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());

		logger.trace("ENTERED……………………………………	@GetMapping(\"/admin/logout\")--------");
		logger.trace("EXITED……………………………………	@GetMapping(\"/admin/logout\"---------");

		return "logout";
	}

	@GetMapping("/user/logout")
	public String handleLogOutUser(@ModelAttribute MyUserDto myUserDto, Model model) {
		myUserDto = myUserService.currentUser();

		model.addAttribute("username", myUserDto.getUsername());

		System.out.println("myUser.getUsername()= " + myUserDto.getUsername());
		SecurityContextHolder.clearContext();

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		logger.trace("ENTERED……………………………………	@GetMapping(\"/user/logout\")--------");
		logger.trace("EXITED……………………………………		@GetMapping(\"/user/logout\")---------");

		return "logout";
	}

	@GetMapping("/logout")
	public String handleLogOut(@ModelAttribute MyUserDto myUser, Model model) {

		SecurityContextHolder.clearContext();
		model.addAttribute("username", myUser.getUsername());

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());

		logger.trace("ENTERED……………………………………		@GetMapping(\"/logout\")--------");
		logger.trace("EXITED……………………………………			@GetMapping(\"/logout\")--------");

		return "logout";

	}

	@GetMapping("/admin/index")
	public String handleWelcomeAdmin() {

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println(
				"ENTERED...................................	@GetMapping(\"/admin/index\")");
		logger.trace("ENTERED……………………………………		@GetMapping(\"/admin/index\")------");

		return "admin-index";
	}

	@GetMapping("/user/index")
	public String handleWelcome() {

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println(
				"ENTERED...................................	@GetMapping(\"/user/index\")");
		logger.trace("ENTERED……………………………………		@GetMapping(\"/user/index\")------");

		return "user-index";

	}
	// @GetMapping("/admin/activeuser")
	// public ResponseEntity<MyUserDto> getActiveUser() {
	// LastWord lastWord = new LastWord(getClass().getName());
	// System.out.println("Class = " + lastWord.getLastWord());
	// logger.trace("ENTERED……………………………………getActiveUser()");
	//
	// System.out.println("Class = " + lastWord.getLastWord());
	// logger.trace("EXITED……………………………………getActiveUser()");
	// return new ResponseEntity<>(userService.currentUser(), HttpStatus.OK);
	// }
	//
	// @GetMapping("/admin/getallusers")
	// public ResponseEntity<List<MyUser>> getAllUsers() {
	// LastWord lastWord = new LastWord(getClass().getName());
	// System.out.println("Class = " + lastWord.getLastWord());
	// logger.trace("ENTERED……………………………………getAllUsers()");
	//
	// List<MyUser> users = myUserRepository.findAll();
	//
	// System.out.println("Class = " + lastWord.getLastWord());
	// logger.trace("EXITED……………………………………getAllUsers()");
	//
	// return new ResponseEntity<>(users, HttpStatus.OK);
	// }

	// @GetMapping("/admin/user/{id}")
	// public ResponseEntity<MyUserDto> getUserDetail(@PathVariable("id") int id) {
	// LastWord lastWord = new LastWord(getClass().getName());
	// System.out.println("Class = " + lastWord.getLastWord());
	// logger.trace("ENTERED……………………………………getUserDetail()");
	// logger.trace("EXITED……………………………………getUserDetail()");
	// return new ResponseEntity<>(userService.getMyUserById(id), HttpStatus.OK);
	// }

	// @PutMapping("/admin/userdetail/update/{id}")
	// public ResponseEntity<MyUserDto> updateUserDetail(@RequestBody MyUserDto
	// myUserUpdate, @PathVariable("id") int
	// id) {
	// LastWord lastWord = new LastWord(getClass().getName());
	// System.out.println("Class = " + lastWord.getLastWord());
	// logger.trace("ENTERED……………………………………updateUserDetail()");
	//
	// MyUserDto response = userService.updateMyUserDetail(myUserUpdate, id);
	//
	// logger.trace("EXITED……………………………………updatePrLongDetail()");
	// return new ResponseEntity<>(response, HttpStatus.OK);
	// }

	// -----------
	// convertMyUserDtoToMyUser
	//

}
