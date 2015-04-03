package fr.stevecohen.rpc;

import java.lang.reflect.Method;

import fr.stevecohen.services.AbstractService;

public class RpcObject {
	private AbstractService service;
	private Method method;
	
	public RpcObject(AbstractService service, Method method) {
		this.service = service;
		this.method = method;
	}
	
	public AbstractService getService() {
		return service;
	}
	public void setService(AbstractService service) {
		this.service = service;
	}
	public Method getMethodName() {
		return method;
	}
	public void setMethodName(Method method) {
		this.method = method;
	}
}
