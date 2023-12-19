package framework.utils;

import java.lang.reflect.Field;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import locators.PageLocatorsInitializer;

public class PageLocatorsManager {

	private final WebDriver driver;
	private final Map<String, Object> pageLocatorsMap;

	public PageLocatorsManager(WebDriver driver) {
		this.driver = driver;
		this.pageLocatorsMap = PageLocatorsInitializer.initializePageLocators(driver);
	}

	public void addPageLocators(String key, Object pageLocators) {
		pageLocatorsMap.put(key, pageLocators);
	}

	public By getLocator(String pageName, String fieldName) {
		Object pageLocators = pageLocatorsMap.get(pageName);
		if (pageLocators == null) {
			throw new IllegalArgumentException("PageLocators not found for page: " + pageName);
		}

		Field field;
		try {
			field = pageLocators.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new IllegalArgumentException("Field not found: " + fieldName + " on page: " + pageName, e);
		}

		if (field.isAnnotationPresent(FindBy.class)) {
			WebElement webElement;
			try {
				webElement = (WebElement) field.get(pageLocators);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Failed to access field: " + fieldName, e);
			}
			return getByFromWebElement(webElement);
		}

		throw new IllegalArgumentException(
				"Field: " + fieldName + " on page: " + pageName + " does not have @FindBy annotation");
	}

	public By getLocator(String fieldName) {
		for (Object pageLocators : pageLocatorsMap.values()) {
			Field field;
			try {
				field = pageLocators.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);
			} catch (NoSuchFieldException | SecurityException e) {
				continue; // Continue to the next pageLocators if field not found
			}

			if (field.isAnnotationPresent(FindBy.class)) {
				WebElement webElement;
				try {
					webElement = (WebElement) field.get(pageLocators);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Failed to access field: " + fieldName, e);
				}
				return getByFromWebElement(webElement);
			}
		}

		throw new IllegalArgumentException(
				"Field: " + fieldName + " not found with @FindBy annotation in any pageLocators");
	}

	private By getByFromWebElement(WebElement webElement) {
		String elementString = webElement.toString();

		// Find the index of " -> " and extract the substring after it
		String substringAfterArrow = elementString.substring(elementString.indexOf(" -> ") + 4);

		// Replace consecutive "]" with a single "]" and remove the last "]" after "}"
		String cleanedString = substringAfterArrow.replaceAll("\\]+", "]").replaceFirst("\\]$", "");

		// Split the remaining string
		String[] locatorParts = cleanedString.split(": ");

		String locatorType = locatorParts[0].trim();
		String locatorValue = locatorParts[1].trim();

		// Remove the "]" after "}"
		locatorValue = locatorValue.replaceAll("\\]$", "");

		return switch (locatorType) {
		case "css selector" -> By.cssSelector(locatorValue);
		case "xpath" -> By.xpath(locatorValue);
		case "id" -> By.id(locatorValue);
		case "name" -> By.className(locatorValue);
		// Add cases for other locator types (id, xpath, etc.) if needed
		default -> throw new UnsupportedOperationException("Locator type not supported: " + locatorType);
		};
	}

//    private By getByFromWebElement(WebElement webElement) {
////        String[] locatorParts = webElement.toString().split(" -> ")[1].replace("]", "").split(": ");-- working but if we have multiple "]" it fails 
//    	String locatorParts[] = webElement.toString().split(" -> ")[1].replaceFirst("]", "").split(": ");
////    	String locatorParts[] = webElement.toString().split(" -> ")[1].split(": ");
//    	
//        
//        // Find the last occurrence of "]"
////        int lastBracketIndex = elementToString.lastIndexOf("]");
////        
////        // Remove the last "]" and split the remaining string
////        String[] locatorParts = elementToString.substring(0, lastBracketIndex).split(": ");
//
//        String locatorType = locatorParts[0];
//        String locatorValue = locatorParts[1];
//
//        return switch (locatorType.toLowerCase()) {
//            case "css selector" -> By.cssSelector(locatorValue);
//            case "xpath" -> By.xpath(locatorValue);
//            case "id" -> By.id(locatorValue);
//            // Add cases for other locator types if needed
//            default -> throw new UnsupportedOperationException("Locator type not supported: " + locatorType);
//        };
//    }
}

//import java.lang.reflect.Field;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.FindBy;
//
//
//
//public class PageLocatorsManager {
//
//    private final WebDriver driver;
//    private final Map<String, Object> pageLocatorsMap;
//
//    public PageLocatorsManager(WebDriver driver) {
//        this.driver = driver;
//        this.pageLocatorsMap = new HashMap<>();
//        initializePageLocators();
//    }
//
//    private void initializePageLocators() {
//    	addPageLocators("LoginPageLocators", new LoginPageLocators(driver));
//        addPageLocators("EntityPageLocators", new EntityPageLocators(driver));
////        PageLocatorsInitializer initializer = new PageLocatorsInitializer(driver);
////        initializer.initializePageLocators(this);
//    }
//
//    public void addPageLocators(String key, Object pageLocators) {
//        pageLocatorsMap.put(key, pageLocators);
//    }
//
//    public By getLocator(String pageName, String fieldName) throws NoSuchFieldException, IllegalAccessException {
//        Object pageLocators = pageLocatorsMap.get(pageName);
//        if (pageLocators == null) {
//            throw new IllegalArgumentException("PageLocators not found for page: " + pageName);
//        }
//
//        Field[] fields = pageLocators.getClass().getDeclaredFields();
//        for (Field field : fields) {
//            if (field.isAnnotationPresent(FindBy.class) && field.getName().equals(fieldName)) {
//                field.setAccessible(true);
//                WebElement webElement = (WebElement) field.get(pageLocators);
//                return getByFromWebElement(webElement);
//            }
//        }
//
//        throw new IllegalArgumentException("Locator not found for field: " + fieldName + " on page: " + pageName);
//    }
//    
//    
// 
//
//    private By getByFromWebElement(WebElement webElement) {
//        String[] locatorParts = webElement.toString().split(" -> ")[1].replace("]", "").split(": ");
//        String locatorType = locatorParts[0];
//        String locatorValue = locatorParts[1];
//
//        return switch (locatorType) {
//            case "css selector" -> By.cssSelector(locatorValue);
//            // Add cases for other locator types (id, xpath, etc.) if needed
//            default -> throw new UnsupportedOperationException("Locator type not supported: " + locatorType);
//        };
//    }
//}
