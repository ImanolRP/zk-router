package com.pastelstudios.zk.router.type;

public class LongPathVariableType implements PathVariableType<Long> {

	@Override
	public Long fromString(String value) {
		return Long.valueOf(value);
	}

	@Override
	public String getName() {
		return "long";
	}

}
