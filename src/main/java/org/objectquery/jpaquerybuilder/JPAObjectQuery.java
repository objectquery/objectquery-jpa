package org.objectquery.jpaquerybuilder;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.objectquery.builder.GenericObjectQuery;

public class JPAObjectQuery {

	public static JPQLQueryGenerator jpqlGenerator(GenericObjectQuery<?> objectQuery) {
		return new JPQLQueryGenerator(objectQuery);
	}

	public static Query buildQuery(GenericObjectQuery<?> objectQuery, EntityManager entityManager) {
		JPQLQueryGenerator gen = jpqlGenerator(objectQuery);
		Query qu = entityManager.createQuery(gen.getQuery());
		Map<String, Object> pars = gen.getParamenters();
		for (Map.Entry<String, Object> ent : pars.entrySet()) {
			qu.setParameter(ent.getKey(), ent.getValue());
		}
		return qu;
	}

}
