package org.objectquery.jpaquerybuilder;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.objectquery.builder.ConditionType;
import org.objectquery.jpaquerybuilder.domain.Person;

public class TestPersistentSelect {
	private EntityManager entityManager;

	@Before
	public void beforeTest() {
		if (TestPersistentSuite.entityManagerFactory != null) {
			entityManager = TestPersistentSuite.entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSimpleSelect() {
		if (entityManager == null) {
			return;
		}
		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.condition(target.getName(), ConditionType.EQUALS, "tom");

		List<Person> res = qp.execute(entityManager);
		Assert.assertEquals(1, res.size());
		Assert.assertEquals(res.get(0).getName(), "tom");
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
