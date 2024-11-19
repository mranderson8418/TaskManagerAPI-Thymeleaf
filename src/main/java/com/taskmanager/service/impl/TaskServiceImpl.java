package com.taskmanager.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.taskmanager.dto.MyTaskDto;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.exceptions.MyUserNotFoundException;
import com.taskmanager.exceptions.TaskNotFoundException;
import com.taskmanager.model.MyTask;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.service.TaskService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TaskServiceImpl implements TaskService {

	Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class.getName());

	HttpServletRequest request;

	private TaskRepository taskRepository;

	public TaskServiceImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	private MyTaskDto convertToDto(MyTask task) {

		logger.trace("ENTERED……………………………………convertToDto()");

		MyTaskDto taskDto = new MyTaskDto();

		// ... map properties from task to dto

		taskDto.setUsername(task.getUsername());

		taskDto.setContent(task.getContent());
		taskDto.setComplete(task.isComplete());
		logger.trace("EXITED……………………………………convertToDto()");

		return taskDto;
	}

	@Override
	public MyTaskDto createTask(MyTaskDto myTaskDto) {
		logger.trace("Entered......createTask() ");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		System.out.println("authentication.getPrincipal() = " + authentication.getPrincipal());

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();

		System.out.println("username =================" + username);

		MyTask task = new MyTask();

		task.setUsername(username);
		task.setContent(myTaskDto.getContent());
		task.setComplete(myTaskDto.isComplete());

		MyTask newTask = taskRepository.save(task);

		logger.trace("Exited......createTask() ");

		return mapToDto(newTask);
	}

	public MyTask createTaskUpdate(MyTask task, MyTask taskUpdate) {
		logger.trace("Entered......createTaskUpdate() ");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		System.out.println("authentication.getPrincipal() = " + authentication.getPrincipal());

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		System.out.println("username = " + username);

		task.setUsername(username);
		task.setContent(taskUpdate.getContent());
		task.setComplete(taskUpdate.isComplete());
		logger.trace("Exited......createTaskUpdate() ");

		return task;
	}

	@Override
	public void deleteByTaskId(int id) {
		logger.trace("Entered......deleteByTaskId() ");

		MyTask task = taskRepository.findById(id)
				.orElseThrow(() -> new MyUserNotFoundException("Task with id = " + id + " could not be deleted..."));

		if (taskRepository.findById(id).isPresent()) {
			taskRepository.deleteById(id);
		}

		logger.trace("Exited......deleteByTaskId() ");

	}

	@Override
	public TaskResponse getAllTasks(int pageNo, int pageSize) {

		// Get Authentication from the security context
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// from the UserDetails get the "principal" or user currently logged in
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		// Get user name from the userDetails
		String username = userDetails.getUsername();

		logger.trace("Entered...........................getAllTasks()");

		// Insert parameters for pageNo and PageSize into PageRequest Object
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize);

		// Will search the taskRepository for all task according to username &
		// pageRequest
		Page<MyTask> task = taskRepository.findAllTasksByUsername(username, pageRequest);

		// this "task.getContent()" will get everything in the page
		List<MyTask> myTaskList = task.getContent();

		// List<MyTaskDto> myTaskDtoList =
		// taskList.stream().map(this::mapToDto).collect(Collectors.toList());

		TaskResponse taskResponse = new TaskResponse();

		taskResponse.setContent(task.getContent());
		taskResponse.setPageNo(task.getNumber());
		taskResponse.setPageSize(task.getSize());
		taskResponse.setTotalElements(task.getTotalElements());
		taskResponse.setTotalPages(task.getTotalPages());
		taskResponse.setLast(task.isLast());
		logger.trace("Exited...........................getAllTasks()");
		return taskResponse;
	}

	@Override
	public MyTaskDto getTaskById(int id) {
		logger.trace("Entered.................getTaskById()");

		MyTask task = taskRepository.findById(id)
				.orElseThrow(() -> new TaskNotFoundException("Print drawing could not be found :("));
		logger.trace("Exited...........................getTaskById()");
		return mapToDto(task);
	}

	private MyTask convertToTask(MyTaskDto myTaskDto) {
		logger.trace("Entered...........................mapToDto()");

		MyTask task = new MyTask();

		task.setId(myTaskDto.getId());
		task.setComplete(myTaskDto.isComplete());
		task.setContent(myTaskDto.getContent());
		task.setUsername(myTaskDto.getUsername());

		logger.trace("Exited...........................mapToDto()");
		return task;
	}

	public MyTaskDto mapToDto(MyTask task) {
		logger.trace("Entered...........................mapToDto()");

		MyTaskDto taskDto = new MyTaskDto();

		taskDto.setId(task.getId());
		taskDto.setComplete(task.isComplete());
		taskDto.setContent(task.getContent());
		taskDto.setUsername(task.getUsername());

		logger.trace("Exited...........................mapToDto()");
		return taskDto;

	}

	@Override
	public MyTaskDto updateTask(MyTaskDto taskDtoUpdate, int id) throws TaskNotFoundException {
		logger.trace("Entered...........................updateTask()");

		MyTask myTaskUpdate = convertToTask(taskDtoUpdate);

		try {
			// Find the Task entity by ID or throw an exception if not found
			MyTask task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task could not be updated"));

			// Create an updated Task entity
			MyTask updatedTask = createTaskUpdate(task, myTaskUpdate);

			// Save the updated Task entity
			MyTask newTask = taskRepository.save(updatedTask);

			// Map the updated Task entity to DTO and return it
			logger.trace("Exited...........................updateTask()");

			return mapToDto(newTask);

		} catch (TaskNotFoundException pde) {
			logger.trace("Exited...........................updateTask()");
			// Re-throw TaskNotFoundException with a more specific message
			throw new TaskNotFoundException("Task  could not be updated--OOp");
		}
	}

}
