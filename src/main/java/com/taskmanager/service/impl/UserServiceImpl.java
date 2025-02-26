package com.taskmanager.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskmanager.dto.MyUserDto;
import com.taskmanager.exceptions.ActiveUserCannotBeDeletedException;
import com.taskmanager.exceptions.MyUserNotFoundException;
import com.taskmanager.exceptions.TaskNotFoundException;
import com.taskmanager.model.MyTask;
import com.taskmanager.model.MyUser;
import com.taskmanager.repository.MyUserRepository;
import com.taskmanager.service.MyUserService;

@Service
public class UserServiceImpl implements MyUserService {

	@Autowired
	PasswordEncoder passwordEncoder;

	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class.getName());

	private MyUserRepository myUserRepository;

	public UserServiceImpl(MyUserRepository myUserRepository) {
		this.myUserRepository = myUserRepository;
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

	@Override
	public MyUserDto createUser(MyUserDto myUserDto) {

		// Get list of all the users in the database
		List<MyUser> currentUserList = myUserRepository.findAll();

		myUserDto.setUserNumber(currentUserList.size() + 1);

		MyUser newUser = convertMyUserDtoToMyUser(myUserDto);

		System.out.println("newUser.toString() = " + newUser.toString());

		MyUser userSaved = myUserRepository.save(newUser);

		System.out.println("userSaved.toString() = " + userSaved.toString());

		MyUserDto newMyUserDto = mapToDto(userSaved);

		logger.trace("EXITED……………………………………registerNewUser()");

		return newMyUserDto;

	}

	public Map<Integer, Integer> getHashMap() throws MyUserNotFoundException {

		logger.trace("Entered...........................getHashMap()");

		String usernameActive = verifyLoggedInUser();

		// Will search the myUserRepository for username
		List<MyUser> myUserList = myUserRepository.findAll();

		Map<Integer, Integer> userIdAndNumber = new HashMap<Integer, Integer>();

		try {
			// Re-number the user List
			// Create new "tempTaskList" copy of "taskList"
			for (int k = 0; k < myUserList.size(); k++) {

				myUserList.get(k).setUserNumber(k + 1);

				userIdAndNumber.put(myUserList.get(k).getUserNumber(), myUserList.get(k).getId());

			}

			logger.trace("Exited...........................getHashMap()");

			return userIdAndNumber;

		} catch (MyUserNotFoundException tnfe) {
			throw new MyUserNotFoundException("User not found....");
		}
	}

	@Override
	public List<MyUserDto> deleteMyUserById(int userNumber) throws TaskNotFoundException, ActiveUserCannotBeDeletedException {
		logger.trace("Entered......deleteMyUserById() ");

		String username = verifyLoggedInUser();

		// Will search the myUserRepository for username
		List<MyUser> myUserList = myUserRepository.findAll();

		Map<Integer, Integer> userIdAndNumber = new HashMap<Integer, Integer>();

		userIdAndNumber = getHashMap();

		logger.trace("Entered...........................getMyUserById()");

		int userId = userIdAndNumber.get(userNumber);

		MyUser myUser = myUserRepository.findById(userId)
				.orElseThrow(() -> new MyUserNotFoundException("MyUser could not be deleted..."));

		if (myUserRepository.findById(userId).isPresent()) {

			if (myUser.getUsername().equals(username)) {

				throw new ActiveUserCannotBeDeletedException("Active user cannot be deleted...");
			} else {

				myUserRepository.deleteById(userId);

			}

		}

		List<MyUserDto> myUserDtoList = afterDeleteGetAllUsers();

		logger.trace("Exited......deleteMyUserById() ");

		return myUserDtoList;
	}

	public List<MyUserDto> afterDeleteGetAllUsers() throws MyUserNotFoundException {

		logger.trace("Entered...........................afterDeleteGetAllTasks()");

		// Will search the userRepository for all task according to username
		List<MyUser> userList = myUserRepository.findAll();

		// Create temperary storage for taskList from taskRepository
		List<MyUser> tempUserList = new ArrayList<>();

		// Re-number the user list
		// Create new "userTempList" copy of "userList"
		for (int k = 0; k < userList.size(); k++) {

			tempUserList.add(userList.get(k));

			tempUserList.get(k).setUserNumber(k + 1);

			updateUserNumber(tempUserList.get(k), tempUserList.get(k).getUserNumber());

		}

		try {

			MyUserDto myUserDto = new MyUserDto();

			List<MyUserDto> myUserDtoList = new ArrayList<>();

			for (int i = 0; i < userList.size(); i++) {

				myUserDto = mapToDto(userList.get(i));

				System.out.println("myTaskDto.getId() =============> " + myUserDto.getId());

				myUserDtoList.add(myUserDto);

			}

			logger.trace("Exiting...........................afterDeleteGetAllTasks()");

			return myUserDtoList;

		} catch (MyUserNotFoundException unfe) {
			throw new MyUserNotFoundException("My user not found Exception");
		}

	}

	public void updateUserNumber(MyUser myUserUpdate, int userNumber) throws MyUserNotFoundException {

		logger.trace("Entered...........................updateTask()");

		Map<Integer, Integer> userIdAndNumber = getHashMap();

		try {
			// Find the Task entity by ID or throw an exception if not found
			MyUser myUser = myUserRepository.findById(userIdAndNumber.get(userNumber))
					.orElseThrow(() -> new TaskNotFoundException("Task could not be updated"));

			// Create an updated Task entity
			MyUser updatedUser = createUserUpdateUserNumber(myUser, myUserUpdate);

			// Save the updated Task entity
			MyUser newUser = myUserRepository.save(updatedUser);

			// Map the updated Task entity to DTO and return it

			logger.trace("Exited...........................updateTask()");

		} catch (MyUserNotFoundException pde) {
			logger.trace("Exited...........................updateTask()");
			// Re-throw TaskNotFoundException with a more specific message
			throw new MyUserNotFoundException("User not found....");
		}
	}

	private MyUser createUserUpdateUserNumber(MyUser myUser, MyUser myUserUpdate) throws MyUserNotFoundException {

		logger.trace("Entered......createTaskUpdate() ");

		String username = verifyLoggedInUser();

		// Get the currrent users information
		MyUserDto myUserDto = currentUser();

		myUser.setId(myUserUpdate.getId());
		myUser.setUserNumber(myUserUpdate.getUserNumber());
		myUser.setPassword(myUserUpdate.getPassword());
		myUser.setGender(myUserUpdate.getGender());
		myUser.setRole(myUserUpdate.getRole());
		myUser.setDob(myUserUpdate.getDob());
		myUser.setEmail(myUserUpdate.getEmail());
		myUser.setUsername(myUserUpdate.getEmail());

		logger.trace("Exited......createTaskUpdate() ");

		return myUser;

	}

	public MyUserDto mapToDto(MyUser myUser) {
		System.out.println("ENTERED...................................mapToDto(" + myUser.getUsername() + ")");
		logger.trace("Entered...........................mapToDto()");

		MyUserDto newMyUserDto = new MyUserDto();
		newMyUserDto.setEmail(myUser.getUsername());
		newMyUserDto.setId(myUser.getId());
		newMyUserDto.setEmail(myUser.getEmail());
		newMyUserDto.setPassword("(Encoded)");
		newMyUserDto.setRole(myUser.getRole());
		newMyUserDto.setUserNumber(myUser.getUserNumber());
		newMyUserDto.setGender(myUser.getGender());
		newMyUserDto.setDob(myUser.getDob());
		newMyUserDto.setUsername(myUser.getUsername());

		newMyUserDto.setUserNumber(myUser.getUserNumber());
		System.out.println("EXITED...................................mapToDto(" + myUser.getUsername() + ")");
		logger.trace("Exited...........................mapToDto()");
		return newMyUserDto;
	}

	public MyUser convertMyUserDtoToMyUser(MyUserDto myUserDto) {
		System.out.println("ENTERED...................................convertMyUserDtoToMyUser(" + myUserDto.getUsername() + ")");
		logger.trace("ENTERED……………………………………convertMyUserDtoToMyUser()");

		MyUser myUser = new MyUser();

		// ... map properties from myUser to myUserDto
		// myUser.setMyUsernumber(myUserDto.getMyUsernumber());

		myUser.setUsername(myUserDto.getUsername());
		myUser.setPassword(passwordEncoder.encode(myUserDto.getPassword()));
		myUser.setUserNumber(myUserDto.getUserNumber());
		myUser.setRole(myUserDto.getRole());
		myUser.setEmail(myUserDto.getEmail());
		myUser.setDob(myUserDto.getDob());
		myUser.setGender(myUserDto.getGender());

		System.out.println("myUser.toString() = " + myUser.toString());

		System.out.println("EXITED...................................convertMyUserDtoToMyUser(" + myUserDto.getUsername() + ")");
		logger.trace("EXITED……………………………………convertMyUserDtoToMyUser()");

		return myUser;
	}

	public MyUserDto createUserUpdate(MyUserDto myUser, MyUserDto myUserDtoUpdate) {

		logger.trace("Entered......createUserUpdate() ");

		myUser.setId(myUserDtoUpdate.getId());
		myUser.setUsername(myUserDtoUpdate.getDob());
		myUser.setUsername(myUserDtoUpdate.getUsername());
		myUser.setUsername(myUserDtoUpdate.getGender());
		myUser.setUsername(myUserDtoUpdate.getRole());
		myUser.setRole(myUserDtoUpdate.getRole());

		logger.trace("Exited......createUserUpdate() ");

		return myUser;
	}

	public List<MyUserDto> findAllUsers() {
		logger.trace("Entered......findAllProducts() ");

		List<MyUser> myUserList = myUserRepository.findAll();
		logger.trace("Exited......findAllProducts() ");

		return myUserList.stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public MyUserDto getMyUserById(int id) {

		Map<Integer, Integer> userIdAndNumber = getHashMap();

		logger.trace("Entered...........................getMyUserById()");

		int userId = userIdAndNumber.get(id);

		MyUser myUser = myUserRepository.findById(userId)
				.orElseThrow(() -> new MyUserNotFoundException("User Id could not be found :("));
		logger.trace("Exited...........................getMyUserById()");
		return mapToDto(myUser);
	}

	@Override
	public MyUserDto updateMyUserDetail(MyUserDto myUserDtoUpdate, int id) throws MyUserNotFoundException {
		logger.trace("Entered...........................updateMyUser()");

		Map<Integer, Integer> userIdAndNumber = getHashMap();

		int userId = userIdAndNumber.get(id);

		MyUser myUserUpdate = convertMyUserDtoToMyUser(myUserDtoUpdate);

		try {
			// Find the MyUser entity by ID or throw an exception if not found
			MyUser myUser = myUserRepository.findById(id)
					.orElseThrow(() -> new MyUserNotFoundException("MyUser could not be found..."));

			MyUserDto myUserDto = mapToDto(myUser);

			// Create an updated MyUser entity
			MyUserDto updatedMyUserDto = createUserUpdate(myUserDto, myUserDtoUpdate);

			MyUser myUserUpdateB = convertMyUserDtoToMyUser(updatedMyUserDto);

			// Save the updated MyUser entity
			MyUser newMyUser = myUserRepository.save(myUserUpdateB);

			// Map the updated MyUser entity to DTO and return it
			logger.trace("Exited...........................updateMyUser()");

			return mapToDto(newMyUser);

		} catch (MyUserNotFoundException pde) {
			logger.trace("Exited...........................updateMyUser()");
			// Re-throw MyUserNotFoundException with a more specific message
			throw new MyUserNotFoundException("MyUser could not be updated...");
		}
	}

	@Override
	public MyUserDto currentUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		System.out.println("authentication.getPrincipal() = " + authentication.getPrincipal());

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		String username = userDetails.getUsername();

		System.out.println("username ================= " + username);

		MyUser myUser = myUserRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new MyUserNotFoundException("MyUser could not be found..."));

		return mapToDto(myUser);
	}

	@Override
	public int getActiveUserId(String username) {

		int userIdValue = 0;
		Optional<MyUser> myUser = myUserRepository.findByUsername(username);

		if (myUser.isPresent()) {
			userIdValue = myUser.get().getId();
		}

		return userIdValue;
	}

	@Override
	public List<MyUserDto> getAllMyUsersNow() {

		List<MyUser> myUserList = myUserRepository.findAll();
		List<MyUserDto> myUserDtoList = new ArrayList<>();

		for (int i = 0; i < myUserList.size(); i++) {

			myUserDtoList.add(mapToDto(myUserList.get(i)));
		}

		return myUserDtoList;
	}

	@Override
	public MyUserDto findByUsername(String username) {

		MyUser myUser = myUserRepository.findByUsername(username)
				.orElseThrow(() -> new MyUserNotFoundException("User could not be found..."));

		return mapToDto(myUser);
	}

	@Override
	public void updateUserNumber(MyTask myTaskUpdate, int taskNumber) {
		// TODO Auto-generated method stub

	}

}
