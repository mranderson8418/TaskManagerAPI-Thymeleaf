package com.taskmanager.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String createObject(@ModelAttribute MyUserDto myUserDto, Model model) {
		// Validate object data if necessary
		// Save object to database
		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		logger.trace("ENTERED……………………………………/user/userList");
		logger.trace("EXITED……………………………………/user/userList");

		List<MyUser> users = myUserRepository.findAll();

		List<MyUserDto> myUserDtoList = new ArrayList<>();

		// converts the MyUser list into MyUserDto list to add a layer of security
		for (int i = 0; i < users.size(); i++) {

			myUserDto = myUserService.mapToDto(users.get(i));

			myUserDtoList.add(myUserDto);

		}

		model.addAttribute("users", myUserDtoList);

		return "admin-user-list";
	}

	@PostMapping("/admin/delete/user")
	public String deleteUserId(@RequestParam("selectedUserId") int selectedUserId, Model model) {

		logger.trace("ENTERED……………………………………	@PostMapping(\"/user/delete/task\")------");

		Optional<MyUser> foundUser = myUserRepository.findById(selectedUserId);

		try {
			// delete the user with the userNumber value
			userService.deleteMyUserById(selectedUserId);

			if (foundUser.isPresent()) {

				System.out.println("Task exists-------------------" + selectedUserId);

				List<MyUser> myUserList = myUserRepository.findAll();

				MyUserDto myUserDto = new MyUserDto();

				List<MyUserDto> myUserDtoList = new ArrayList<>();

				// converts the MyTask list into MyTaskDto list to add a layer of security
				for (int i = 0; i < myUserList.size(); i++) {

					myUserDto = myUserService.mapToDto(myUserList.get(i));

					myUserDtoList.add(myUserDto);

				}

				model.addAttribute("users", myUserDtoList);

				return "admin-delete-user";

			}
		} catch (UsernameNotFoundException unfe) {
			throw new UsernameNotFoundException("User with ID # " + selectedUserId + "not found in the user database");
		}
		return null;

	}

	@GetMapping("/admin/delete/user")
	public String getDeleteUser(@ModelAttribute MyUserDto myUserDto, Model model) {
		// Validate object data if necessary
		// Save object to database
		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		logger.trace("ENTERED……………………………………	@GetMapping(\"/user/delete/user\")");
		logger.trace("EXITED……………………………………	@GetMapping(\"/user/delete/user\")");

		List<MyUser> myUserList = myUserRepository.findAll();

		List<MyUserDto> myUserDtoList = new ArrayList<>();

		// converts the MyUser list into MyUserDto list to add a layer of security
		for (int i = 0; i < myUserList.size(); i++) {

			myUserDto = userService.mapToDto(myUserList.get(i));

			myUserDtoList.add(myUserDto);

		}

		model.addAttribute("users", myUserDtoList);

		return "admin-delete-user";
	}

	@GetMapping("/login")
	public String handleLogin(@ModelAttribute MyUserDto myUserDto, Model model) {

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());

		logger.trace("ENTERED…………………………………… @GetMapping(\"/login\")--------");
		logger.trace("EXITED…………………………………… @GetMapping(\"/logout\"---------");

		return "login";
	}

	@GetMapping("/user/logout")
	public String handleLogOutuser(@ModelAttribute MyUserDto myUserDto, Model model) {

		myUserDto = myUserService.currentUser();

		model.addAttribute("username", myUserDto.getUsername());

		System.out.println("myUser.getUsername()= " + myUserDto.getUsername());
		SecurityContextHolder.clearContext();

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());

		logger.trace("ENTERED……………………………………	@GetMapping(\"/user/logout\")--------");
		logger.trace("EXITED……………………………………	@GetMapping(\"/user/logout\"---------");

		return "logout";
	}

	@GetMapping("/user/home")
	public String handleUserHome() {
		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println("ENTERED...................................@GetMapping('/user/home')");
		logger.trace("ENTERED……………………………………	@GetMapping(\"/user/home\")------");

		return "user-home";
	}

	@GetMapping("/user/index")
	public String handleUserIndex() {

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println("ENTERED...................................	@GetMapping(\"/user/index\")");
		logger.trace("ENTERED……………………………………		@GetMapping(\"/user/index\")------");

		return "user-index";

	}

	@GetMapping("/admin/home")
	public String handleAdminHome() {
		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println("ENTERED...................................@GetMapping('/admin/home')");
		logger.trace("ENTERED……………………………………	@GetMapping(\"/admin/home\")------");

		return "admin-home";
	}

	@GetMapping("/admin/index")
	public String handleUserWelcome() {

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println("ENTERED...................................	@GetMapping(\"/admin/index\")");
		logger.trace("ENTERED……………………………………		@GetMapping(\"/admin/index\")------");

		return "admin-index";

	}

}
