package com.taskmanager.service;

import java.util.List;

import com.taskmanager.dto.MyTaskDto;
import com.taskmanager.dto.MyUserDto;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.model.MyTask;

public interface TaskService {

	public MyTaskDto createTask(MyTaskDto taskDto, MyUserDto myUserDto);

	public TaskResponse getAllTasks(int pageNo, int pageSize);

	public MyTaskDto getTaskById(int id);

	public MyTaskDto updateTask(MyTaskDto taskDtoUpdate, int id);

	public void deleteByTaskId(int id);

	public MyTaskDto mapToDto(MyTask task);

	public List<MyTaskDto> getAllTasksObjects();

}
