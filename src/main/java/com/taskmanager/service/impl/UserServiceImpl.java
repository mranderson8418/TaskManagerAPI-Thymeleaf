package com.taskmanager.service.impl;

import java.util.List;
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

	@Override
	public MyUserDto createUser(MyUserDto myUserDto) {

		MyUser newUser = convertMyUserDtoToMyUser(myUserDto);

		newUser.setPassword(passwordEncoder.encode(myUserDto.getPassword()));

		newUser.setUsername(myUserDto.getUsername());

		newUser.setRole(myUserDto.getRole());

		myUserRepository.save(newUser);
		System.out.println("ENTERED...................................createUser(" +
				myUserDto.getUsername() + ")");
		logger.trace("EXITED……………………………………registerNewUser()");

		MyUserDto newMyUserDto = mapToDto(newUser);

		return newMyUserDto;

	}

	public MyUserDto mapToDto(MyUser myUser) {
		System.out.println(
				"ENTERED...................................mapToDto(" + myUser.getUsername() + ")");
		logger.trace("Entered...........................mapToDto()");

		MyUserDto newMyUserDto = new MyUserDto();
		newMyUserDto.setId(myUser.getId());
		newMyUserDto.setEmail(myUser.getEmail());
		newMyUserDto.setPassword("(Encoded)");
		newMyUserDto.setRole(myUser.getRole());
		newMyUserDto.setGender(myUser.getGender());
		newMyUserDto.setDob(myUser.getDob());
		newMyUserDto.setUsername(myUser.getUsername());
		System.out.println(
				"EXITED...................................mapToDto(" + myUser.getUsername() + ")");
		logger.trace("Exited...........................mapToDto()");
		return newMyUserDto;
	}

	public MyUser convertMyUserDtoToMyUser(MyUserDto myUserDto) {
		System.out.println("ENTERED...................................convertMyUserDtoToMyUser(" +
				myUserDto.getUsername() + ")");
		logger.trace("ENTERED……………………………………convertMyUserDtoToMyUser()");

		MyUser myUser = new MyUser();

		// ... map properties from myUser to myUserDto

		myUser.setUsername(myUserDto.getUsername());
		myUser.setPassword(myUserDto.getPassword());
		myUser.setRole(myUserDto.getRole());
		myUser.setEmail(myUserDto.getEmail());
		myUser.setDob(myUserDto.getDob());
		myUser.setGender(myUserDto.getGender());
		System.out.println("EXITED...................................convertMyUserDtoToMyUser(" +
				myUserDto.getUsername() + ")");
		logger.trace("EXITED……………………………………convertMyUserDtoToMyUser()");

		return myUser;
	}

	public MyUserDto createUserUpdate(MyUserDto myUser, MyUserDto myUserDtoUpdate) {

		logger.trace("Entered......createUserUpdate() ");

		myUser.setId(myUserDtoUpdate.getId());
		myUser.setUsername(myUserDtoUpdate.getUsername());
		myUser.setUsername(myUserDtoUpdate.getUsername());
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

		List<MyUserDto> content = myUserDtoList.stream().map(this::mapToDto)
				.collect(Collectors.toList());

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
		logger.trace("Entered...........................getMyUserById()");

		MyUser myUser = myUserRepository.findById(id)
				.orElseThrow(() -> new MyUserNotFoundException("User Id could not be found :("));
		logger.trace("Exited...........................getMyUserById()");
		return mapToDto(myUser);
	}

	@Override
	public MyUserDto updateMyUserDetail(MyUserDto myUserDtoUpdate, int id)
			throws MyUserNotFoundException {
		logger.trace("Entered...........................updateMyUser()");

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

		MyUser myUser = myUserRepository.findById(id)
				.orElseThrow(() -> new MyUserNotFoundException("MyUser could not be deleted..."));

		if (myUserRepository.findById(id).isPresent()) {

			if (myUser.getUsername().equals(activeUsername)) {
				throw new ActiveUserCannotBeDeletedException("Active user cannot be deleted...");
			} else {

				myUserRepository.deleteById(id);
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
