package org.objectquery.jpa;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.objectquery.BaseQuery;
import org.objectquery.DeleteQuery;
import org.objectquery.SelectQuery;
import org.objectquery.UpdateQuery;
import org.objectquery.generic.GenericBaseQuery;
import org.objectquery.generic.ObjectQueryException;

public class JPAObjectQuery {

	public static JPQLQueryGenerator jpqlGenerator(BaseQuery<?> objectQuery) {
		if (objectQuery instanceof GenericBaseQuery<?>)
			return new JPQLQueryGenerator((GenericBaseQuery<?>) objectQuery);
		throw new ObjectQueryException("The Object query instance of unconvertable implementation ");
	}

	public static Query buildQuery(BaseQuery<?> objectQuery, EntityManager entityManager) {
		JPQLQueryGenerator gen = jpqlGenerator(objectQuery);
		Query qu = entityManager.createQuery(gen.getQuery());
		Map<String, Object> pars = gen.getParameters();
		for (Map.Entry<String, Object> ent : pars.entrySet()) {
			qu.setParameter(ent.getKey(), ent.getValue());
		}
		return qu;
	}

	public static List<?> execute(SelectQuery<?> objectQuery, EntityManager entityManager) {
		return buildQuery(objectQuery, entityManager).getResultList();
	}

	public static int execute(DeleteQuery<?> dq, EntityManager entityManager) {
		return buildQuery(dq, entityManager).executeUpdate();
	}

	public static int execute(UpdateQuery<?> query, EntityManager entityManager) {
		return buildQuery(query, entityManager).executeUpdate();
	}

}
