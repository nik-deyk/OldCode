package com.velikiyprikalel.demo.pojo;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tasks_table")
public class Task implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "task_name")
	private String taskName;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "owner", referencedColumnName = "name")
	private Owner owner;

	private int priority;

	public Task() {
	}

	public Task(int id, String taskName, String owner, int priority) {
		this.id = id;
		this.taskName = taskName;
		this.owner = new Owner(taskName);
		this.priority = priority;
	}

	public Task(String taskName, String owner, int priority) {
		this(0, taskName, owner, priority);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
    
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getOwner() {
		return owner.getName();
	}

	public void setOwner(String owner) {
		this.owner = new Owner(owner);
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String toString() {
		return "Task{" +
				"id=" + id +
				", taskName='" + taskName + '\'' +
				", owner='" + owner.getName() + '\'' +
				", priority=" + priority +
				'}';
	}
}
