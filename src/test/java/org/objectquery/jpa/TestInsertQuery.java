package org.objectquery.jpa;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectquery.InsertQuery;
import org.objectquery.generic.GenericInsertQuery;
import org.objectquery.jpa.domain.Home;
import org.objectquery.jpa.domain.Person;

public class TestInsertQuery {

	private EntityManager entityManager;

	@Before
	public void beforeTest() {
		entityManager = PersistentTestHelper.getFactory().createEntityManager();
		entityManager.getTransaction().begin();
	}

	
	@Test
	public void testSimpleInsert() {
		InsertQuery<Person> ip = new GenericInsertQuery<Person>(Person.class);
		ip.set(ip.target().getName(), "test");
		JPAObjectQuery.execute(ip,  entityManager);
	}

	@Test
	public void testSimpleInsertGen() {
		InsertQuery<Person> ip = new GenericInsertQuery<Person>(Person.class);
		ip.set(ip.target().getName(), "test");
		JPQLQueryGenerator q = JPAObjectQuery.jpqlGenerator(ip);
		Assert.assertEquals("insert into org.objectquery.jpa.domain.Person (name)values(:name)", q.getQuery());
	}

	
	@Test
	public void testMultipInsert() {
		InsertQuery<Home> ip = new GenericInsertQuery<Home>(Home.class);
		ip.set(ip.box(ip.target().getPrice()), 4D);
		ip.set(ip.box(ip.target().getWeight()), 6);
		int res = JPAObjectQuery.execute(ip, entityManager);
		Assert.assertEquals(1, res);
	}

	@Test
	public void testMultipInsertGen() {
		InsertQuery<Home> ip = new GenericInsertQuery<Home>(Home.class);
		ip.set(ip.box(ip.target().getPrice()), 4D);
		ip.set(ip.box(ip.target().getWeight()), 6);
		JPQLQueryGenerator q = JPAObjectQuery.jpqlGenerator(ip);
		Assert.assertEquals("insert into org.objectquery.jpa.domain.Home (price,weight)values(:price,:weight)", q.getQuery());
	}
	
	@Test
	public void testDupicateFieldInsert() {
		InsertQuery<Home> ip = new GenericInsertQuery<Home>(Home.class);
		ip.set(ip.box(ip.target().getPrice()), 4D);
		ip.set(ip.box(ip.target().getPrice()), 6D);
		int res = JPAObjectQuery.execute(ip, entityManager);
		Assert.assertEquals(1, res);
	}

	@Test
	public void testNestedInsert() {
		InsertQuery<Person> ip = new GenericInsertQuery<Person>(Person.class);
		ip.set(ip.target().getDud().getName(), "test");
		JPQLQueryGenerator q = JPAObjectQuery.jpqlGenerator(ip);
		Assert.assertEquals("insert into org.objectquery.jpa.domain.Person (dud.name)values(:dud_name)", q.getQuery());
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
