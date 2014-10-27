package com.pastelstudios.zk.router;

public class NoOpRoutingErrorHandler implements ZkRoutingErrorHandler {

	@Override
	public void handleMissingRoute(String url) {
		
	}

	@Override
	public void handleInvalidRoute(String url, RuntimeException e) {
		if(!(e instanceof NumberFormatException)) {
			throw e;
		}
	}

	@Override
	public void handleAccessDenied(String url) {
		System.err.println("access denied");
	}

}
