package com.pastelstudios.zk.router.type;

public class BytePathVariableType implements PathVariableType<Byte> {

	@Override
	public Byte fromString(String value) {
		return Byte.valueOf(value);
	}

	@Override
	public String getName() {
		return "byte";
	}

}
