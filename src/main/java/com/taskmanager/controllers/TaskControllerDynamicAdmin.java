
package com.taskmanager.controllers;

import java.util.Collection;
import java.util.List;

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

import com.taskmanager.dto.MyTaskDto;
import com.taskmanager.dto.MyUserDto;
import com.taskmanager.exceptions.TaskNotFoundException;
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

	private String nameClass() {

		LastWord lastWord = new LastWord(getClass().getName());

		return "Class = " + lastWord.getLastWord();
	}

	@GetMapping({ "/user/create/task", "/admin/create/task" })
	public String createTask(Model model) {

		model.addAttribute("myUserDto", new MyUserDto());

		System.out.println(nameClass());

		System.out.println("ENTERED................	@GetMapping(\"/admin/create/task\")");
		logger.trace("ENTERED……………………………………		@GetMapping(\"/admin/create/task\")------");

		model.addAttribute("myTaskDto", new MyTaskDto());

		MyUserDto myUserDto = myUserService.currentUser();

		System.out.println("ROLE ===== " + myUserDto.getRole());

		if (myUserDto.getRole().contains("ADMIN")) {

			return "admin-create-task";
		}
		return "user-create-task";

	}

	@PostMapping({ "/user/create/task", "/admin/create/task" })
	// @ModelAttribute - this will bind the annotated object with the model class
	public String createTaskNewPost(@ModelAttribute MyTaskDto myTaskDto, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		System.out.println("authentication.getPrincipal() = " + authentication.getPrincipal());

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		String username = userDetails.getUsername();

		MyUserDto myUserDto = myUserService.currentUser();

		System.out.println(nameClass());

		logger.trace("ENTERED……………………………………		@PostMapping(\"/admin/create/task\")------");

		// task is inserted into the TaskRepository
		myTaskDto = taskService.createTask(myTaskDto);

		model.addAttribute("id", myTaskDto.getId());
		model.addAttribute("taskNumber", myTaskDto.getTaskNumber());
		model.addAttribute("username", myTaskDto.getUsername());
		model.addAttribute("content", myTaskDto.getContent());
		model.addAttribute("complete", myTaskDto.isComplete());

		System.out.println("Task # " + myTaskDto.getId() + " is added to the database");

		Collection<? extends GrantedAuthority> role = getAuthorityOfUser();

		for (GrantedAuthority authority : role) {

			if (authority.getAuthority().equals("ROLE_ADMIN")) {
				return "admin-created-new-task";
			}
		}
		return "user-created-new-task";
	}

	// http://localhost:8080/admin/update/task
	@GetMapping({ "/user/update/task/viewTable", "/admin/update/task/viewTable" })
	public String updateTaskTableView(@ModelAttribute MyTaskDto myTaskDto, Model model) {

		System.out.println(nameClass());

		System.out.println("ENTERED...................................		@GetMapping(\"/admin/updateTask\")");
		logger.trace("ENTERED……………………………………			@GetMapping(\"/admin/updateTask\")------");

		List<MyTaskDto> myTaskDtoList = taskService.getAllTasksObjects();

		model.addAttribute("myUserDto", new MyUserDto());

		model.addAttribute("myTaskDto", new MyTaskDto());

		model.addAttribute("tasks", myTaskDtoList);

		MyUserDto myUserDto = myUserService.currentUser();

		System.out.println("ROLE ===== " + myUserDto.getRole());

		if (myUserDto.getRole().contains("ADMIN")) {

			return "admin-update-task-view-table";
		}

		return "user-update-task-view-table";

	}

	@PostMapping({ "/user/update/task/viewTable", "/admin/update/task/viewTable" })
	// @ModelAttribute - this will bind the annotated object with the model class
	public String updateTaskNewViewTable(@ModelAttribute MyTaskDto myTaskDto, Model model) {

		System.out.println(nameClass());
		System.out.println("ENTERED..........................................updateTaskNew()");

		logger.trace("ENTERED……………………………………		@PostMapping(\"/admin/updateTask\")------");

		try {
			// task is inserted into the TaskRepository
			MyTaskDto task_inserted = taskService.updateTask(myTaskDto, myTaskDto.getTaskNumber());

			model.addAttribute("id", task_inserted.getId());
			model.addAttribute("taskNumber", task_inserted.getTaskNumber());
			model.addAttribute("username", task_inserted.getUsername());
			model.addAttribute("content", task_inserted.getContent());
			model.addAttribute("complete", task_inserted.isComplete());

			System.out.println("Task # " + task_inserted.getTaskNumber() + " is updated in the database");

			MyUserDto myUserDto = myUserService.currentUser();

			System.out.println("ROLE ===== " + myUserDto.getRole());

			if (myUserDto.getRole().contains("ADMIN")) {
				System.out.println("EXITED..........................................updateTaskNew()");
				return "redirect:/admin/taskList/viewTable";
			}
			System.out.println("EXITED..........................................updateTaskNew()");
			return "redirect:/user/taskList/viewTable";

		} catch (TaskNotFoundException tnfe) {
			throw new TaskNotFoundException("unable to find task with id");
		}

	}

	// http://localhost:8080/admin/update/task
	@GetMapping({ "/user/update/task/viewModular", "/admin/update/task/viewModular" })
	public String updateTask(@ModelAttribute MyTaskDto myTaskDto, Model model) {

		System.out.println(nameClass());

		System.out.println("ENTERED...................................		@GetMapping(\"/admin/updateTask\")");
		logger.trace("ENTERED……………………………………			@GetMapping(\"/admin/updateTask\")------");

		List<MyTaskDto> myTaskDtoList = taskService.getAllTasksObjects();

		model.addAttribute("myUserDto", new MyUserDto());

		model.addAttribute("myTaskDto", new MyTaskDto());

		model.addAttribute("tasks", myTaskDtoList);

		MyUserDto myUserDto = myUserService.currentUser();

		System.out.println("ROLE ===== " + myUserDto.getRole());

		if (myUserDto.getRole().contains("ADMIN")) {

			return "admin-update-task-view-modular";
		}

		return "user-update-task-view-modular";

	}

	@PostMapping({ "/user/update/task/viewModular", "/admin/update/task/viewModular" })
	// @ModelAttribute - this will bind the annotated object with the model class
	public String updateTaskNew(@ModelAttribute MyTaskDto myTaskDto, Model model) {

		System.out.println(nameClass());
		System.out.println("ENTERED..........................................updateTaskNew()");

		logger.trace("ENTERED……………………………………		@PostMapping(\"/admin/updateTask\")------");

		try {
			// task is inserted into the TaskRepository
			MyTaskDto task_inserted = taskService.updateTask(myTaskDto, myTaskDto.getTaskNumber());

			model.addAttribute("id", task_inserted.getId());
			model.addAttribute("taskNumber", task_inserted.getTaskNumber());
			model.addAttribute("username", task_inserted.getUsername());
			model.addAttribute("content", task_inserted.getContent());
			model.addAttribute("complete", task_inserted.isComplete());

			System.out.println("Task # " + task_inserted.getTaskNumber() + " is updated in the database");

			MyUserDto myUserDto = myUserService.currentUser();

			System.out.println("ROLE ===== " + myUserDto.getRole());

			if (myUserDto.getRole().contains("ADMIN")) {
				System.out.println("EXITED..........................................updateTaskNew()");
				return "redirect:/admin/taskList/viewModular";
			}
			System.out.println("EXITED..........................................updateTaskNew()");
			return "redirect:/user/taskList/viewModular";

		} catch (TaskNotFoundException tnfe) {
			throw new TaskNotFoundException("unable to find task with id");
		}

	}

	@GetMapping({ "/user/delete/task/viewTable", "/admin/delete/task/viewTable" })
	public String getDeleteTaskViewTable(@ModelAttribute MyTaskDto myTaskDto, Model model) {

		// Validate object data if necessary
		// Save object to database
		System.out.println(nameClass());
		System.out.println("ENTERED..........................................@GetMapping----> getDeleteTask()");

		;
		logger.trace("ENTERED..........................................@GetMapping----> getDeleteTask()");

		List<MyTaskDto> myTaskDtoList = taskService.getAllTasksObjects();

		model.addAttribute("myTaskDto", new MyTaskDto());

		model.addAttribute("tasks", myTaskDtoList);

		MyUserDto myUserDto = myUserService.currentUser();

		System.out.println("ROLE ===== " + myUserDto.getRole());

		if (myUserDto.getRole().contains("ADMIN")) {

			logger.trace("EXITED……………………………………	@GetMapping----> getDeleteTask()");

			return "admin-delete-task-view-table";
		}
		System.out.println("EXITED..........................................@GetMapping----> getDeleteTask()");
		logger.trace("EXITED..........................................@GetMapping----> getDeleteTask()");
		return "user-delete-task-view-table";

	}

	@GetMapping({ "/user/delete/task/viewModular", "/admin/delete/task/viewModular" })
	public String getDeleteTaskViewModular(@ModelAttribute MyTaskDto myTaskDto, Model model) {

		// Validate object data if necessary
		// Save object to database
		System.out.println(nameClass());
		System.out.println("ENTERED..........................................@GetMapping----> getDeleteTask()");

		;
		logger.trace("ENTERED..........................................@GetMapping----> getDeleteTask()");

		List<MyTaskDto> myTaskDtoList = taskService.getAllTasksObjects();

		model.addAttribute("myTaskDto", new MyTaskDto());

		model.addAttribute("tasks", myTaskDtoList);

		MyUserDto myUserDto = myUserService.currentUser();

		System.out.println("ROLE ===== " + myUserDto.getRole());

		if (myUserDto.getRole().contains("ADMIN")) {

			logger.trace("EXITED……………………………………	@GetMapping----> getDeleteTask()");

			return "admin-delete-task-view-modular";
		}
		System.out.println("EXITED..........................................@GetMapping----> getDeleteTask()");
		logger.trace("EXITED..........................................@GetMapping----> getDeleteTask()");
		return "user-delete-task-view-modular";

	}

	@PostMapping({ "/user/delete/task/viewTable", "/admin/delete/task/viewTable" })
	public String deleteTaskId(@ModelAttribute MyTaskDto myTaskDto, Model model) throws TaskNotFoundException {

		System.out.println(nameClass());

		System.out.println("ENTERED..........................................deleteTaskId()");

		logger.trace("ENTERED……………………………………	@PostMapping()------> deleteTaskId()");

		try {

			// delete the task with the id value = taskNumber
			taskService.deleteByTaskId(myTaskDto.getTaskNumber());

		} catch (TaskNotFoundException tnfe) {

			throw new TaskNotFoundException("Task not found with id for the active user");

		}

		List<MyTaskDto> myTaskDtoList = taskService.afterDeleteGetAllTasks();

		model.addAttribute("tasks", myTaskDtoList);

		MyUserDto myUserDto = myUserService.currentUser();

		System.out.println("ROLE ===== " + myUserDto.getRole());

		if (myUserDto.getRole().contains("ADMIN")) {
			System.out.println("EXITED..........................................deleteTaskId()");
			return "admin-delete-task-view-table";
		}
		System.out.println("EXITED..........................................deleteTaskId()");
		return "user-delete-task-view-table";

	}

	@GetMapping({ "/user/taskList/viewModular", "/admin/taskList/viewModular" })
	public String getTaskListObjects(@ModelAttribute MyTaskDto myTaskDto, Model model) {
		// Validate object data if necessary
		// Save object to database

		logger.trace("EXITED……………………………………	@GetMapping(\"/admin/taskList\")");

		List<MyTaskDto> myTaskDtoList = taskService.getAllTasksObjects();

		for (int i = 0; i < myTaskDtoList.size(); i++) {

			System.out.println(myTaskDtoList.get(i).toString());

		}

		model.addAttribute("tasks", myTaskDtoList);

		MyUserDto myUserDto = myUserService.currentUser();

		System.out.println("ROLE ===== " + myUserDto.getRole());

		if (myUserDto.getRole().contains("ADMIN")) {

			return "admin-task-list-view-modular";
		}
		return "user-task-list-view-modular";

	}

	@GetMapping({ "/user/taskList/viewTable", "/admin/taskList/viewTable" })
	public String getTaskListObjectsTableView(@ModelAttribute MyTaskDto myTaskDto, Model model) {
		// Validate object data if necessary
		// Save object to database

		logger.trace("EXITED……………………………………	@GetMapping(\"/admin/taskList\")");

		List<MyTaskDto> myTaskDtoList = taskService.getAllTasksObjects();

		for (int i = 0; i < myTaskDtoList.size(); i++) {

			System.out.println(myTaskDtoList.get(i).toString());

		}

		model.addAttribute("tasks", myTaskDtoList);

		MyUserDto myUserDto = myUserService.currentUser();

		System.out.println("ROLE ===== " + myUserDto.getRole());

		if (myUserDto.getRole().contains("ADMIN")) {

			return "admin-task-list-view-table";
		}
		return "user-task-list-view-table";

	}

	public Collection<? extends GrantedAuthority> getAuthorityOfUser() {

		System.out.println(nameClass());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		// this is called a bounded generic
		// it is a list of something that extends the GrantedAuthority class
		Collection<? extends GrantedAuthority> role = userDetails.getAuthorities();

		return role;
	}

}
