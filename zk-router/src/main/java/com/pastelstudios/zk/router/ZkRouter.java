package com.pastelstudios.zk.router;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;

public class ZkRouter {

	private Map<String, ZkRoute> routesWithoutParams = new TreeMap<>();
	private Map<String, ZkRoute> routesWithParams = new TreeMap<>();
	private List<ZkRouteSecurityConstraint> securityConstraints = new LinkedList<>();
	private Component contentHolder = null;
	private Component content = null;
	private ZkRoutingErrorHandler routingErrorHandler = new NoOpRoutingErrorHandler();

	public ZkRouter(Map<String, ZkRoute> routesWithoutParams, Map<String, ZkRoute> routesWithParams, List<ZkRouteSecurityConstraint> securityConstraints) {
		this.routesWithoutParams = routesWithoutParams;
		this.routesWithParams = routesWithParams;
		this.securityConstraints = securityConstraints;
	}

	private String removeFirstAndLastSlash(String url) {
		url = url.replaceAll("^/", "");
		url = url.replaceAll("/$", "");
		return url;
	}

	public void setContentHolder(Component contentHolder) {
		this.contentHolder = contentHolder;
	}

	public Component getContentHolder() {
		return contentHolder;
	}

	public void setRoutingErrorHandler(ZkRoutingErrorHandler routeMissingHandler) {
		this.routingErrorHandler = routeMissingHandler;
	}

	public void dispatch(String url) {
		url = removeFirstAndLastSlash(url);
		
		ZkRouteSecurityConstraint securityConstraint = findSecurityConstraint(url);
		if(securityConstraint != null && !securityConstraint.hasAccess()) {
			if(routingErrorHandler != null) {
				routingErrorHandler.handleAccessDenied(url);
			}
			return;
		}
		
		ZkRoute route = findRoute(url);
		if (route == null) {
			if (routingErrorHandler != null) {
				routingErrorHandler.handleMissingRoute(url);
			}
		} else {			
			Map<String, Object> pathVariables = new HashMap<>();
			try {
				pathVariables = route.resolvePathVariables(url);
			} catch (RuntimeException e) {
				if(routingErrorHandler != null) {
					routingErrorHandler.handleInvalidRoute(url, e);
					return;
				} else {
					throw e;
				}
			}
			
			if (content != null) {
				content.detach();
				content = null;
			}
			content = Executions.createComponents(route.getView(), contentHolder, pathVariables);
		}
	}
	
	private ZkRouteSecurityConstraint findSecurityConstraint(String url) {
		for(ZkRouteSecurityConstraint securityConstraint : securityConstraints) {
			if(securityConstraint.matches(url)) {
				return securityConstraint;
			}
		}
		return null;
	}

	private ZkRoute findRoute(String url) {
		for (String testUrl : routesWithoutParams.keySet()) {
			ZkRoute route = routesWithoutParams.get(testUrl);
			if (route.matches(url)) {
				return route;
			}
		}

		for (String testUrl : routesWithParams.keySet()) {
			ZkRoute route = routesWithParams.get(testUrl);
			if (route.matches(url)) {
				return route;
			}
		}

		return null;
	}
	
	public void goTo(String url) {
		url = url.replaceAll("#", "");
		Clients.evalJavaScript("window.location.hash = '#" + url + "';");
	}
	
	public static ZkRouter getCurrent() {
		Execution execution = Executions.getCurrent();
		if(execution != null && execution.getDesktop() != null) {
			Object router = execution.getDesktop().getAttribute("router");
			if(router != null && router instanceof ZkRouter) {
				return (ZkRouter) router;
			}
		}
		return null;
	}
}
