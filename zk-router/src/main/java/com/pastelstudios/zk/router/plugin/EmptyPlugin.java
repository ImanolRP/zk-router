package com.pastelstudios.zk.router.plugin;

import org.zkoss.zk.ui.Component;

import com.pastelstudios.zk.router.RouterException;
import com.pastelstudios.zk.router.ZkRouter;
import com.pastelstudios.zk.router.ZkRouterPlugin;

public class EmptyPlugin implements ZkRouterPlugin {

	@Override
	public void beforeRouting(ZkRouter router, Component content, String url) throws RouterException {
	}

	@Override
	public void beforeContentChanged(ZkRouter router, Component content) throws RouterException {
	}

	@Override
	public void afterContentChanged(ZkRouter router, Component content) {
	}
}
