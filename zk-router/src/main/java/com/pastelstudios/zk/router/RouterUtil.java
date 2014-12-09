package com.pastelstudios.zk.router;

public class RouterUtil {

	public static String removeFirstAndLastSlash(String url) {
		url = url.replaceAll("^/", "");
		url = url.replaceAll("/$", "");
		return url;
	}
}
