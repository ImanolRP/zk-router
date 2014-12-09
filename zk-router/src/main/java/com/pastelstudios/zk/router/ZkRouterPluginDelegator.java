package com.pastelstudios.zk.router;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;

public class ZkRouterPluginDelegator implements ZkRouterPlugin {

	private List<ZkRouterPlugin> plugins;
	private Map<Class<?>, ZkRouterPlugin> pluginLocator = new HashMap<>();
	
	public ZkRouterPluginDelegator(List<ZkRouterPlugin> plugins) {
		this.plugins = plugins;
		for(ZkRouterPlugin plugin : plugins) {
			pluginLocator.put(plugin.getClass(), plugin);
		}
	}
	
	@Override
	public void beforeRouting(ZkRouter router, Component content, String url) throws RouterException {
		for(ZkRouterPlugin plugin : plugins) {
			plugin.beforeRouting(router, content, url);
		}
	}

	@Override
	public void beforeContentChanged(ZkRouter router, Component content) throws RouterException {
		for(ZkRouterPlugin plugin : plugins) {
			plugin.beforeContentChanged(router, content);
		}
	}

	@Override
	public void afterContentChanged(ZkRouter router, Component content) {
		for(ZkRouterPlugin plugin : plugins) {
			plugin.afterContentChanged(router, content);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ZkRouterPlugin > T getPlugin(Class<T> pluginClass) {
		return (T) pluginLocator.get(pluginClass);
	}

}
