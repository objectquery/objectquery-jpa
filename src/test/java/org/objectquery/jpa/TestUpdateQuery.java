package org.objectquery.jpa;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectquery.UpdateQuery;
import org.objectquery.generic.GenericUpdateQuery;
import org.objectquery.jpa.domain.Home;
import org.objectquery.jpa.domain.Person;

public class TestUpdateQuery {

	private EntityManager entityManager;

	@Before
	public void beforeTest() {
		entityManager = PersistentTestHelper.getFactory().createEntityManager();
		entityManager.getTransaction().begin();
	}

	@Test
	public void testSimpleUpdate() {
		UpdateQuery<Home> query = new GenericUpdateQuery<Home>(Home.class);
		query.set(query.target().getAddress(), "new-address");
		query.eq(query.target().getAddress(), "old-address");
		int res = JPAObjectQuery.execute(query, entityManager);
		Assert.assertEquals(1, res);
	}

	@Test
	public void testSimpleUpdateGen() {
		UpdateQuery<Home> query = new GenericUpdateQuery<Home>(Home.class);
		query.set(query.target().getAddress(), "new-address");
		query.eq(query.target().getAddress(), "old-address");
		JPQLQueryGenerator q = JPAObjectQuery.jpqlGenerator(query);
		Assert.assertEquals("update org.objectquery.jpa.domain.Home set address = :address where address  =  :address1", q.getQuery());
	}

	@Test
	public void testSimpleNestedUpdate() {
		UpdateQuery<Person> query = new GenericUpdateQuery<Person>(Person.class);
		query.set(query.target().getHome().getAddress(), "new-address");
		query.eq(query.target().getHome().getAddress(), "old-address");
		int res = JPAObjectQuery.execute(query, entityManager);
		Assert.assertEquals(1, res);
	}

	@Test
	public void testSimpleNestedUpdateGen() {
		UpdateQuery<Person> query = new GenericUpdateQuery<Person>(Person.class);
		query.set(query.target().getHome().getAddress(), "new-address");
		query.eq(query.target().getHome().getAddress(), "old-address");

		JPQLQueryGenerator q = JPAObjectQuery.jpqlGenerator(query);
		Assert.assertEquals("update org.objectquery.jpa.domain.Person set home.address = :home_address where home.address  =  :home_address1", q.getQuery());
	}

	@Test
	public void testMultipleNestedUpdate() {
		UpdateQuery<Home> query = new GenericUpdateQuery<Home>(Home.class);
		query.set(query.target().getAddress(), "new-address");
		query.set(query.box(query.target().getPrice()), 1d);
		query.eq(query.target().getAddress(), "old-address");
		int res = JPAObjectQuery.execute(query, entityManager);
		Assert.assertEquals(1, res);
	}

	@Test
	public void testMultipleNestedUpdateGen() {
		UpdateQuery<Home> query = new GenericUpdateQuery<Home>(Home.class);
		query.set(query.target().getAddress(), "new-address");
		query.set(query.box(query.target().getPrice()), 1d);
		query.eq(query.target().getAddress(), "old-address");

		JPQLQueryGenerator q = JPAObjectQuery.jpqlGenerator(query);
		Assert.assertEquals("update org.objectquery.jpa.domain.Home set address = :address,price = :price where address  =  :address1", q.getQuery());
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
