package org.objectquery.jpaobjectquery;

import org.objectquery.ObjectQuery;
import org.objectquery.generic.GenericObjectQuery;
import org.objectquery.generic.domain.Person;

public class TestJoinQuery {

	public void testSimpleJoin() {
		ObjectQuery<Person> query = new GenericObjectQuery<Person>(Person.class);
		query.join(Person.class);
	}

}
