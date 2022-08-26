package com.velikiyprikalel.demo;


public class DemoApplication {

	public static void main(String[] args) {
		System.out.println("Hello World!");

		for (var task : DBConnection.getAll()) {
			System.out.println(task.getTaskName());
		}
	}

}
