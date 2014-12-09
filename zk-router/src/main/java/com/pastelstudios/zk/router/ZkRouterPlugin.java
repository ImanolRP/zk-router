package com.pastelstudios.zk.router;

import org.zkoss.zk.ui.Component;

public interface ZkRouterPlugin {

	void beforeRouting(ZkRouter router, Component content, String url) throws RouterException;
	void beforeContentChanged(ZkRouter router, Component content) throws RouterException;
	void afterContentChanged(ZkRouter router, Component content);
}
