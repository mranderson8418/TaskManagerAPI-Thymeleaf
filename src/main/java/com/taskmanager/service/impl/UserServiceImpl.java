package com.taskmanager.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskmanager.dto.MyUserDto;
import com.taskmanager.dto.MyUserResponse;
import com.taskmanager.exceptions.ActiveUserCannotBeDeletedException;
import com.taskmanager.exceptions.MyUserNotFoundException;
import com.taskmanager.exceptions.TaskNotFoundException;
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

		MyUser newUser = convertMyUserDtoToMyUser(myUserDto);

		newUser.setPassword(passwordEncoder.encode(myUserDto.getPassword()));

		newUser.setUsername(myUserDto.getUsername());

		newUser.setRole(myUserDto.getRole());

		myUserRepository.save(newUser);
		System.out.println("ENTERED...................................createUser(" + myUserDto.getUsername() + ")");
		logger.trace("EXITED……………………………………registerNewUser()");

		MyUserDto newMyUserDto = mapToDto(newUser);

		return newMyUserDto;

	}

	public Map<Integer, Integer> getHashMap() {

		logger.trace("Entered...........................getHashMap()");

		String usernameActive = verifyLoggedInUser();

		// Will search the myUserRepository for username
		List<MyUser> myUserList = myUserRepository.findAll();

		Map<Integer, Integer> userIdAndNumber = new HashMap<Integer, Integer>();

		try {
			// Re-number the user List
			// Create new "tempTaskList" copy of "taskList"
			for (int k = 0; k < myUserList.size(); k++) {

				myUserList.get(k).setUsernumber(k + 1);

				userIdAndNumber.put(myUserList.get(k).getUserNumber(), myUserList.get(k).getId());

			}

			logger.trace("Exited...........................getHashMap()");

			return userIdAndNumber;

		} catch (TaskNotFoundException tnfe) {
			throw new TaskNotFoundException("Task Could not be found");
		}

	}

	public MyUserDto mapToDto(MyUser myUser) {
		System.out.println("ENTERED...................................mapToDto(" + myUser.getUsername() + ")");
		logger.trace("Entered...........................mapToDto()");

		MyUserDto newMyUserDto = new MyUserDto();

		newMyUserDto.setId(myUser.getId());
		newMyUserDto.setEmail(myUser.getEmail());
		newMyUserDto.setPassword("(Encoded)");
		newMyUserDto.setRole(myUser.getRole());
		newMyUserDto.setUserDtoNumber(myUser.getUserNumber());
		newMyUserDto.setGender(myUser.getGender());
		newMyUserDto.setDob(myUser.getDob());
		newMyUserDto.setUsername(myUser.getUsername());

		System.out.println("EXITED...................................mapToDto(" + myUser.getUsername() + ")");
		logger.trace("Exited...........................mapToDto()");
		return newMyUserDto;
	}

	public MyUser convertMyUserDtoToMyUser(MyUserDto myUserDto) {
		System.out.println(
				"ENTERED...................................convertMyUserDtoToMyUser(" + myUserDto.getUsername() + ")");
		logger.trace("ENTERED……………………………………convertMyUserDtoToMyUser()");

		MyUser myUser = new MyUser();

		// ... map properties from myUser to myUserDto
		// myUser.setMyUsernumber(myUserDto.getMyUsernumber());
		myUser.setUsername(myUserDto.getUsername());
		myUser.setPassword(myUserDto.getPassword());
		myUser.setUsernumber(myUserDto.getUserDtoNumber());
		myUser.setRole(myUserDto.getRole());
		myUser.setEmail(myUserDto.getEmail());
		myUser.setDob(myUserDto.getDob());
		myUser.setGender(myUserDto.getGender());

		System.out.println(
				"EXITED...................................convertMyUserDtoToMyUser(" + myUserDto.getUsername() + ")");
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

	public MyUserResponse getAllMyUsers(int pageNo, int pageSize) {

		logger.trace("Entered...........................getAllMyUsers()");

		PageRequest pageable = PageRequest.of(pageNo, pageSize);

		Page<MyUser> myUser = myUserRepository.findAll(pageable);

		List<MyUser> myUserDtoList = myUser.getContent();

		List<MyUserDto> content = myUserDtoList.stream().map(this::mapToDto).collect(Collectors.toList());

		MyUserResponse myUserDtoResponse = new MyUserResponse();

		myUserDtoResponse.setUsername(content);
		myUserDtoResponse.setPageNo(myUser.getNumber());
		myUserDtoResponse.setPageSize(myUser.getSize());
		myUserDtoResponse.setTotalElements(myUser.getTotalElements());
		myUserDtoResponse.setTotalPages(myUser.getTotalPages());
		myUserDtoResponse.setLast(myUser.isLast());
		logger.trace("Exited...........................getAllMyUsers()");
		return myUserDtoResponse;

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
	public void deleteMyUserById(int id) {
		logger.trace("Entered......deleteMyUserById() ");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		String activeUsername = userDetails.getUsername();

		Map<Integer, Integer> userIdAndNumber = getHashMap();

		logger.trace("Entered...........................getMyUserById()");

		int userId = userIdAndNumber.get(id);

		MyUser myUser = myUserRepository.findById(userId)
				.orElseThrow(() -> new MyUserNotFoundException("MyUser could not be deleted..."));

		if (myUserRepository.findById(userId).isPresent()) {

			if (myUser.getUsername().equals(activeUsername)) {
				throw new ActiveUserCannotBeDeletedException("Active user cannot be deleted...");
			} else {

				myUserRepository.deleteById(userId);
			}
		}

		logger.trace("Exited......deleteMyUserById() ");
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

}
