package com.pastelstudios.zk.router;

public class InvalidRouteException extends RouterException {

	private static final long serialVersionUID = 1591173165516450052L;
	private static final String ROUTE_INVALID_CODE = "bg.demax.zk.router.InvalidRoute";
	
	public InvalidRouteException(RuntimeException e) {
		super(ROUTE_INVALID_CODE, e);
	}

}
