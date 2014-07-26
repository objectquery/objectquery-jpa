package org.objectquery.jpa;

import java.util.List;

import javax.persistence.EntityManager;

import org.objectquery.DeleteQuery;
import org.objectquery.InsertQuery;
import org.objectquery.QueryEngine;
import org.objectquery.SelectMapQuery;
import org.objectquery.SelectQuery;
import org.objectquery.UpdateQuery;

public class JPAQueryEngine extends QueryEngine<EntityManager> {

	@Override
	public List<?> execute(SelectQuery<?> query, EntityManager engineSession) {
		return JPAObjectQuery.execute(query, engineSession);
	}

	@Override
	public int execute(DeleteQuery<?> dq, EntityManager engineSession) {
		return JPAObjectQuery.execute(dq, engineSession);
	}

	@Override
	public boolean execute(InsertQuery<?> ip, EntityManager engineSession) {
		throw new UnsupportedOperationException("JPA Query Engine doesn't support insert query");
	}

	@Override
	public int execute(UpdateQuery<?> query, EntityManager engineSession) {
		return JPAObjectQuery.execute(query, engineSession);
	}

	@Override
	public <M> List<M> execute(SelectMapQuery<?, M> query, EntityManager engineSession) {
		return JPAObjectQuery.execute(query, engineSession);
	}
}