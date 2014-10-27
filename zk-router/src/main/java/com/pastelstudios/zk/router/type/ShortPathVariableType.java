package com.pastelstudios.zk.router.type;

public class ShortPathVariableType implements PathVariableType<Short> {

	@Override
	public Short fromString(String value) {
		return Short.valueOf(value);
	}

	@Override
	public String getName() {
		return "short";
	}

}
