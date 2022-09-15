package com.velikiyprikalel.demo;

import java.util.List;
import java.util.logging.Level;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.velikiyprikalel.demo.pojo.Task;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class DBHibernate implements DBInterface {
	private static SessionFactory sessionFactory;

	static {
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
			.configure("hibernate.xml").build();

		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			System.out.println("Cannot get session factory!");
			e.printStackTrace();
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// Getting data from base:

	public List<Task> getAll() {
		Session session = sessionFactory.openSession();
		
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<Task> query = criteriaBuilder.createQuery(Task.class);

		Root<Task> root = query.from(Task.class);
		query.select(root);

		Query sqlQuery = session.createQuery(query);

		List<Task> list = sqlQuery.getResultList();

		session.close();
		return list;
	}
}
