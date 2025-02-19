package com.taskmanager.service;

import java.util.List;

import com.taskmanager.dto.MyUserDto;
import com.taskmanager.model.MyUser;

public interface MyUserService {

	public MyUserDto createUser(MyUserDto myUserDto);

	public void deleteMyUserById(int id);

	public List<MyUserDto> getAllMyUsersNow();

	public MyUserDto getMyUserById(int id);

	public MyUserDto updateMyUserDetail(MyUserDto myUserDtoUpdate, int id);

	public MyUserDto currentUser();

	public MyUser convertMyUserDtoToMyUser(MyUserDto myUserDto);

	public MyUserDto mapToDto(MyUser myUser);

	public int getActiveUserId(String username);

	public MyUserDto findByUsername(String username);

}
