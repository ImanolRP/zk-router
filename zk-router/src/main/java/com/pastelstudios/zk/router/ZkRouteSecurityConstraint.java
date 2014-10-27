package com.pastelstudios.zk.router;

import java.util.regex.Pattern;

import org.zkoss.spring.security.SecurityUtil;

public class ZkRouteSecurityConstraint {

	private Pattern pattern;
	private String roles;
	
	public ZkRouteSecurityConstraint(String path, String roles) {
		if(path == null || path.trim().isEmpty()) {
			throw new IllegalArgumentException("You must a provide a non empty path!");
		}
		if(roles == null || roles.trim().isEmpty()) {
			throw new IllegalArgumentException("You must define at least 1 role for path " + path);
		}
		
		path = path.trim().toLowerCase();
		String regexp = path.replaceAll("\\*", ".*");
		pattern = Pattern.compile(regexp);
		
		this.roles = roles.trim().toUpperCase();
	}
	
	public boolean matches(String url) {
		return pattern.matcher(url).matches();
	}
	
	public boolean hasAccess() {
		return SecurityUtil.isAnyGranted(roles);
	}
}
