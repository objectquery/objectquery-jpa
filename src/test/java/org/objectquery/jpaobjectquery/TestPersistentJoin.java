package org.objectquery.jpaobjectquery;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.objectquery.ObjectQuery;
import org.objectquery.generic.GenericObjectQuery;
import org.objectquery.generic.JoinType;
import org.objectquery.generic.ObjectQueryException;
import org.objectquery.jpaobjectquery.domain.Person;

public class TestPersistentJoin {

	private EntityManager entityManager;

	@Before
	public void beforeTest() {
		entityManager = PersistentTestHelper.getFactory().createEntityManager();
		entityManager.getTransaction().begin();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testSimpleJoin() {
		ObjectQuery<Person> query = new GenericObjectQuery<Person>(Person.class);
		Person joined = query.join(Person.class);
		query.eq(query.target().getMom(), joined);

		List<Person> persons = JPAObjectQuery.buildQuery(query, entityManager).getResultList();
		Assert.assertEquals(1, persons.size());
	}

	@Test(expected=ObjectQueryException.class)
	@SuppressWarnings("unchecked")
	public void testTypedJoin() {
		ObjectQuery<Person> query = new GenericObjectQuery<Person>(Person.class);
		Person joined = query.join(Person.class, JoinType.LEFT);
		query.eq(query.target().getMom(), joined);

		List<Person> persons = JPAObjectQuery.buildQuery(query, entityManager).getResultList();
		Assert.assertEquals(1, persons.size());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testTypedPathJoin() {
		ObjectQuery<Person> query = new GenericObjectQuery<Person>(Person.class);
		Person joined = query.join(query.target().getMom(), Person.class, JoinType.LEFT);
		query.eq(joined.getName(), "tommum");

		List<Person> persons = JPAObjectQuery.buildQuery(query, entityManager).getResultList();
		Assert.assertEquals(1, persons.size());
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
