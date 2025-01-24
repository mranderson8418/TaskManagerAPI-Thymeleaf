package com.taskmanager.controllers;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taskmanager.dto.MyTaskDto;
import com.taskmanager.dto.MyUserDto;
import com.taskmanager.exceptions.TaskNotFoundException;
import com.taskmanager.model.MyTask;
import com.taskmanager.repository.MyUserRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.security.MyUserDetailService;
import com.taskmanager.service.MyUserService;
import com.taskmanager.service.TaskService;

@Controller
public class TaskControllerDynamicAdmin {

	// The AuthenticationManager will help us authenticate by username and
	// password

	private static int taskCount = 0;

	// Initialize a logger for the class
	Logger logger = LoggerFactory.getLogger(TaskControllerDynamicAdmin.class.getName());

	@Autowired
	private MyUserDetailService myUserDetailService;

	@Autowired
	private MyUserService myUserService;

	@Autowired
	private MyUserRepository myUserRepository;

	@Autowired
	private TaskRepository myTaskRepository;

	@Autowired
	private TaskService taskService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	public static final Collection<? extends GrantedAuthority> role = null;

	public TaskControllerDynamicAdmin(
			TaskService taskService,
			MyUserRepository myUserRepository,
			TaskRepository myTaskRepository,
			PasswordEncoder passwordEncoder) {
		this.taskService = taskService;
		this.myTaskRepository = myTaskRepository;
		this.myTaskRepository = myTaskRepository;
		this.passwordEncoder = passwordEncoder;

	}

	@GetMapping({ "/user/create/task", "/admin/create/task" })
	public String createTask(Model model) {

		model.addAttribute("myUserDto", new MyUserDto());

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());

		System.out.println(
				"ENTERED...................................	@GetMapping(\"/admin/create/task\")");
		logger.trace("ENTERED……………………………………		@GetMapping(\"/admin/create/task\")------");

		model.addAttribute("myTaskDto", new MyTaskDto());

		MyUserDto myUserDto = myUserService.currentUser();

		System.out.println("ROLE ===== " + myUserDto.getRole());

		if (myUserDto.getRole().contains("ADMIN")) {

			return "admin-create-task";
		}
		return "user-create-task";

	}

	public Collection<? extends GrantedAuthority> getAuthorityOfUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		// this is called a bounded generic
		// it is a list of something that extends the GrantedAuthority class
		Collection<? extends GrantedAuthority> role = userDetails.getAuthorities();

		return role;
	}

	@PostMapping({ "/user/create/task", "/admin/create/task" })
	// @ModelAttribute - this will bind the annotated object with the model class
	public String createTaskNewPost(@ModelAttribute MyTaskDto myTaskDto, Model model) {

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());

		logger.trace("ENTERED……………………………………		@PostMapping(\"/admin/create/task\")------");

		// task is inserted into the TaskRepository
		MyTaskDto task_inserted = taskService.createTask(myTaskDto);

		model.addAttribute("id", task_inserted.getId());
		model.addAttribute("username", task_inserted.getUsername());
		model.addAttribute("content", task_inserted.getContent());
		model.addAttribute("complete", task_inserted.isComplete());

		System.out.println("Task # " + task_inserted.getId() + " is added to the database");

		Collection<? extends GrantedAuthority> role = getAuthorityOfUser();

		for (GrantedAuthority authority : role) {

			if (authority.getAuthority().equals("ROLE_ADMIN")) {
				return "admin-new-task-created";
			}
		}
		return "user-new-task-created";
	}

	// http://localhost:8080/admin/update/task
	@GetMapping({ "/user/update/task", "/admin/update/task" })
	public String updateTask(@ModelAttribute MyTaskDto myTaskDto, Model model) {

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());

		System.out.println(
				"ENTERED...................................		@GetMapping(\"/admin/updateTask\")");
		logger.trace("ENTERED……………………………………			@GetMapping(\"/admin/updateTask\")------");

		List<MyTaskDto> myTaskDtoList = taskService.getAllTasksObjects();

		model.addAttribute("myUserDto", new MyUserDto());

		model.addAttribute("myTaskDto", new MyTaskDto());

		model.addAttribute("tasks", myTaskDtoList);
		MyUserDto myUserDto = myUserService.currentUser();

		System.out.println("ROLE ===== " + myUserDto.getRole());

		if (myUserDto.getRole().contains("ADMIN")) {

			return "admin-update-task";
		}
		return "user-update-task";

	}

	@PostMapping({ "/user/update/task", "/admin/update/task" })
	// @ModelAttribute - this will bind the annotated object with the model class
	public String updateTaskNew(@ModelAttribute MyTaskDto myTaskDto, Model model) {

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println(
				"ENTERED...................................		@PostMapping(\"/admin/updateTask\")");
		logger.trace("ENTERED……………………………………		@PostMapping(\"/admin/updateTask\")------");

		try {
			// task is inserted into the TaskRepository
			MyTaskDto task_inserted = taskService.updateTask(myTaskDto, myTaskDto.getId());

			model.addAttribute("id", task_inserted.getId());
			model.addAttribute("username", task_inserted.getUsername());
			model.addAttribute("content", task_inserted.getContent());
			model.addAttribute("complete", task_inserted.isComplete());

			System.out.println("Task # " + task_inserted.getId() + " is updated in the database");
			MyUserDto myUserDto = myUserService.currentUser();

			System.out.println("ROLE ===== " + myUserDto.getRole());

			if (myUserDto.getRole().contains("ADMIN")) {

				return "redirect:/admin/taskList";
			}
			return "redirect:/user/taskList";

		} catch (TaskNotFoundException tnfe) {
			throw new TaskNotFoundException("unable to find task with id");
		}

	}

	@GetMapping({ "/user/delete/task", "/admin/delete/task" })
	public String getDeleteTask(@ModelAttribute MyTaskDto myTaskDto, Model model) {

		// Validate object data if necessary
		// Save object to database
		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		logger.trace("ENTERED……………………………………	@GetMapping(\"/admin/deleteTask\")");
		logger.trace("EXITED……………………………………	@GetMapping(\"/admin/deleteTask\")");

		List<MyTaskDto> myTaskDtoList = taskService.getAllTasksObjects();

		model.addAttribute("tasks", myTaskDtoList);
		MyUserDto myUserDto = myUserService.currentUser();

		System.out.println("ROLE ===== " + myUserDto.getRole());

		if (myUserDto.getRole().contains("ADMIN")) {

			return "admin-delete-task";
		}
		return "user-delete-task";

	}

	@PostMapping({ "/user/delete/task", "/admin/delete/task" })
	public String deleteTaskId(@RequestParam("taskId") Integer selectedTaskId, Model model) {

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println(
				"ENTERED...................................@PostMapping('/admin/delete/task')");
		logger.trace("ENTERED……………………………………	@PostMapping(\"/admin/delete/task\")------");

		Optional<MyTask> foundTask = myTaskRepository.findById(selectedTaskId);

		try {

			// delete the task with the id value = taskID
			taskService.deleteByTaskId(selectedTaskId);

		} catch (TaskNotFoundException tnfe) {
			throw new TaskNotFoundException("Task not found with id for the active user");
		}

		List<MyTaskDto> myTaskDtoList = taskService.getAllTasksObjects();

		model.addAttribute("tasks", myTaskDtoList);

		MyUserDto myUserDto = myUserService.currentUser();

		System.out.println("ROLE ===== " + myUserDto.getRole());

		if (myUserDto.getRole().contains("ADMIN")) {

			return "admin-delete-task";
		}
		return "user-delete-task";

	}

	@GetMapping({ "/user/taskList", "/admin/taskList" })
	public String getTaskListObjects(@ModelAttribute MyTaskDto myTaskDto, Model model) {
		// Validate object data if necessary
		// Save object to database
		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		logger.trace("ENTERED……………………………………	@GetMapping(\"/admin/taskList\")");
		logger.trace("EXITED……………………………………	@GetMapping(\"/admin/taskList\")");

		List<MyTaskDto> myTaskDtoList = taskService.getAllTasksObjects();

		for (int i = 0; i < myTaskDtoList.size(); i++) {

			System.out.println(myTaskDtoList.get(i).toString());

		}

		model.addAttribute("tasks", myTaskDtoList);
		MyUserDto myUserDto = myUserService.currentUser();

		System.out.println("ROLE ===== " + myUserDto.getRole());

		if (myUserDto.getRole().contains("ADMIN")) {

			return "admin-task-list";
		}
		return "user-task-list";

	}

}