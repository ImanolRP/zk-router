package com.pastelstudios.zk.router;

public class RouterException extends Exception {

	private static final long serialVersionUID = -4593379991749366983L;

	public RouterException(String message) {
		super(message);
	}
	
	public RouterException(String message, Throwable cause) {
		super(message, cause);
	}
}
