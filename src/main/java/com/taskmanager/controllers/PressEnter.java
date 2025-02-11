package com.taskmanager.controllers;

import java.util.Scanner;

public class PressEnter {

	public static Scanner scanner = new Scanner(System.in);

	public static void pressEnter() {

		System.out.println("---PRESS ENTER---");
		scanner.nextLine();
	}
}
