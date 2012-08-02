package org.objectquery.jpaquerybuilder;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.objectquery.builder.AbstractObjectQuery;
import org.objectquery.builder.GroupType;

public class JPQLObjectQuery<T> extends AbstractObjectQuery<T> {

	public JPQLObjectQuery(Class<T> clazz) {
		super(new JPQLQueryBuilder(GroupType.AND), clazz);
	}

	public Object execute(EntityManager entityManager) {
		Query qu = entityManager.createQuery(((JPQLQueryBuilder) getBuilder()).buildQuery());
		Map<String, Object> pars = ((JPQLQueryBuilder) getBuilder()).getParamenters();
		for (Map.Entry<String, Object> ent : pars.entrySet()) {
			qu.setParameter(ent.getKey(), ent.getValue());
		}
		return qu.getResultList();
	}
}
