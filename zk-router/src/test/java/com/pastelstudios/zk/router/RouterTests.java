package com.pastelstudios.zk.router;

import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RouterTests {

	private static ZkRouterFactory routerFactory;
	
	@BeforeClass
	public static void initRouterFactory() {
		routerFactory = new ZkRouterFactory();
		routerFactory.addRoute("/test", "test");
		routerFactory.addRoute("/testDefault/{id}", "test");
		routerFactory.addRoute("/testInt/{id:int}", "test");
		routerFactory.addRoute("/testLong/{id:long}", "test");
	}
	
	@Test
	public void plainUrlMatchesProperly() {
		ZkRouterFactory routerFactory = new ZkRouterFactory();
		ZkRoute route = routerFactory.addRoute("/test", "test");
		boolean matches = route.matches("test");
		Assert.assertTrue(matches);
		matches = route.matches("asd");
		Assert.assertFalse(matches);
	}
	
	@Test
	public void resolvesNormalParameter() {
		ZkRouterFactory routerFactory = new ZkRouterFactory();
		ZkRoute route = routerFactory.addRoute("test/{id}", "test");
		Assert.assertTrue(route.matches("test/123"));
		Assert.assertFalse(route.matches("test/123/1"));
		Map<String, Object> vars = route.resolvePathVariables("test/123");
		Assert.assertTrue(vars.containsKey("id"));
		Assert.assertTrue(vars.get("id") instanceof String);
		Assert.assertEquals("123", vars.get("id"));
	}
	
	@Test
	public void resolvesIntParameter() {
		ZkRouterFactory routerFactory = new ZkRouterFactory();
		ZkRoute route = routerFactory.addRoute("test/{id:int}", "test");
		Assert.assertTrue(route.matches("test/123"));
		Map<String, Object> vars = route.resolvePathVariables("test/123");
		Assert.assertTrue(vars.containsKey("id"));
		Assert.assertTrue(vars.get("id") instanceof Integer);
		Assert.assertEquals(123, vars.get("id"));
	}
	
	@Test
	public void resolvesLongParameter() {
		ZkRouterFactory routerFactory = new ZkRouterFactory();
		ZkRoute route = routerFactory.addRoute("test/{id:long}", "test");
		Assert.assertTrue(route.matches("test/123"));
		Map<String, Object> vars = route.resolvePathVariables("test/123");
		Assert.assertTrue(vars.containsKey("id"));
		Assert.assertTrue(vars.get("id") instanceof Long);
		Assert.assertEquals(123L, vars.get("id"));
	}
	
	@Test
	public void resolvesShortParameter() {
		ZkRouterFactory routerFactory = new ZkRouterFactory();
		ZkRoute route = routerFactory.addRoute("test/{id:short}", "test");
		Assert.assertTrue(route.matches("test/123"));
		Map<String, Object> vars = route.resolvePathVariables("test/123");
		Assert.assertTrue(vars.containsKey("id"));
		Assert.assertTrue(vars.get("id") instanceof Short);
		Assert.assertEquals((short) 123, vars.get("id"));
	}
	
	@Test
	public void resolvesByteParameter() {
		ZkRouterFactory routerFactory = new ZkRouterFactory();
		ZkRoute route = routerFactory.addRoute("test/{id:byte}", "test");
		Assert.assertTrue(route.matches("test/123"));
		Map<String, Object> vars = route.resolvePathVariables("test/123");
		Assert.assertTrue(vars.containsKey("id"));
		Assert.assertTrue(vars.get("id") instanceof Byte);
		Assert.assertEquals((byte) 123, vars.get("id"));
	}
	
	@Test
	public void resolvesStringParameter() {
		ZkRouterFactory routerFactory = new ZkRouterFactory();
		ZkRoute route = routerFactory.addRoute("test/{id:string}", "test");
		Assert.assertTrue(route.matches("test/123"));
		Map<String, Object> vars = route.resolvePathVariables("test/123");
		Assert.assertTrue(vars.containsKey("id"));
		Assert.assertTrue(vars.get("id") instanceof String);
		Assert.assertEquals("123", vars.get("id"));
	}
	
	@Test(expected = NumberFormatException.class)
	public void failsResolvingIntParameter() {
		ZkRouterFactory routerFactory = new ZkRouterFactory();
		ZkRoute route = routerFactory.addRoute("test/{id:int}", "test");
		Assert.assertTrue(route.matches("test/asd"));
		route.resolvePathVariables("test/asd");
	}
	
	@Test(expected = NumberFormatException.class)
	public void failsResolvingLongParameter() {
		ZkRouterFactory routerFactory = new ZkRouterFactory();
		ZkRoute route = routerFactory.addRoute("/test/{id:long}", "test");
		Assert.assertTrue(route.matches("test/asd"));
		route.resolvePathVariables("test/asd");
	}
	
	@Test(expected = NumberFormatException.class)
	public void failsResolvingShortParameter() {
		ZkRouterFactory routerFactory = new ZkRouterFactory();
		ZkRoute route = routerFactory.addRoute("/test/{id:short}", "test");
		Assert.assertTrue(route.matches("test/asd"));
		route.resolvePathVariables("test/asd");
	}
	
	@Test(expected = NumberFormatException.class)
	public void failsResolvingByteParameter() {
		ZkRouterFactory routerFactory = new ZkRouterFactory();
		ZkRoute route = routerFactory.addRoute("/test/{id:byte}", "test");
		Assert.assertTrue(route.matches("test/asd"));
		route.resolvePathVariables("test/asd");
	}
	
	@Test
	public void getEmpyParamsForPlainUrl() {
		ZkRouterFactory routerFactory = new ZkRouterFactory();
		ZkRoute route = routerFactory.addRoute("/test", "test");
		Assert.assertTrue(route.matches("test"));
		Assert.assertTrue(route.resolvePathVariables("test").isEmpty());
	}
	
	@Test
	public void resolveUnknownType() {
		ZkRouterFactory routerFactory = new ZkRouterFactory();
		ZkRoute route = routerFactory.addRoute("/test/{id:asd}", "test");
		Assert.assertTrue(route.matches("test/123"));
		Map<String, Object> vars = route.resolvePathVariables("test/123");
		Assert.assertTrue(vars.containsKey("id"));
		Assert.assertTrue(vars.get("id") instanceof String);
		Assert.assertEquals("123", vars.get("id"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void passInvalidParamDefinition() {
		ZkRouterFactory routerFactory = new ZkRouterFactory();
		routerFactory.addRoute("/test/{id:asd:asd}", "test");
	}
}
