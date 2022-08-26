package com.velikiyprikalel.demo.pojo;

import java.io.Serializable;
import java.util.Set;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "owners_table")
public class Owner implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

	@NaturalId
	private String name;

	@OneToMany(mappedBy = "owner")
	private Set<Task> tasks;

	public Owner() {
	}

	public Owner(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
