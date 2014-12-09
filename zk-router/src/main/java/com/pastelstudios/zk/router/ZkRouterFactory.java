package com.pastelstudios.zk.router;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

public class ZkRouterFactory {

	private Map<String, ZkRoute> routesWithoutParams = new TreeMap<>();
	private Map<String, ZkRoute> routesWithParams = new TreeMap<>();
	private ZkRoutingErrorHandler defaultRoutingErrorHandler = new NoOpRoutingErrorHandler();
	private List<ZkRouterPlugin> plugins = new LinkedList<>();
	
	public ZkRoute addRoute(String url, String view) {
		url = removeFirstAndLastSlash(url);
		ZkRoute route = new ZkRoute(url, view);
		if (url.contains("{")) {
			routesWithParams.put(url, route);
		} else {
			routesWithoutParams.put(url, route);
		}
		return route;
	}

	private String removeFirstAndLastSlash(String url) {
		url = url.replaceAll("^/", "");
		url = url.replaceAll("/$", "");
		return url;
	}
	
	public ZkRouter createRouter() {
		ZkRouter router = new ZkRouter(routesWithoutParams, routesWithParams, plugins);
		router.setRoutingErrorHandler(defaultRoutingErrorHandler);
		Executions.getCurrent().getDesktop().setAttribute("router", router);
		return router;
	}
	
	public ZkRouter createRouter(Component contentHolder) {
		ZkRouter router = new ZkRouter(routesWithoutParams, routesWithParams, plugins);
		router.setContentHolder(contentHolder);
		router.setRoutingErrorHandler(defaultRoutingErrorHandler);
		Executions.getCurrent().getDesktop().setAttribute("router", router);
		return router;
	}

	public void setDefaultRoutingErrorHandler(ZkRoutingErrorHandler errorHandler) {
		this.defaultRoutingErrorHandler = errorHandler;
	}

	public List<ZkRouterPlugin> getPlugins() {
		return plugins;
	}

	public void setPlugins(List<ZkRouterPlugin> plugins) {
		if(plugins == null) {
			this.plugins = new LinkedList<>();
		} else {
			this.plugins = plugins;
		}
	}
}
