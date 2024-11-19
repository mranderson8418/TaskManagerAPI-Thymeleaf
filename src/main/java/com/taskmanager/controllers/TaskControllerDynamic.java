package com.taskmanager.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taskmanager.dto.MyTaskDto;
import com.taskmanager.dto.MyUserDto;
import com.taskmanager.exceptions.TaskNotFoundException;
import com.taskmanager.model.MyTask;
import com.taskmanager.repository.MyUserRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.security.MyUserDetailService;
import com.taskmanager.service.TaskService;

@Controller
public class TaskControllerDynamic {

	// The AuthenticationManager will help us authenticate by username and
	// password

	private static int taskCount = 0;

	// Initialize a logger for the class
	Logger logger = LoggerFactory.getLogger(TaskControllerDynamic.class.getName());

	@Autowired
	private MyUserDetailService myUserDetailService;

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

	public TaskControllerDynamic(
			TaskService taskService,
			MyUserRepository myUserRepository,
			TaskRepository myTaskRepository,
			PasswordEncoder passwordEncoder) {
		this.taskService = taskService;
		this.myTaskRepository = myTaskRepository;
		this.myTaskRepository = myTaskRepository;
		this.passwordEncoder = passwordEncoder;

	}

	// @PostMapping("/task/create")
	// @ResponseStatus(HttpStatus.CREATED)
	// public ResponseEntity<MyTaskDto> createTask(@RequestBody MyTaskDto taskDto) {
	//
	// logger.trace("ENTERED……………………………………createTask()");
	//
	// System.out.println("/task/create"); // Log the creation request
	// logger.trace("EXITED……………………………………createTask()");
	// return new ResponseEntity<MyTaskDto>(taskService.createTask(taskDto),
	// HttpStatus.CREATED);
	// }

	// HI SCOTT

	// http://localhost:8080/admin/create/task
	@GetMapping("/admin/create/task")
	public String createTaskNew(Model model) {

		taskCount = taskCount + 1;

		model.addAttribute("myUserDto", new MyUserDto());

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());

		System.out.println("ENTERED...................................	@GetMapping(\"/admin/create/task\")");
		logger.trace("ENTERED……………………………………		@GetMapping(\"/admin/create/task\")------");

		model.addAttribute("myUser", new MyTaskDto());

		return "admin-create-task";
	}

	// http://localhost:8080/admin/create/task
	@PostMapping("/admin/create/task")
	// @ModelAttribute - this will bind the annotated object with the model class
	public String createTaskNew(@ModelAttribute MyTaskDto myTaskDto, Model model) {

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println("ENTERED...................................	@PostMapping(\"/admin/create/task\")");

		logger.trace("ENTERED……………………………………		@PostMapping(\"/admin/create/task\")------");

		if (myTaskDto.getContent() == null) {
			myTaskDto.setContent("");

		}

		// task is inserted into the TaskRepository
		MyTaskDto task_inserted = taskService.createTask(myTaskDto);

		System.out.println("username" + task_inserted.getUsername());

		System.out.println("Content:" + task_inserted.getContent());
		System.out.println("Complete: " + task_inserted.isComplete());

		model.addAttribute("id", task_inserted.getId());
		model.addAttribute("username", task_inserted.getUsername());
		model.addAttribute("content", task_inserted.getContent());
		model.addAttribute("complete", task_inserted.isComplete());

		System.out.println("Task # " + task_inserted.getId() + " is added to the database");

		// List<MyTask> tasks = myTaskRepository.findAll();

		// model.addAttribute("tasks", tasks);

		return "admin-task-added";

	}

	// http://localhost:8080/admin/update/task/{id}
	@GetMapping("/admin/update/task")
	public String updateTask(@ModelAttribute MyTaskDto myTaskDto, Model model) {

		model.addAttribute("myUserDto", new MyUserDto());

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());

		System.out.println("ENTERED...................................		@GetMapping(\"/admin/updateTask\")");
		logger.trace("ENTERED……………………………………			@GetMapping(\"/admin/updateTask\")------");

		List<MyTask> myTaskList = myTaskRepository.findAll();

		for (int i = 0; i < myTaskList.size(); i++) {

			System.out.println("taskList.get(i).getId()= " + myTaskList.get(i).getId());

			System.out.println("taskList.get(i).getUsername() = " + myTaskList.get(i).getUsername());

			System.out.println("taskList.get(i).getContent() " + myTaskList.get(i).getContent());

			System.out.println("taskList.get(i).isComplete()" + myTaskList.get(i).isComplete());

			System.out.println();
		}

		List<MyTaskDto> myTaskDtoList = new ArrayList<>();

		// converts the MyTask list into MyTaskDto list to add a layer of security
		for (int i = 0; i < myTaskList.size(); i++) {

			myTaskDto = taskService.mapToDto(myTaskList.get(i));

			myTaskDtoList.add(myTaskDto);

		}

		for (int i = 0; i < myTaskList.size(); i++) {

			System.out.println(myTaskDtoList.get(i).getId());

			System.out.println(myTaskDtoList.get(i).getUsername());

			System.out.println(myTaskDtoList.get(i).getContent());

			System.out.println(myTaskDtoList.get(i).isComplete());

			System.out.println();
		}

		model.addAttribute("tasks", myTaskDtoList);

		return "admin-create-task-update";
	}

	@PutMapping("/admin/update/task/{id}")
	// @ModelAttribute - this will bind the annotated object with the model class
	public String updateTask(@RequestParam("taskID") Integer selectedTaskId, @ModelAttribute MyTaskDto myTaskDto) {

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println("ENTERED...................................		@PostMapping(\"/admin/updateTask\")");
		logger.trace("ENTERED……………………………………		@PostMapping(\"/admin/updateTask\")------");

		// task is inserted into the TaskRepository
		MyTaskDto task_inserted = taskService.updateTask(myTaskDto, selectedTaskId);

		System.out.println("username" + task_inserted.getUsername());

		System.out.println("Content:" + task_inserted.getContent());
		System.out.println("Complete: " + task_inserted.isComplete());
		System.out.println("------------------------------------------------------------------------");

		model.addAttribute("id", task_inserted.getId());
		model.addAttribute("username", task_inserted.getUsername());
		model.addAttribute("content", task_inserted.getContent());
		model.addAttribute("complete", task_inserted.isComplete());

		System.out.println("Task # " + task_inserted.getId() + " is updated in the database");

		// List<MyTask> tasks = myTaskRepository.findAll();

		// model.addAttribute("tasks", tasks);

		return "admin-task-added";

	}

	@GetMapping("/admin/taskList")
	public String getTaskList(@ModelAttribute MyTaskDto myTaskDto, Model model) {
		// Validate object data if necessary
		// Save object to database
		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		logger.trace("ENTERED……………………………………	@GetMapping(\"/admin/taskList\")");
		logger.trace("EXITED……………………………………	@GetMapping(\"/admin/taskList\")");

		List<MyTask> myTaskList = myTaskRepository.findAll();

		for (int i = 0; i < myTaskList.size(); i++) {

			System.out.println("taskList.get(i).getId()= " + myTaskList.get(i).getId());

			System.out.println("taskList.get(i).getUsername() = " + myTaskList.get(i).getUsername());

			System.out.println("taskList.get(i).getContent() " + myTaskList.get(i).getContent());

			System.out.println("taskList.get(i).isComplete()" + myTaskList.get(i).isComplete());

			System.out.println();
		}

		List<MyTaskDto> myTaskDtoList = new ArrayList<>();

		// converts the MyTask list into MyTaskDto list to add a layer of security
		for (int i = 0; i < myTaskList.size(); i++) {

			myTaskDto = taskService.mapToDto(myTaskList.get(i));

			myTaskDtoList.add(myTaskDto);

		}

		for (int i = 0; i < myTaskList.size(); i++) {

			System.out.println(myTaskDtoList.get(i).getId());

			System.out.println(myTaskDtoList.get(i).getUsername());

			System.out.println(myTaskDtoList.get(i).getContent());

			System.out.println(myTaskDtoList.get(i).isComplete());

			System.out.println();
		}

		model.addAttribute("tasks", myTaskDtoList);

		return "admin-task-list";
	}

	@GetMapping("/admin/delete/task")
	public String getDeleteTask(@ModelAttribute MyTaskDto myTaskDto, Model model) {
		// Validate object data if necessary
		// Save object to database
		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		logger.trace("ENTERED……………………………………	@GetMapping(\"/admin/deleteTask\")");
		logger.trace("EXITED……………………………………	@GetMapping(\"/admin/deleteTask\")");

		List<MyTask> myTaskList = myTaskRepository.findAll();

		for (int i = 0; i < myTaskList.size(); i++) {

			System.out.println("taskList.get(i).getId()= " + myTaskList.get(i).getId());

			System.out.println("taskList.get(i).getUsername() = " + myTaskList.get(i).getUsername());

			System.out.println("taskList.get(i).getContent() " + myTaskList.get(i).getContent());

			System.out.println("taskList.get(i).isComplete()" + myTaskList.get(i).isComplete());

			System.out.println();
		}

		List<MyTaskDto> myTaskDtoList = new ArrayList<>();

		// converts the MyTask list into MyTaskDto list to add a layer of security
		for (int i = 0; i < myTaskList.size(); i++) {

			myTaskDto = taskService.mapToDto(myTaskList.get(i));

			myTaskDtoList.add(myTaskDto);

		}

		for (int i = 0; i < myTaskList.size(); i++) {

			System.out.println(myTaskDtoList.get(i).getId());

			System.out.println(myTaskDtoList.get(i).getUsername());

			System.out.println(myTaskDtoList.get(i).getContent());

			System.out.println(myTaskDtoList.get(i).isComplete());

			System.out.println();
		}

		model.addAttribute("tasks", myTaskDtoList);

		return "admin-task-list-delete";
	}

	@PostMapping("/admin/delete/task")
	public String deleteTaskId(@RequestParam("taskId") Integer selectedTaskId, Model model) {

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		System.out.println("ENTERED...................................@PostMapping('/admin/delete/task')");
		logger.trace("ENTERED……………………………………	@PostMapping(\"/admin/delete/task\")------");

		Optional<MyTask> foundTask = myTaskRepository.findById(selectedTaskId);

		// delete the task with the id value = taskID
		taskService.deleteByTaskId(selectedTaskId);

		if (foundTask.isPresent()) {

			System.out.println("Task exists-------------------" + selectedTaskId);

			List<MyTask> myTaskList = myTaskRepository.findAll();
			MyTaskDto myTaskDto = new MyTaskDto();

			List<MyTaskDto> myTaskDtoList = new ArrayList<>();

			// converts the MyTask list into MyTaskDto list to add a layer of security
			for (int i = 0; i < myTaskList.size(); i++) {

				myTaskDto = taskService.mapToDto(myTaskList.get(i));

				myTaskDtoList.add(myTaskDto);

			}

			for (int i = 0; i < myTaskList.size(); i++) {

				System.out.println(myTaskDtoList.get(i).getId());

				System.out.println(myTaskDtoList.get(i).getUsername());

				System.out.println(myTaskDtoList.get(i).getContent());

				System.out.println(myTaskDtoList.get(i).isComplete());

				System.out.println();
			}

			model.addAttribute("tasks", myTaskDtoList);

			return "admin-task-list-delete";

		}

		else {

			throw new TaskNotFoundException("Task with ID # " + selectedTaskId + "not found in the task database");

		}

	}

}

//
// @DeleteMapping("/admin/deleteTask/{id}")
// public ResponseEntity<String> deleteTaskById(@ModelAttribute model) {
//
// logger.trace("ENTERED……………………………………deleteTaskById()");
//
// taskService.deleteByTaskId(id);
//
// logger.trace("EXITED……………………………………deleteTaskById()");
// return new ResponseEntity<>("Successfully deleted task id = " + id,
// HttpStatus.OK);
// }
//
// @GetMapping("/getalltasks")
// public ResponseEntity<TaskResponse> getAllTasks(
// @RequestParam(value = "pageNo", defaultValue = "0", required = false) int
// pageNo,
// @RequestParam(value = "pageSize", defaultValue = "10", required = false) int
// pageSize) {
//
// logger.trace("ENTERED……………………………………getAllTasks()");
// logger.trace("EXITED……………………………………getAllTasks()");
// return new ResponseEntity<>(taskService.getAllTasks(pageNo, pageSize),
// HttpStatus.OK);
// }
//
// @GetMapping("/task/{id}")
// public ResponseEntity<MyTaskDto> getTaskDetail(@PathVariable("id") int id) {
// logger.trace("ENTERED……………………………………getTaskDetail()");
// logger.trace("EXITED……………………………………getTaskDetail()");
// return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
// }

// @PutMapping("/task/update/{id}")
// public ResponseEntity<MyTaskDto> updateTaskDetail(@RequestBody MyTaskDto
// taskUpdate,
// @PathVariable("id") int id) {
// logger.trace("ENTERED……………………………………updateTaskDetail()");
//
// MyTaskDto response = taskService.updateTask(taskUpdate, id);
// logger.trace("EXITED……………………………………updateTaskDetail()");
// return new ResponseEntity<>(response, HttpStatus.OK);
// }
