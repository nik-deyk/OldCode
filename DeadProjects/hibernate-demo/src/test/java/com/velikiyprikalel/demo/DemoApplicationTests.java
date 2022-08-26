package com.velikiyprikalel.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.velikiyprikalel.demo.pojo.Task;


class DemoApplicationTests {
	// You need your database to be on.

	@Test
	void simpleConnectionWorksGreat() {
		List<Task> tasks = new DBConnection().getAll();
		assertEquals("test_task-1", tasks.get(0).getTaskName());
		assertEquals("another task", tasks.get(1).getTaskName());
	}


	@Test
	void hibernateConnectionWorksGreat() {
		List<Task> tasks = new DBHibernate().getAll();
		assertEquals("test_task-1", tasks.get(0).getTaskName());
		assertEquals("another task", tasks.get(1).getTaskName());
	}
}
