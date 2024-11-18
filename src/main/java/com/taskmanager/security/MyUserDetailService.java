package com.taskmanager.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.taskmanager.controllers.LastWord;
import com.taskmanager.model.MyUser;
import com.taskmanager.repository.MyUserRepository;

@Component
public class MyUserDetailService implements UserDetailsService {

	// public PressEnter pressEnter = new PressEnter();

	Scanner scanner = new Scanner(System.in);

	Logger logger = LoggerFactory.getLogger(MyUserDetailService.class.getName());

	@Autowired
	MyUserRepository myUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		System.out.println(getClass().getName().toUpperCase());

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println("ENTERED....................loadUserByUsername(" + username + ")");

		logger.trace("ENTERED……………………………………loadUserByUsername()");

		Optional<MyUser> myUser = myUserRepository.findByUsername(username);

		if (myUser.isPresent()) {

			// we will provide a userDetails for the user
			// We are creating a "User" instance with the MyUser "myUser" instance
			var userObj = myUser.get();

			logger.trace("EXITED……………………………………loadUserByUsername()");

			System.out.println("getRoles(userObj)[0] = " + getRoles(userObj)[0]);

			try {

				System.out.println("getRoles(userObj)[1] = " + getRoles(userObj)[1]);

			} catch (Exception e) {

				System.out.println("user role in index 1 not found.... ");

			}

			// this will build an instance of UserDetailsService
			// spring security will authenticate against this
			return User.builder().username(userObj.getUsername()).password(userObj.getPassword())
					.roles(getRoles(userObj)).build();

		} else {

			System.out.println("EXITED..................loadUserByUsername(" + username + ")");
			logger.trace("EXITED……………………………………loadUserByUsername()");

			throw new UsernameNotFoundException(username);
		}

	}

	//
	private String[] getRoles(MyUser myUser) {

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println("ENTERED...................................getRoles(" + myUser.getRole() + ")");
		logger.trace("ENTERED……………………………………getRoles()");

		// if user role is empty just return "USER"

		if (myUser.getRole() == null) {

			System.out.println("EXITED...................................getRoles(" + myUser.getRole() + ")");
			logger.trace("EXITED……………………………………getRoles()");

			return new String[] { "USER" };

		}

		String temp1 = myUser.getRole();

		temp1.split(",");

		List<String> userRolesList = new ArrayList<>();

		String[] userRoles = temp1.split(",");

		String roleAdmin = "ADMIN";

		for (int i = 0; i < userRoles.length; i++) {

			if (userRoles[i].toUpperCase().equals("ADMIN")) {

				System.out.println();
				String[] temp = { "ADMIN", "USER" };

				userRoles = temp;
				break;
			}

		}

		System.out.println("Arrays.asList(userRoles) = " + Arrays.asList(userRoles));

		userRolesList = Arrays.asList(userRoles);

		return userRoles;

	}

	// public void pressEnter(Scanner scanner) {
	//
	// System.out.println("---PRESS ENTER---");
	// scanner.nextLine();
	// }

}
