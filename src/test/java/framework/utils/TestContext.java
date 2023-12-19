package framework.utils;

import java.util.HashMap;
import java.util.Map;

public class TestContext {

	private Map<String, Object> testContext;

	public TestContext() {
		testContext = new HashMap<>();
	}

	public void setContext(String key, Object value) {
		testContext.put(key.toString(), value);
	}

	public Object getContext(String key) {
		return testContext.get(key.toString());
	}

	public Boolean isContains(String key) {
		return testContext.containsKey(key.toString());
	}
}