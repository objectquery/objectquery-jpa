package org.objectquery.jpa;

import java.util.List;

import javax.persistence.EntityManager;

import org.objectquery.DeleteQuery;
import org.objectquery.InsertQuery;
import org.objectquery.QueryEngine;
import org.objectquery.SelectQuery;
import org.objectquery.UpdateQuery;

public class JPAQueryEngine extends QueryEngine<EntityManager> {

	@Override
	public <RET extends List<?>> RET execute(SelectQuery<?> query, EntityManager engineSession) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int execute(DeleteQuery<?> dq, EntityManager engineSession) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean execute(InsertQuery<?> ip, EntityManager engineSession) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int execute(UpdateQuery<?> query, EntityManager engineSession) {
		// TODO Auto-generated method stub
		return 0;
	}

}