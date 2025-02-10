package com.taskmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Scott Fix
@SpringBootApplication
public class TaskManagerApiApplication {

	static final Logger logger = LoggerFactory.getLogger(TaskManagerApiApplication.class.getName());

	public static void main(String[] args) {
		try {
			logger.trace("Starting……………………………………TaskManagerApiApplication()");
		} catch (Exception e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}

		SpringApplication.run(TaskManagerApiApplication.class, args);

	}

}
