package com.pastelstudios.zk.router;

import org.zkoss.xel.VariableResolver;
import org.zkoss.xel.XelException;

public class RouterVariableResolver implements VariableResolver {

	@Override
	public Object resolveVariable(String variableName) throws XelException {
		if("router".equals(variableName)) {
			return ZkRouter.getCurrent();
		}
		return null;
	}

}
