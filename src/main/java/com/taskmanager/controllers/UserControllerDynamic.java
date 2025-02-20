package com.taskmanager.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.taskmanager.dto.MyUserDto;
import com.taskmanager.model.MyUser;
import com.taskmanager.repository.MyUserRepository;
import com.taskmanager.security.MyUserDetailService;
import com.taskmanager.service.MyUserService;

@Controller
public class UserControllerDynamic {

	@Autowired
	private AuthenticationManager authenticationManager;

	// The AuthenticationManager will help us authenticate by username and
	// Initialize a logger for the class
	Logger logger = LoggerFactory.getLogger(UserControllerDynamic.class.getName());

	@Autowired
	private MyUserDetailService myUserDetailService;

	@Autowired
	private MyUserRepository myUserRepository;

	@Autowired
	private MyUserService myUserService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	MyUserService userService;

	public UserControllerDynamic(MyUserService userService) {
		this.userService = userService;
	}

	@GetMapping("/admin/userList")
	public String createUserList(Model model) {

		logger.trace("ENTERED……………………createObject()………………/admin/userList");

		List<MyUserDto> myUserDtoList = myUserService.getAllMyUsersNow();

		if (myUserDtoList == null) {
			myUserDtoList = new ArrayList<>();
		}
		for (int i = 0; i < myUserDtoList.size(); i++) {

			System.out.println(myUserDtoList.get(i).toString());

		}

		model.addAttribute("users", myUserDtoList);

		logger.trace("EXITED………………createObject()…………………/user/userList");

		return "admin-user-list";
	}

	@GetMapping("/admin/delete/user")
	public String getDeleteUser(@ModelAttribute MyUserDto myUserDto, Model model) {
		// Validate object data if necessary
		// Save object to database

		logger.trace("ENTERED……………………………………	@GetMapping(\"/user/delete/user\")");

		List<MyUser> myUserList = myUserRepository.findAll();

		List<MyUserDto> myUserDtoList = new ArrayList<>();

		// converts the MyUser list into MyUserDto list to add a layer of security
		for (int i = 0; i < myUserList.size(); i++) {

			myUserDto = userService.mapToDto(myUserList.get(i));

			myUserDtoList.add(myUserDto);

		}

		model.addAttribute("users", myUserDtoList);

		logger.trace("EXITED……………………………………	@GetMapping(\"/user/delete/user\")");

		return "admin-delete-user";
	}

	@PostMapping("/admin/delete/user")
	public String deleteUserId(@ModelAttribute MyUserDto myUserDto, Model model) {

		logger.trace("ENTERED……………………………………	@PostMapping(\"/user/delete/task\")------");

		myUserService.deleteMyUserById(myUserDto.getUserNumber());

		List<MyUserDto> myUserDtoList = myUserService.afterDeleteGetAllUsers();
		// List<MyUserDto> myUserDtoList = myUserService.getAllMyUsersNow();

		model.addAttribute("users", myUserDtoList);

		return "admin-delete-user";

	}

	@GetMapping("/login")
	public String handleLogin(@ModelAttribute MyUserDto myUserDto, Model model) {

		logger.trace("EXITED…………………………………… @GetMapping(\"/logout\"---------");

		return "login";
	}

	@GetMapping("/user/logout")
	public String handleLogOutuser(@ModelAttribute MyUserDto myUserDto, Model model) {

		myUserDto = myUserService.currentUser();

		model.addAttribute("username", myUserDto.getUsername());

		SecurityContextHolder.clearContext();

		logger.trace("EXITED……………………………………	@GetMapping(\"/user/logout\"---------");

		return "logout";
	}

	@GetMapping("/user/home")
	public String handleUserHome() {

		logger.trace("ENTERED……………………………………	@GetMapping(\"/user/home\")------");

		return "user-home";
	}

	@GetMapping("/user/index")
	public String handleUserIndex() {

		logger.trace("ENTERED……………………………………		@GetMapping(\"/user/index\")------");

		return "user-index";

	}

	@GetMapping("/admin/home")
	public String handleAdminHome() {

		logger.trace("ENTERED……………………………………	@GetMapping(\"/admin/home\")------");

		return "admin-home";
	}

	@GetMapping("/admin/index")
	public String handleUserWelcome() {

		logger.trace("ENTERED……………………………………		@GetMapping(\"/admin/index\")------");

		return "admin-index";

	}

}
