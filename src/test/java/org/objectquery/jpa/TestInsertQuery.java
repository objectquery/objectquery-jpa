package org.objectquery.jpa;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectquery.InsertQuery;
import org.objectquery.generic.GenericInsertQuery;
import org.objectquery.generic.ObjectQueryException;
import org.objectquery.jpa.domain.Person;

public class TestInsertQuery {

	private EntityManager entityManager;

	@Before
	public void beforeTest() {
		entityManager = PersistentTestHelper.getFactory().createEntityManager();
		entityManager.getTransaction().begin();
	}

	@Test(expected = ObjectQueryException.class)
	public void testSimpleInsert() {
		InsertQuery<Person> ip = new GenericInsertQuery<Person>(Person.class);
		ip.set(ip.target().getName(), "test");
		JPAObjectQuery.buildQuery(ip, entityManager);
	}

	@After
	public void afterTest() {
		if (entityManager != null) {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
		entityManager = null;
	}

}
