package com.taskmanager.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.taskmanager.controllers.LastWord;
import com.taskmanager.dto.MyTaskDto;
import com.taskmanager.dto.MyUserDto;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.exceptions.EmailNotMaskedAppropriately;
import com.taskmanager.exceptions.MyUserNotFoundException;
import com.taskmanager.exceptions.TaskNotFoundException;
import com.taskmanager.model.MyTask;
import com.taskmanager.repository.MyUserRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.service.MyUserService;
import com.taskmanager.service.TaskService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	MyUserService myUserService;

	Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class.getName());

	HttpServletRequest request;

	private TaskRepository taskRepository;

	private MyUserRepository myUserRepository;

	public TaskServiceImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	// heyheyhey heyhey
	private MyTaskDto convertToDto(MyTask task) {
		System.out.println("\n\n");
		System.out.println(nameClass());
		System.out.println("Entered...........................convertToDto()");

		try {

			logger.trace("ENTERED……………………………………convertToDto()");
			MyTaskDto taskDto = new MyTaskDto();
			// ... map properties from task to dto
			taskDto.setId(task.getId());
			taskDto.setTaskNumber(task.getTaskNumber());
			taskDto.setUsername(task.getUsername());
			taskDto.setContent(task.getContent());
			taskDto.setComplete(task.isComplete());
			logger.trace("EXITED……………………………………convertToDto()");
			System.out.println("Exited...........................convertToDto()");
			System.out.println("\n\n");
			return taskDto;

		} catch (MyUserNotFoundException unfe) {
			throw new MyUserNotFoundException("User not found exception");
		}
	}

	public String maskEmail(String email) {

		if (email == null || !email.contains("@")) {
			throw new IllegalArgumentException("Invalid email format");
		}

		String[] myArray = email.split("@");

		String str2[] = new String[2];
		int i = 0;
		for (String x : myArray) {
			str2[i] = x;
			i++;

		}

		System.out.println(str2[1]);

		String str1 = email.substring(0, 3);

		String str3 = "***";
		System.out.println("str1 + str3 + str2[1].substring(5, 9);");
		// PressEnter.pressEnter();
		return str1 + str3 + str2[1].substring(5, 9);
	}

	@Override
	public MyTaskDto createTask(MyTaskDto myTaskDto) {

		String usernameActive = verifyLoggedInUser();
		logger.trace("Entered......createTask() ");

		// Get list of all task the User has in the database
		List<MyTask> currentUserTaskList = taskRepository.findAllTasksByUsernameObjectList(usernameActive);

		myTaskDto.setTaskNumber(currentUserTaskList.size() + 1);

		MyTask task = new MyTask();

		task.setId(myTaskDto.getId());
		task.setTaskNumber(myTaskDto.getTaskNumber());
		task.setUsername(usernameActive);
		task.setContent(myTaskDto.getContent());
		task.setComplete(myTaskDto.isComplete());

		MyTask newTask = taskRepository.save(task);

		logger.trace("Exited......createTask() ");

		return mapToDto(newTask);
	}

	@Override
	public MyTaskDto updateTask(MyTaskDto taskDtoUpdate, int taskNumber) throws TaskNotFoundException {

		logger.trace("Entered...........................updateTask()");

		Map<Integer, Integer> taskIdAndNumber = getHashMap();

		MyTask myTaskUpdate = convertToTask(taskDtoUpdate);

		try {
			int taskId = taskIdAndNumber.get(taskNumber);
		} catch (TaskNotFoundException pde) {
			logger.trace("Exited...........................updateTask()");
			// Re-throw TaskNotFoundException with a more specific message
			throw new TaskNotFoundException("Task  could not be found");
		} catch (NullPointerException npe) {
			throw new NullPointerException("Task does not exists");
		}

		int taskId = taskIdAndNumber.get(taskNumber);
		// Find the Task entity by ID or throw an exception if not found
		MyTask task = taskRepository.findById(taskId)
				.orElseThrow(() -> new TaskNotFoundException("Task could not be updated"));

		// Create an updated Task entity
		MyTask updatedTask = createTaskUpdate(task, myTaskUpdate);

		// Save the updated Task entity
		MyTask newTask = taskRepository.save(updatedTask);

		// Map the updated Task entity to DTO and return it

		logger.trace("Exited...........................updateTask()");

		return mapToDto(newTask);

	}

	public MyTask createTaskUpdate(MyTask task, MyTask taskUpdate) {

		logger.trace("Entered......createTaskUpdate() ");

		String username = verifyLoggedInUser();

		System.out.println("username = " + username);

		// Get the currrent users information
		MyUserDto myUserDto = myUserService.currentUser();

		// if the task.username = currentUser.username then continue
		if (task.getUsername().equals(myUserDto.getUsername())) {

			task.setUsername(username);

			if (!taskUpdate.getContent().isBlank()) {

				task.setContent(taskUpdate.getContent());

			}

			task.setComplete(taskUpdate.isComplete());

			logger.trace("Exited......createTaskUpdate() ");

			return task;
		} else {
			throw new TaskNotFoundException("Task id not found");
		}

	}

	public String verifyLoggedInUser() {
		System.out.println("\n\n");
		System.out.println(nameClass());

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

	public Map<Integer, Integer> getHashMap() {

		System.out.println("\n\n");
		System.out.println(nameClass());
		System.out.println("Entered...........................getHashMap()");

		logger.trace("Entered...........................getHashMap()");

		String usernameActive = verifyLoggedInUser();

		// Will search the taskRepository for all task according to username
		List<MyTask> taskList = taskRepository.findAllTasksByUsernameObjectList(usernameActive);

		Map<Integer, Integer> taskIdAndNumber = new HashMap<Integer, Integer>();

		try {
			// Re-number the task list
			// Create new "tempTaskList" copy of "taskList"
			for (int k = 0; k < taskList.size(); k++) {

				taskList.get(k).setTaskNumber(k + 1);

				taskIdAndNumber.put(taskList.get(k).getTaskNumber(), taskList.get(k).getId());

			}

			logger.trace("Exited...........................getHashMap()");

			return taskIdAndNumber;

		} catch (TaskNotFoundException tnfe) {
			throw new TaskNotFoundException("Task Could not be found");
		}

	}

	@Override
	public TaskResponse getAllTasks(int pageNo, int pageSize) {
		System.out.println("\n\n");
		System.out.println(nameClass());
		System.out.println("Entered...........................getAllTasks()");
		logger.trace("Entered...........................getAllTasks()");

		String usernameActive = verifyLoggedInUser();

		// Insert parameters for pageNo and PageSize into PageRequest Object
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize);

		// Will search the taskRepository for all task according to username &
		// pageRequest
		Page<MyTask> task = taskRepository.findAllTasksByUsername(usernameActive, pageRequest);

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

		System.out.println("Exited...........................getAllTasks()");
		System.out.println("\n\n");
		logger.trace("Exited...........................getAllTasks()");

		return taskResponse;

	}

	@SuppressWarnings("null")
	@Override
	public List<MyTaskDto> getAllTasksObjects() {

		logger.trace("Entered...........................getAllTasksObjects()");

		String username = verifyLoggedInUser();

		// Will search the taskRepository for all task according to username
		List<MyTask> taskList = taskRepository.findAllTasksByUsernameObjectList(username);

		try {
			// Optional<MyUser> myUser = myUserRepository.findByUsername(username);

			MyTaskDto myTaskDto = new MyTaskDto();

			List<MyTaskDto> myTaskDtoList = new ArrayList<>();

			logger.trace("Exiting...........................getAllTasks()");

			try {
				for (int i = 0; i < taskList.size(); i++) {

					String newUsername = maskEmail(taskList.get(i).getUsername());

					taskList.get(i).setUsername(newUsername);
					myTaskDto = convertToDto(taskList.get(i));

					myTaskDtoList.add(myTaskDto);

				}

				return myTaskDtoList;

			} catch (EmailNotMaskedAppropriately cnme) {
				throw new EmailNotMaskedAppropriately("Email was not masked properly");
			}

		} catch (

		MyUserNotFoundException unfe) {
			throw new MyUserNotFoundException("My user not found Exception");
		}

	}

	@Override
	public void deleteByTaskId(int taskNumber) throws TaskNotFoundException {

		logger.trace("Entered......deleteByTaskId() ");

		String username = verifyLoggedInUser();

		Map<Integer, Integer> taskIdAndNumber = getHashMap();

		try {
			int taskId = taskIdAndNumber.get(taskNumber);

			Optional<MyTask> foundTask = taskRepository.findById(taskIdAndNumber.get(taskNumber));

			taskRepository.deleteById(taskIdAndNumber.get(taskNumber));
			System.out.println("Exited...........................deleteByTaskId()");
			System.out.println("\n\n");
			logger.trace("Exited......deleteByTaskId() ");
		} catch (TaskNotFoundException pde) {
			logger.trace("Exited...........................updateTask()");
			// Re-throw TaskNotFoundException with a more specific message
			throw new TaskNotFoundException("Task could not be found");
		} catch (NullPointerException npe) {
			throw new NullPointerException("Task does not exists with taskNumber = " + taskNumber);
		}

	}

	@Override
	public List<MyTaskDto> afterDeleteGetAllTasks() throws TaskNotFoundException {

		logger.trace("Entered...........................afterDeleteGetAllTasks()");

		String usernameActive = verifyLoggedInUser();

		// Will search the taskRepository for all task according to username
		List<MyTask> taskList = taskRepository.findAllTasksByUsernameObjectList(usernameActive);

		// Create temperary storage for taskList from taskRepository
		List<MyTask> tempTaskList = new ArrayList<>();

		// Map < taskNumber , id >
		Map<Integer, Integer> taskIdAndNumber = new HashMap<Integer, Integer>();

		for (int j = 0; j < taskList.size(); j++) {

			taskIdAndNumber.put(taskList.get(j).getTaskNumber(), taskList.get(j).getId());

			System.out.println(
					"taskIdAndNumber ==> (" + taskList.get(j).getTaskNumber() + ", " + taskList.get(j).getId() + ")");
		}

		// Re-number the task list
		// Create new "tempTaskList" copy of "taskList"
		for (int k = 0; k < taskList.size(); k++) {

			tempTaskList.add(taskList.get(k));

			tempTaskList.get(k).setTaskNumber(k + 1);

			updateTaskNumber(tempTaskList.get(k), tempTaskList.get(k).getTaskNumber());

		}

		try {

			MyTaskDto myTaskDto = new MyTaskDto();

			List<MyTaskDto> myTaskDtoList = new ArrayList<>();

			for (int i = 0; i < taskList.size(); i++) {

				myTaskDto = convertToDto(taskList.get(i));

				System.out.println("myTaskDto.getId() =============> " + myTaskDto.getId());

				myTaskDtoList.add(myTaskDto);

			}

			logger.trace("Exiting...........................afterDeleteGetAllTasks()");

			return myTaskDtoList;

		} catch (MyUserNotFoundException unfe) {
			throw new MyUserNotFoundException("My user not found Exception");
		}

	}

	public void updateTaskNumber(MyTask myTaskUpdate, int taskNumber) throws TaskNotFoundException {

		logger.trace("Entered...........................updateTask()");

		Map<Integer, Integer> taskIdAndNumber = getHashMap();

		try {
			// Find the Task entity by ID or throw an exception if not found
			MyTask task = taskRepository.findById(taskIdAndNumber.get(taskNumber))
					.orElseThrow(() -> new TaskNotFoundException("Task could not be updated"));

			// Create an updated Task entity
			MyTask updatedTask = createTaskUpdateTaskNumber(task, myTaskUpdate);

			// Save the updated Task entity
			MyTask newTask = taskRepository.save(updatedTask);

			// Map the updated Task entity to DTO and return it

			logger.trace("Exited...........................updateTask()");

		} catch (TaskNotFoundException pde) {
			logger.trace("Exited...........................updateTask()");
			// Re-throw TaskNotFoundException with a more specific message
			throw new TaskNotFoundException("Task  could not be updated--OOp");
		}
	}

	public MyTask createTaskUpdateTaskNumber(MyTask task, MyTask taskUpdate) {

		logger.trace("Entered......createTaskUpdate() ");

		String username = verifyLoggedInUser();

		// Get the currrent users information
		MyUserDto myUserDto = myUserService.currentUser();

		// if the task.username = currentUser.username then continue
		if (task.getUsername().equals(myUserDto.getUsername())) {

			task.setId(taskUpdate.getId());
			task.setTaskNumber(taskUpdate.getTaskNumber());
			task.setUsername(username);
			task.setContent(taskUpdate.getContent());
			task.setComplete(taskUpdate.isComplete());

			logger.trace("Exited......createTaskUpdate() ");

			return task;
		} else {
			throw new TaskNotFoundException("Task id not found");
		}

	}

	@Override
	public MyTaskDto getTaskById(int id) {
		System.out.println("\n\n");
		System.out.println(nameClass());
		logger.trace("Entered.................getTaskById()");

		MyTask task = taskRepository.findById(id)
				.orElseThrow(() -> new TaskNotFoundException("Print drawing could not be found :("));
		logger.trace("Exited...........................getTaskById()");
		System.out.println("\n\n");
		return mapToDto(task);
	}

	private MyTask convertToTask(MyTaskDto myTaskDto) {
		System.out.println("\n\n");
		System.out.println(nameClass());
		logger.trace("Entered...........................convertToTask()");

		MyTask task = new MyTask();

		task.setId(myTaskDto.getId());
		task.setTaskNumber(myTaskDto.getTaskNumber());
		task.setComplete(myTaskDto.isComplete());
		task.setContent(myTaskDto.getContent());
		task.setUsername(myTaskDto.getUsername());

		logger.trace("Exited...........................convertToTask()");
		System.out.println("\n\n");
		return task;
	}

	public MyTaskDto mapToDto(MyTask task) {
		System.out.println("\n\n");
		System.out.println(nameClass());
		logger.trace("Entered...........................mapToDto()");

		MyTaskDto taskDto = new MyTaskDto();

		taskDto.setId(task.getId());
		taskDto.setTaskNumber(task.getTaskNumber());
		taskDto.setComplete(task.isComplete());
		taskDto.setContent(task.getContent());
		taskDto.setUsername(task.getUsername());

		System.out.println("task.getTaskNumber() ----------------------------------- " + task.getTaskNumber());
		logger.trace("Exited...........................mapToDto()");
		System.out.println("\n\n");
		return taskDto;

	}

	private String nameClass() {

		LastWord lastWord = new LastWord(getClass().getName());

		return "Class = " + lastWord.getLastWord();
	}

	@Override
	public MyTask createTaskUpdateAfterDelete() {
		// TODO Auto-generated method stub
		return null;
	}

}