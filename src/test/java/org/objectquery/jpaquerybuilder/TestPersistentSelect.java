package org.objectquery.jpaquerybuilder;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.objectquery.builder.ConditionType;
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

	@After
	public void afterTest() {
		if (entityManager != null) {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
		entityManager = null;
	}
}
