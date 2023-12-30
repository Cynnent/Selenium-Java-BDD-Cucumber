package locators;

import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

public class PageLocatorsInitializer {

	public static Map<String, Object> initializePageLocators(WebDriver driver) {
		Map<String, Object> pageLocatorsMap = new HashMap<>();
		addPageLocators(pageLocatorsMap, "LoginPageLocators", new LoginPageLocators(driver));
		addPageLocators(pageLocatorsMap, "EntityPageLocators", new EntityPageLocators(driver));
		// Add more as needed

		return pageLocatorsMap;
	}

	private static void addPageLocators(Map<String, Object> pageLocatorsMap, String key, Object pageLocators) {
		pageLocatorsMap.put(key, pageLocators);
	}
}
