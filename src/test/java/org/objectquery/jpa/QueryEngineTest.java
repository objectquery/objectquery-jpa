package org.objectquery.jpa;

import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.objectquery.QueryEngine;

public class QueryEngineTest {

	@Test
	public void testFactory() {
		QueryEngine<EntityManager> instance = QueryEngine.instance(EntityManager.class);
		assertTrue(instance instanceof JPAQueryEngine);
	}

	@Test
	public void testDefalutFactory() {
		QueryEngine<EntityManager> instance = QueryEngine.defaultInstance();
		assertTrue(instance instanceof JPAQueryEngine);
	}
}
