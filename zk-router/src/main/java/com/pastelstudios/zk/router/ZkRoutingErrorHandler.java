package com.pastelstudios.zk.router;

public interface ZkRoutingErrorHandler {

	void handleMissingRoute(String url);
	void handleInvalidRoute(String url, RuntimeException e);
	void handleAccessDenied(String url);
}
