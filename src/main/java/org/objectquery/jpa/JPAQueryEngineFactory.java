package org.objectquery.jpa;

import javax.persistence.EntityManager;

import org.objectquery.QueryEngine;
import org.objectquery.QueryEngineFactory;

public class JPAQueryEngineFactory implements QueryEngineFactory {

	@Override
	public <S> QueryEngine<S> createQueryEngine(Class<S> targetSession) {
		if (EntityManager.class.equals(targetSession))
			return createDefaultQueryEngine();
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> QueryEngine<T> createDefaultQueryEngine() {
		return (QueryEngine<T>) new JPAQueryEngine();
	}

}
