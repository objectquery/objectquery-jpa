package org.objectquery.jpaobjectquery;

import org.junit.Assert;
import org.junit.Test;
import org.objectquery.ObjectQuery;
import org.objectquery.generic.GenericObjectQuery;
import org.objectquery.generic.JoinType;
import org.objectquery.generic.ObjectQueryException;
import org.objectquery.jpaobjectquery.domain.Person;

public class TestJoinQuery {

	@Test
	public void testSimpleJoin() {
		ObjectQuery<Person> query = new GenericObjectQuery<Person>(Person.class);
		Person joined = query.join(Person.class);
		query.eq(query.target().getMom(), joined);

		Assert.assertEquals(
				"select A from org.objectquery.jpaobjectquery.domain.Person A,org.objectquery.jpaobjectquery.domain.Person AB0 where A.mom  =  AB0",
				JPAObjectQuery.jpqlGenerator(query).getQuery());
	}

	@Test(expected=ObjectQueryException.class)
	public void testTypedJoin() {
		ObjectQuery<Person> query = new GenericObjectQuery<Person>(Person.class);
		Person joined = query.join(Person.class, JoinType.LEFT);
		query.eq(query.target().getMom(), joined);

		Assert.assertEquals(
				"select A from org.objectquery.jpaobjectquery.domain.Person A LEFT JOIN org.objectquery.jpaobjectquery.domain.Person AB0 where A.mom  =  AB0",
				JPAObjectQuery.jpqlGenerator(query).getQuery());
	}

	@Test
	public void testTypedPathJoin() {
		ObjectQuery<Person> query = new GenericObjectQuery<Person>(Person.class);
		Person joined = query.join(query.target().getMom(), Person.class, JoinType.LEFT);
		query.eq(joined.getName(), "test");

		Assert.assertEquals("select A from org.objectquery.jpaobjectquery.domain.Person A LEFT JOIN A.mom AB0 where AB0.name  =  :AB0_name", JPAObjectQuery
				.jpqlGenerator(query).getQuery());
	}

}
