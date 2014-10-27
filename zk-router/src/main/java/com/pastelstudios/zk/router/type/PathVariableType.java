package com.pastelstudios.zk.router.type;

public interface PathVariableType<T> {

	public String getName();
	public T fromString(String value);
}
