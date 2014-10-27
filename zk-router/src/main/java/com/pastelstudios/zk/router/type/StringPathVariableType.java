package com.pastelstudios.zk.router.type;

public class StringPathVariableType implements PathVariableType<String> {

	@Override
	public String fromString(String value) {
		return value;
	}

	@Override
	public String getName() {
		return "string";
	}

}
