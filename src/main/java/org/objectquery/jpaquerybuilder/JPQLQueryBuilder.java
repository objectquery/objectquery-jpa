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
			return " member of ";
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
			return " not member of ";
		case NOT_EQUALS:
			return " <> ";
		case NOT_IN:
			return " not in ";
		case NOT_LIKE:
			return "not like";
		}
		return "";
	}

	private void buildName(PathItem item, StringBuilder sb) {
		sb.append("A");
		if (item.getParent() != null)
			sb.append(".");
		buildPath(item, sb);
	}

	private String buildParameterName(ConditionItem cond) {
		StringBuilder name = new StringBuilder();
		buildParameterName(cond, name);
		int i = 1;
		String realName = name.toString();
		do {
			if (!parameters.containsKey(realName)) {
				parameters.put(realName, cond.getValue());
				return realName;
			}
			realName = name.toString() + i++;
		} while (true);
	}

	private void stringfyCondition(ConditionItem cond, StringBuilder sb) {

		if (cond.getType().equals(ConditionType.CONTAINS) || cond.getType().equals(ConditionType.NOT_CONTAINS)) {
			if (cond.getValue() instanceof PathItem) {
				buildName((PathItem) cond.getValue(), sb);
			} else {
				sb.append(":");
				sb.append(buildParameterName(cond));
			}
			sb.append(" ").append(getConditionType(cond.getType())).append(" ");
			buildName(cond.getItem(), sb);
		} else {
			buildName(cond.getItem(), sb);
			sb.append(" ").append(getConditionType(cond.getType())).append(" ");
			if (cond.getType().equals(ConditionType.IN) || cond.getType().equals(ConditionType.NOT_IN))
				sb.append("(");
			if (cond.getValue() instanceof PathItem) {
				buildName((PathItem) cond.getValue(), sb);
			} else {
				sb.append(":");
				sb.append(buildParameterName(cond));
			}
			if (cond.getType().equals(ConditionType.IN) || cond.getType().equals(ConditionType.NOT_IN))
				sb.append(")");
		}
	}

	private String resolveFunction(ProjectionType projectionType) {
		switch (projectionType) {
		case AVG:
			return "AVG";
		case MAX:
			return "MAX";
		case MIN:
			return "MIN";
		case COUNT:
			return "COUNT";
		}
		return "";
	}

	public String buildQuery(Class<?> clazz) {
		parameters.clear();
		StringBuilder builder = new StringBuilder();
		builder.append("select ");
		if (!getProjections().isEmpty()) {
			Iterator<Projection> projections = getProjections().iterator();
			while (projections.hasNext()) {
				Projection proj = projections.next();
				if (proj.getType() != null)
					builder.append(" ").append(resolveFunction(proj.getType())).append("(");
				buildName(proj.getItem(), builder);
				if (proj.getType() != null)
					builder.append(")");
				if (projections.hasNext())
					builder.append(",");
			}
		} else
			builder.append("A");
		builder.append(" from ").append(clazz.getName()).append(" A");
		if (!this.getConditions().isEmpty()) {
			builder.append(" where ");
			stringfyGroup(this, builder);
		}
		if (!getOrders().isEmpty()) {
			builder.append(" order by ");
			Iterator<Order> orders = getOrders().iterator();
			while (orders.hasNext()) {
				Order ord = orders.next();
				buildName(ord.getItem(), builder);
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
		return parameters;
	}

}
