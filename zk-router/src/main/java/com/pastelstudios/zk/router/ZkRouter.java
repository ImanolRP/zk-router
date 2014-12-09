package com.pastelstudios.zk.router;

import java.util.HashMap;
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
	private ZkRouterPluginDelegator pluginDelegator = null;
	private Component contentHolder = null;
	private Component content = null;
	private ZkRoutingErrorHandler routingErrorHandler = new NoOpRoutingErrorHandler();

	public ZkRouter(Map<String, ZkRoute> routesWithoutParams, Map<String, ZkRoute> routesWithParams, List<ZkRouterPlugin> plugins) {
		this.routesWithoutParams = routesWithoutParams;
		this.routesWithParams = routesWithParams;
		this.pluginDelegator = new ZkRouterPluginDelegator(plugins);
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
		url = RouterUtil.removeFirstAndLastSlash(url);
		try {
			tryDispatch(url);
		} catch (RouterException e) {
			if(routingErrorHandler != null) {
				routingErrorHandler.handleRoutingError(e);
			}
		}
	}
	
	private void tryDispatch(String url) throws RouterException {
		pluginDelegator.beforeRouting(this, content, url);
		
		ZkRoute route = findRoute(url);
		if (route == null) {
			throw new RouteMissingException();
		}		
		Map<String, Object> pathVariables = new HashMap<>();
		try {
			pathVariables = route.resolvePathVariables(url);
		} catch (RuntimeException e) {
			throw new InvalidRouteException(e);
		}
		
		pluginDelegator.beforeContentChanged(this, content);
		if (content != null) {
			content.detach();
			content = null;
		}
		content = Executions.createComponents(route.getView(), contentHolder, pathVariables);
		pluginDelegator.afterContentChanged(this, content);
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
	
	public <T extends ZkRouterPlugin> T getPlugin(Class<T> pluginClass) {
		return pluginDelegator.getPlugin(pluginClass);
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