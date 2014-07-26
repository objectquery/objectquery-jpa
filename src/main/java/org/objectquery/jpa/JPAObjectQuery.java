package org.objectquery.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.objectquery.BaseQuery;
import org.objectquery.DeleteQuery;
import org.objectquery.SelectMapQuery;
import org.objectquery.SelectQuery;
import org.objectquery.UpdateQuery;
import org.objectquery.generic.GenericBaseQuery;
import org.objectquery.generic.GenericInternalQueryBuilder;
import org.objectquery.generic.GenericSelectQuery;
import org.objectquery.generic.ObjectQueryException;
import org.objectquery.generic.Projection;

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

	@SuppressWarnings("unchecked")
	public static <M> List<M> execute(SelectMapQuery<?, M> objectQuery, EntityManager entityManager) {
		Query query = buildQuery(objectQuery, entityManager);
		GenericSelectQuery<?, M> gq = (GenericSelectQuery<?, M>) objectQuery;
		List<Projection> projections = ((GenericInternalQueryBuilder) gq.getBuilder()).getProjections();
		List<M> result = new ArrayList<>();
		if (projections.size() == 1) {
			Projection prj = projections.get(0);
			StringBuilder builder = new StringBuilder();
			GenericInternalQueryBuilder.buildAlias(prj, builder);
			String name = builder.toString();
			Map<String, Object> values = new HashMap<String, Object>();
			for (Object value : (List<Object>) query.getResultList()) {
				values.put(name, value);
				result.add(GenericInternalQueryBuilder.setMapping(gq.getMapperClass(), projections, values));
			}
		} else {
			List<String> names = new ArrayList<>();
			StringBuilder builder = new StringBuilder();
			for (Projection prj : projections) {
				builder.setLength(0);
				GenericInternalQueryBuilder.buildAlias(prj, builder);
				names.add(builder.toString());
			}
			Map<String, Object> values = new HashMap<>();
			for (Object[] val : (List<Object[]>) query.getResultList()) {
				values.clear();
				for (int i = 0; i < val.length; i++) {
					values.put(names.get(i), val[i]);
				}
				result.add(GenericInternalQueryBuilder.setMapping(gq.getMapperClass(), projections, values));
			}
		}
		return result;
	}

	public static int execute(DeleteQuery<?> dq, EntityManager entityManager) {
		return buildQuery(dq, entityManager).executeUpdate();
	}

	public static int execute(UpdateQuery<?> query, EntityManager entityManager) {
		return buildQuery(query, entityManager).executeUpdate();
	}

}
