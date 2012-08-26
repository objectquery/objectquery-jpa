package org.objectquery.jpaquerybuilder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.objectquery.builder.AbstractInternalQueryBuilder;
import org.objectquery.builder.ConditionElement;
import org.objectquery.builder.ConditionGroup;
import org.objectquery.builder.ConditionItem;
import org.objectquery.builder.ConditionType;
import org.objectquery.builder.GroupType;
import org.objectquery.builder.Order;
import org.objectquery.builder.PathItem;
import org.objectquery.builder.Projection;
import org.objectquery.builder.ProjectionType;

public class JPQLQueryBuilder extends AbstractInternalQueryBuilder {

	private Map<String, Object> parameters = new HashMap<String, Object>();

	protected JPQLQueryBuilder(GroupType type) {
		super(type);
	}

	private void stringfyGroup(ConditionGroup group, StringBuilder builder) {
		if (!group.getConditions().isEmpty()) {
			Iterator<ConditionElement> eli = group.getConditions().iterator();
			while (eli.hasNext()) {
				ConditionElement el = eli.next();
				if (el instanceof ConditionItem) {
					stringfyCondition((ConditionItem) el, builder);
				} else if (el instanceof ConditionGroup) {
					builder.append(" ( ");
					stringfyGroup((ConditionGroup) el, builder);
					builder.append(" ) ");
				}
				if (eli.hasNext()) {
					builder.append(" ").append(group.getType().toString()).append(" ");
				}
			}
		}
	}

	private String getConditionType(ConditionType type) {
		switch (type) {
		case CONTAINS:
			return " contains ";
		case EQUALS:
			return " = ";
		case IN:
			return " in ";
		case LIKE:
			return " like ";
		case MAX:
			return " > ";
		case MIN:
			return " < ";
		case MAX_EQUALS:
			return " >= ";
		case MIN_EQUALS:
			return " <= ";
		case NOT_CONTAINS:
			return "";
		case NOT_EQUALS:
			return " <> ";
		case NOT_IN:
			return " not in ";
		case NOT_LIKE:
			return "not like";
		}
		return "";
	}

	private void stringfyCondition(ConditionItem cond, StringBuilder sb) {
		buildPath(cond.getItem(), sb);
		sb.append(" ").append(getConditionType(cond.getType())).append(" ");
		if (cond.getValue() instanceof PathItem)
			buildPath((PathItem) cond.getValue(), sb);
		else {
			sb.append(":");
			StringBuilder name = new StringBuilder();
			buildParameterName(cond, name);
			int i = 1;
			String realName = name.toString();
			do {
				if (!parameters.containsKey(realName)) {
					parameters.put(realName, cond.getValue());
					sb.append(realName);
					break;
				}
				realName = name.toString() + i++;
			} while (true);
		}
	}

	private String resolveFunction(ProjectionType projectionType) {
		return "nada";
	}

	public String buildQuery(Class<?> clazz) {
		parameters.clear();
		StringBuilder builder = new StringBuilder();
		builder.append("select ");
		Iterator<Projection> projections = getProjections().iterator();
		while (projections.hasNext()) {
			Projection proj = projections.next();
			if (proj.getType() != null)
				builder.append(" ").append(resolveFunction(proj.getType())).append("(");

			buildPath(proj.getItem(), builder);
			if (proj.getType() != null)
				builder.append(")");
			if (projections.hasNext())
				builder.append(",");
		}
		builder.append(" from ").append(clazz.getName());
		if (!this.getConditions().isEmpty()) {
			builder.append(" where ");
			stringfyGroup(this, builder);
		}
		if (!getOrders().isEmpty()) {
			builder.append("order by ");
			Iterator<Order> orders = getOrders().iterator();
			while (orders.hasNext()) {
				Order ord = orders.next();
				buildPath(ord.getItem(), builder);
				if (ord.getType() != null)
					builder.append(" ").append(ord.getType());
				if (orders.hasNext())
					builder.append(',');
			}
		}
		return builder.toString();
	}

	private void buildParameterName(ConditionItem conditionItem, StringBuilder builder) {
		buildPath(conditionItem.getItem(), builder, "_");
	}

	public Map<String, Object> getParamenters() {
		return null;
	}

}
