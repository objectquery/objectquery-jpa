package org.objectquery.jpaquerybuilder;

import org.junit.Assert;
import org.junit.Test;
import org.objectquery.builder.ConditionType;
import org.objectquery.jpaquerybuilder.domain.Person;

public class TestSimpleQuery {

	@Test
	public void testSelect() {

		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.condition(target.getName(), ConditionType.EQUALS, "tom");

		Assert.assertEquals("select  from org.objectquery.jpaquerybuilder.domain.Person where name  =  :name", qp.getQuery());

	}

	@Test
	public void testSelectDupliedPath() {

		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.condition(target.getName(), ConditionType.EQUALS, "tom");
		qp.condition(target.getName(), ConditionType.EQUALS, "tom3");

		Assert.assertEquals("select  from org.objectquery.jpaquerybuilder.domain.Person where name  =  :name AND name  =  :name1", qp.getQuery());

	}

	@Test
	public void testSelectDottedPath() {

		JPQLObjectQuery<Person> qp = new JPQLObjectQuery<Person>(Person.class);
		Person target = qp.target();
		qp.condition(target.getDog().getName(), ConditionType.EQUALS, "tom");
		qp.condition(target.getDud().getName(), ConditionType.EQUALS, "tom3");

		Assert.assertEquals("select  from org.objectquery.jpaquerybuilder.domain.Person where dog.name  =  :dog_name AND dud.name  =  :dud_name", qp.getQuery());

	}

}
