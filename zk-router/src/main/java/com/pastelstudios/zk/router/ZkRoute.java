package com.pastelstudios.zk.router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pastelstudios.zk.router.type.BytePathVariableType;
import com.pastelstudios.zk.router.type.IntegerPathVariableType;
import com.pastelstudios.zk.router.type.LongPathVariableType;
import com.pastelstudios.zk.router.type.PathVariableType;
import com.pastelstudios.zk.router.type.ShortPathVariableType;
import com.pastelstudios.zk.router.type.StringPathVariableType;

public class ZkRoute {

	private String url;
	private String view;
	private Pattern urlPattern;
	private Map<Integer, VariableAndType> pathVariables = new HashMap<>();
	private List<PathVariableType<?>> pathVariableTypes = new ArrayList<>();

	public ZkRoute(String url, String view) {
		this.url = url;
		this.view = view;
		initPathvariableTypes();
		resolveUrlPattern(url);
	}

	private void initPathvariableTypes() {
		pathVariableTypes.add(new BytePathVariableType());
		pathVariableTypes.add(new IntegerPathVariableType());
		pathVariableTypes.add(new LongPathVariableType());
		pathVariableTypes.add(new ShortPathVariableType());
		pathVariableTypes.add(new StringPathVariableType());
	}

	private void resolveUrlPattern(String url) {
		String urlRegexp = url.toLowerCase().replaceAll("\\{[a-zA-Z0-9:]+?\\}", "([^/\\]+)");
		urlPattern = Pattern.compile(urlRegexp);

		String varPatternRegexp = url.replaceAll("\\{[a-zA-Z0-9:]+?\\}", "\\\\{([a-zA-Z0-9:]+)\\\\}");
		Pattern varPattern = Pattern.compile(varPatternRegexp);
		Matcher varMatcher = varPattern.matcher(url);
		if (varMatcher.matches() && varMatcher.groupCount() >= 1) {
			for (int i = 1; i <= varMatcher.groupCount(); i++) {
				String varGroup = varMatcher.group(i);
				registerVariable(varGroup, i);
			}
		}
	}

	private void registerVariable(String varGroup, int groupNumber) {
		String[] parts = varGroup.split(":");
		if(parts.length > 2) {
			throw new IllegalArgumentException("Cannot parse variable " + varGroup + ". Expected input is 'varName[:varType]'!");
		}
		PathVariableType<?> resolvedType = new StringPathVariableType();
		if(parts.length == 2) {
			String type = parts[1].trim().toLowerCase();
			resolvedType = resolvePathVariableType(type);
		}
		String varName = parts[0].trim();
		pathVariables.put(groupNumber, new VariableAndType(varName, resolvedType));
	}

	private PathVariableType<?> resolvePathVariableType(String type) {
		for(PathVariableType<?> pathVariableType : pathVariableTypes) {
			if(pathVariableType.getName().equals(type)) {
				return pathVariableType;
			}
		}
		return new StringPathVariableType();
	}

	public String getUrl() {
		return url;
	}

	public String getView() {
		return view;
	}

	public boolean matches(String url) {
		if (urlPattern.matcher(url).matches()) {
			return true;
		}
		return false;
	}

	public Map<String, Object> resolvePathVariables(String url) {
		Map<String, Object> pathVariables = new HashMap<>();
		Matcher matcher = urlPattern.matcher(url);
		if (matcher.matches() && matcher.groupCount() >= 1) {
			for (int i = 1; i <= matcher.groupCount(); i++) {
				String varGroup = matcher.group(i);
				VariableAndType pathVariable = this.pathVariables.get(i);
				Object value = pathVariable.getType().fromString(varGroup);
				pathVariables.put(pathVariable.getVarName(), value);
			}
		}
		return pathVariables;
	}

	private class VariableAndType {
		private String varName;
		private PathVariableType<?> type;

		public VariableAndType(String varName, PathVariableType<?> type) {
			this.varName = varName;
			this.type = type;
		}

		public String getVarName() {
			return varName;
		}

		public PathVariableType<?> getType() {
			return type;
		}

	}

}