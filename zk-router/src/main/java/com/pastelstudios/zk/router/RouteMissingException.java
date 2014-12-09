package com.pastelstudios.zk.router;

public class RouteMissingException extends RouterException {

	private static final long serialVersionUID = -4834275009870668990L;
	private static final String ROUTE_MISSING_CODE = "com.pastelstudios.zk.router.MissingRoute";

	public RouteMissingException() {
		super(ROUTE_MISSING_CODE);
	}
	
}
