package org.objectquery.jpaquerybuilder;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.objectquery.builder.ConditionType;
import org.objectquery.builder.OrderType;
import org.objectquery.builder.ProjectionType;
import org.objectquery.jpaquerybuilder.domain.Person;

public class TestSimpleQuery {

	@Test
	public void testSelect() {

		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.condition(target.getName(), ConditionType.EQUALS, "tom");

		Assert.assertEquals("select A from org.objectquery.jpaquerybuilder.domain.Person A where A.name  =  :name", qp.getQuery());

	}

	@Test
	public void testSelectDupliedPath() {

		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.condition(target.getName(), ConditionType.EQUALS, "tom");
		qp.condition(target.getName(), ConditionType.EQUALS, "tom3");

		Assert.assertEquals("select A from org.objectquery.jpaquerybuilder.domain.Person A where A.name  =  :name AND A.name  =  :name1", qp.getQuery());

	}

	@Test
	public void testSelectDottedPath() {

		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.condition(target.getDog().getName(), ConditionType.EQUALS, "tom");
		qp.condition(target.getDud().getName(), ConditionType.EQUALS, "tom3");

		Assert.assertEquals("select A from org.objectquery.jpaquerybuilder.domain.Person A where A.dog.name  =  :dog_name AND A.dud.name  =  :dud_name",
				qp.getQuery());

	}

	@Test
	public void testSelectProjection() {

		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.projection(target.getName());
		qp.condition(target.getDog().getName(), ConditionType.EQUALS, "tom");

		Assert.assertEquals("select A.name from org.objectquery.jpaquerybuilder.domain.Person A where A.dog.name  =  :dog_name", qp.getQuery());

	}

	@Test
	public void testProjectionCountThis() {

		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.projection(target, ProjectionType.COUNT);
		qp.condition(target.getDog().getName(), ConditionType.EQUALS, "tom");

		Assert.assertEquals("select  COUNT(A) from org.objectquery.jpaquerybuilder.domain.Person A where A.dog.name  =  :dog_name", qp.getQuery());

	}

	@Test
	public void testSelectOrder() {

		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.condition(target.getDog().getName(), ConditionType.EQUALS, "tom");
		qp.order(target.getName());

		Assert.assertEquals("select A from org.objectquery.jpaquerybuilder.domain.Person A where A.dog.name  =  :dog_name order by A.name", qp.getQuery());

	}

	@Test
	public void testSelectOrderAsc() {

		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.condition(target.getDog().getName(), ConditionType.EQUALS, "tom");
		qp.order(target.getName(), OrderType.ASC);

		Assert.assertEquals("select A from org.objectquery.jpaquerybuilder.domain.Person A where A.dog.name  =  :dog_name order by A.name ASC", qp.getQuery());

	}

	@Test
	public void testSelectOrderDesc() {

		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.condition(target.getDog().getName(), ConditionType.EQUALS, "tom");
		qp.order(target.getName(), OrderType.DESC);
		qp.order(target.getDog().getName(), OrderType.DESC);

		Assert.assertEquals(
				"select A from org.objectquery.jpaquerybuilder.domain.Person A where A.dog.name  =  :dog_name order by A.name DESC,A.dog.name DESC",
				qp.getQuery());

	}

	@Test
	public void testAllSimpleConditions() {

		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.condition(target.getName(), ConditionType.EQUALS, "tom");
		qp.condition(target.getName(), ConditionType.LIKE, "tom");
		qp.condition(target.getName(), ConditionType.MAX, "tom");
		qp.condition(target.getName(), ConditionType.MIN, "tom");
		qp.condition(target.getName(), ConditionType.MAX_EQUALS, "tom");
		qp.condition(target.getName(), ConditionType.MIN_EQUALS, "tom");
		qp.condition(target.getName(), ConditionType.NOT_EQUALS, "tom");

		Assert.assertEquals(
				"select A from org.objectquery.jpaquerybuilder.domain.Person A where A.name  =  :name AND A.name  like  :name1 AND A.name  >  :name2 AND A.name  <  :name3 AND A.name  >=  :name4 AND A.name  <=  :name5 AND A.name  <>  :name6",
				qp.getQuery());

	}

}
