package com.taskmanager.service;

import java.util.List;

import com.taskmanager.dto.MyUserDto;
import com.taskmanager.model.MyTask;
import com.taskmanager.model.MyUser;

public interface MyUserService {

	public MyUserDto createUser(MyUserDto myUserDto);

	public List<MyUserDto> deleteMyUserById(int id);

	public List<MyUserDto> getAllMyUsersNow();

	public MyUserDto getMyUserById(int id);

	public MyUserDto updateMyUserDetail(MyUserDto myUserDtoUpdate, int id);

	public MyUserDto currentUser();

	public MyUser convertMyUserDtoToMyUser(MyUserDto myUserDto);

	public MyUserDto mapToDto(MyUser myUser);

	public int getActiveUserId(String username);

	public List<MyUserDto> afterDeleteGetAllUsers();

	public MyUserDto findByUsername(String username);

	public void updateUserNumber(MyTask myTaskUpdate, int taskNumber);

}
