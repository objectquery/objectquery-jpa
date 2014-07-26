package org.objectquery.jpa;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.objectquery.SelectMapQuery;
import org.objectquery.generic.GenericSelectQuery;
import org.objectquery.jpa.domain.Person;
import org.objectquery.jpa.domain.PersonDTO;

public class TestSelectMapQuery {

	@Test
	public void testSimpleSelectMap() {
		SelectMapQuery<Person, PersonDTO> query = new GenericSelectQuery<Person, PersonDTO>(Person.class, PersonDTO.class);
		query.prj(query.target().getName(), query.mapper().getName());

		assertThat(JPAObjectQuery.jpqlGenerator(query).getQuery(), CoreMatchers.is("select A.name as name from org.objectquery.jpa.domain.Person A"));

	}

}
