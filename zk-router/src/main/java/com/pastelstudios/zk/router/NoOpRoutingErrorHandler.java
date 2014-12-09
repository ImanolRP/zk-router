package com.pastelstudios.zk.router;

public class NoOpRoutingErrorHandler implements ZkRoutingErrorHandler {

	@Override
	public void handleRoutingError(RouterException e) {
	}

}
