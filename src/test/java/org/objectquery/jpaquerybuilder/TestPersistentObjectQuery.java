package org.objectquery.jpaquerybuilder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.objectquery.jpaquerybuilder.domain.Person;

public class TestPersistentObjectQuery {

	private static EntityManagerFactory entityManagerFactory;

	@BeforeClass
	public static void startup() {
		try {
			entityManagerFactory = Persistence.createEntityManagerFactory("test");
			EntityManager entityManager;
			entityManager = entityManagerFactory.createEntityManager();
			Person p = new Person();
			p.setName("tom");
			entityManager.persist(p);
			entityManager.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	@Test
	public void empty() {

	}

	@AfterClass
	public static void shutdown() {
		entityManagerFactory.close();
	}

}
