package org.objectquery.jpaquerybuilder;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.objectquery.builder.GenericObjectQuery;
import org.objectquery.builder.ObjectQuery;
import org.objectquery.builder.OrderType;
import org.objectquery.builder.ProjectionType;
import org.objectquery.jpaquerybuilder.domain.Home;
import org.objectquery.jpaquerybuilder.domain.Person;

public class TestSimpleQuery {

	@Test
	public void testSelect() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getName(), "tom");

		Assert.assertEquals("select A from org.objectquery.jpaquerybuilder.domain.Person A where A.name  =  :name", JPAObjectQuery.jpqlGenerator(qp).getQuery());

	}

	@Test
	public void testSelectDupliedPath() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getName(), "tom");
		qp.eq(target.getName(), "tom3");

		Assert.assertEquals("select A from org.objectquery.jpaquerybuilder.domain.Person A where A.name  =  :name AND A.name  =  :name1", JPAObjectQuery
				.jpqlGenerator(qp).getQuery());

	}

	@Test
	public void testSelectDottedPath() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getDog().getName(), "tom");
		qp.eq(target.getDud().getName(), "tom3");

		Assert.assertEquals("select A from org.objectquery.jpaquerybuilder.domain.Person A where A.dog.name  =  :dog_name AND A.dud.name  =  :dud_name",
				JPAObjectQuery.jpqlGenerator(qp).getQuery());

	}

	@Test
	public void testSelectProjection() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.prj(target.getName());
		qp.eq(target.getDog().getName(), "tom");

		Assert.assertEquals("select A.name from org.objectquery.jpaquerybuilder.domain.Person A where A.dog.name  =  :dog_name",
				JPAObjectQuery.jpqlGenerator(qp).getQuery());

	}

	@Test
	public void testProjectionCountThis() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.prj(target, ProjectionType.COUNT);
		qp.eq(target.getDog().getName(), "tom");

		Assert.assertEquals("select  COUNT(A) from org.objectquery.jpaquerybuilder.domain.Person A where A.dog.name  =  :dog_name", JPAObjectQuery
				.jpqlGenerator(qp).getQuery());

	}

	@Test
	public void testSelectOrder() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getDog().getName(), "tom");
		qp.order(target.getName());

		Assert.assertEquals("select A from org.objectquery.jpaquerybuilder.domain.Person A where A.dog.name  =  :dog_name order by A.name", JPAObjectQuery
				.jpqlGenerator(qp).getQuery());

	}

	@Test
	public void testSelectOrderAsc() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getDog().getName(), "tom");
		qp.order(target.getName(), OrderType.ASC);

		Assert.assertEquals("select A from org.objectquery.jpaquerybuilder.domain.Person A where A.dog.name  =  :dog_name order by A.name ASC", JPAObjectQuery
				.jpqlGenerator(qp).getQuery());

	}

	@Test
	public void testSelectOrderDesc() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getDog().getName(), "tom");
		qp.order(target.getName(), OrderType.DESC);
		qp.order(target.getDog().getName(), OrderType.DESC);

		Assert.assertEquals(
				"select A from org.objectquery.jpaquerybuilder.domain.Person A where A.dog.name  =  :dog_name order by A.name DESC,A.dog.name DESC",
				JPAObjectQuery.jpqlGenerator(qp).getQuery());

	}

	@Test
	public void testAllSimpleConditions() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.eq(target.getName(), "tom");
		qp.like(target.getName(), "tom");
		qp.max(target.getName(), "tom");
		qp.min(target.getName(), "tom");
		qp.maxEq(target.getName(), "tom");
		qp.minEq(target.getName(), "tom");
		qp.notEq(target.getName(), "tom");

		Assert.assertEquals(
				"select A from org.objectquery.jpaquerybuilder.domain.Person A where A.name  =  :name AND A.name  like  :name1 AND A.name  >  :name2 AND A.name  <  :name3 AND A.name  >=  :name4 AND A.name  <=  :name5 AND A.name  <>  :name6",
				JPAObjectQuery.jpqlGenerator(qp).getQuery());

	}

	@Test
	public void testINCondition() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		List<String> pars = new ArrayList<String>();
		qp.in(target.getName(), pars);
		qp.notIn(target.getName(), pars);

		Assert.assertEquals("select A from org.objectquery.jpaquerybuilder.domain.Person A where A.name  in  (:name) AND A.name  not in  (:name1)",
				JPAObjectQuery.jpqlGenerator(qp).getQuery());

	}

	@Test
	public void testContainsCondition() {

		GenericObjectQuery<Person> qp = new GenericObjectQuery<Person>(Person.class);
		Person target = qp.target();
		Person p = new Person();
		qp.contains(target.getFriends(), p);
		qp.notContains(target.getFriends(), p);

		Assert.assertEquals(
				"select A from org.objectquery.jpaquerybuilder.domain.Person A where :friends  member of  A.friends AND :friends1  not member of  A.friends",
				JPAObjectQuery.jpqlGenerator(qp).getQuery());

	}

	@Test
	public void testProjectionGroup() {

		ObjectQuery<Home> qp = new GenericObjectQuery<Home>(Home.class);
		Home target = qp.target();
		qp.prj(target.getAddress());
		qp.prj(qp.box(target.getPrice()), ProjectionType.MAX);
		qp.order(target.getAddress());

		Assert.assertEquals("select A.address, MAX(A.price) from org.objectquery.jpaquerybuilder.domain.Home A group by A.address order by A.address",
				JPAObjectQuery.jpqlGenerator(qp).getQuery());

	}

}
