package com.pastelstudios.zk.router.type;

public class IntegerPathVariableType implements PathVariableType<Integer> {

	@Override
	public Integer fromString(String value) {
		return Integer.valueOf(value);
	}

	@Override
	public String getName() {
		return "int";
	}

}
