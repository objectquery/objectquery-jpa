package org.objectquery.jpaquerybuilder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.objectquery.builder.OrderType;
import org.objectquery.builder.ProjectionType;
import org.objectquery.jpaquerybuilder.domain.Home;
import org.objectquery.jpaquerybuilder.domain.Person;

public class TestPersistentSelect {
	private EntityManager entityManager;

	@Before
	public void beforeTest() {
		entityManager = PersistentTestHelper.getFactory().createEntityManager();
		entityManager.getTransaction().begin();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSimpleSelect() {
		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getName(), "tom");

		List<Person> res = qp.execute(entityManager);
		Assert.assertEquals(1, res.size());
		Assert.assertEquals(res.get(0).getName(), "tom");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSimpleSelectWithutCond() {
		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		List<Person> res = qp.execute(entityManager);
		Assert.assertEquals(3, res.size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSelectPathValue() {
		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getDud().getHome(), target.getMum().getHome());
		List<Person> res = qp.execute(entityManager);
		Assert.assertEquals(1, res.size());
		Assert.assertEquals(res.get(0).getDud().getHome(), res.get(0).getMum().getHome());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSelectCountThis() {
		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.prj(target, ProjectionType.COUNT);
		List<Object> res = qp.execute(entityManager);
		Assert.assertEquals(1, res.size());
		Assert.assertEquals(3L, res.get(0));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSelectPrjection() {
		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.prj(target.getName());
		qp.prj(target.getHome());
		qp.eq(target.getName(), "tom");
		List<Object[]> res = qp.execute(entityManager);
		Assert.assertEquals(1, res.size());
		Assert.assertEquals("tom", res.get(0)[0]);
		Assert.assertEquals("homeless", ((Home) res.get(0)[1]).getAddress());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSelectOrder() {
		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.prj(target.getName());
		qp.order(target.getName());
		List<Object[]> res = qp.execute(entityManager);
		Assert.assertEquals(3, res.size());
		Assert.assertEquals("tom", res.get(0));
		Assert.assertEquals("tomdud", res.get(1));
		Assert.assertEquals("tommum", res.get(2));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSelectOrderDesc() {
		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.prj(target.getName());
		qp.order(target.getName(), OrderType.DESC);
		List<Object[]> res = qp.execute(entityManager);
		Assert.assertEquals(3, res.size());
		Assert.assertEquals("tommum", res.get(0));
		Assert.assertEquals("tomdud", res.get(1));
		Assert.assertEquals("tom", res.get(2));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSelectSimpleConditions() {

		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getName(), "tom");
		qp.like(target.getName(), "tom");
		qp.max(target.getName(), "tom");
		qp.min(target.getName(), "tom");
		qp.maxEq(target.getName(), "tom");
		qp.minEq(target.getName(), "tom");
		qp.notEq(target.getName(), "tom");
		List<Object[]> res = qp.execute(entityManager);
		Assert.assertEquals(0, res.size());

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSelectINCondition() {

		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		JPQLObjectQuery<Person> qp0 = new JPQLObjectQuery<Person>(Person.class);
		Person target0 = qp0.target();
		qp0.eq(target0.getName(), "tom");

		List<String> pars = new ArrayList<String>();
		pars.add("tommy");
		qp.in(target.getName(), pars);
		qp.notIn(target.getName(), pars);

		List<Object[]> res = qp.execute(entityManager);
		Assert.assertEquals(0, res.size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSelectContainsCondition() {

		JPQLObjectQuery<Person> qp0 = new JPQLObjectQuery<Person>(Person.class);
		Person target0 = qp0.target();
		qp0.eq(target0.getName(), "tom");

		List<Person> res0 = qp0.execute(entityManager);
		Assert.assertEquals(1, res0.size());
		Person p = res0.get(0);

		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.contains(target.getFriends(), p);
		qp.notContains(target.getFriends(), p);

		List<Object[]> res = qp.execute(entityManager);
		Assert.assertEquals(0, res.size());
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
