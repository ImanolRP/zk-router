package com.pastelstudios.zk.router.spring;

import java.util.List;

import com.pastelstudios.zk.router.ZkRouterFactory;

public class ZkRouterFactoryBean extends ZkRouterFactory {

	public void setRoutes(List<String> routes) {
		for(String routeDefinition : routes) {
			String[] parts = routeDefinition.split("=>");
			if(parts.length != 2) {
				throw new IllegalArgumentException("Cannot parse route " + routeDefinition + ". Routes must be defined as 'url => view'!");
			}
			String url = parts[0].toLowerCase().trim();
			String view = parts[1].trim();
			addRoute(url, view);
		}
	}
	
	public void setSecurityConstraints(List<String> constraints) {
		for(String constraintDefinition : constraints) {
			String[] parts = constraintDefinition.split("=>");
			if(parts.length != 2) {
				throw new IllegalArgumentException("Cannot parse constraint " + constraintDefinition + ". Security constraints must be defined as 'path => roles'!");
			}
			String path = parts[0].trim().toLowerCase();
			String roles = parts[1].trim().toUpperCase();
			addSecurityConstraint(path, roles);
		}
	}
}
