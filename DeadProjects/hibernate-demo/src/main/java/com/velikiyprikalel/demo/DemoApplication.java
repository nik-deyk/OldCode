package com.velikiyprikalel.demo;


public class DemoApplication {

	public static void main(String[] args) {
		System.out.println("Hello World!");

		DBInterface database = new DBHibernate(); // Можно поменять на DBConnect.

		for (var task : database.getAll()) {
			System.out.println(task);
		}
	}

}
