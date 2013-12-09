package org.objectquery.jpa;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;
import org.objectquery.QueryEngine;

public class QueryEngineTest {

	@Test
	public void testFactory() {
		QueryEngine<EntityManager> instance = QueryEngine.instance(EntityManager.class);
		Assert.assertTrue(instance instanceof JPAQueryEngine);
	}

	@Test
	public void testDefalutFactory() {
		QueryEngine<EntityManager> instance = QueryEngine.defaultInstance();
		Assert.assertTrue(instance instanceof JPAQueryEngine);
	}
}
