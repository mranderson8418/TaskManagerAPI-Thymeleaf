package com.taskmanager.service;

import java.util.List;
import java.util.Map;

import com.taskmanager.dto.MyTaskDto;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.exceptions.TaskNotFoundException;
import com.taskmanager.model.MyTask;

public interface TaskService {

	public MyTaskDto createTask(MyTaskDto taskDto);

	public TaskResponse getAllTasks(int pageNo, int pageSize);

	public MyTaskDto getTaskById(int id);

	public MyTaskDto updateTask(MyTaskDto taskDtoUpdate, int id);

	public void deleteByTaskId(int id);

	public MyTaskDto mapToDto(MyTask task);

	public List<MyTaskDto> getAllTasksObjects();

	public String verifyLoggedInUser();

	/**
	 * @param id
	 * @throws TaskNotFoundException
	 */
	void deleteAllTasks() throws TaskNotFoundException;

	void deleteAllTasks(Map<Integer, Integer> taskIdAndNumber) throws TaskNotFoundException;

}
