package framework.utils;

import java.lang.reflect.Field;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import locators.PageLocatorsInitializer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	/**
	 * Extracts a {@link By} locator from a {@link WebElement} by parsing its string representation.
	 * <p>
	 * This method relies on the default {@code toString()} format of WebElement, which typically
	 * includes the locator strategy and value in the format:
	 * <pre>
	 * [[ChromeDriver: chrome on windows (session-id)] -> locatorType: locatorValue]
	 * </pre>
	 * Supported locator types include: id, name, css selector, class name, tag name, link text, 
	 * partial link text, and xpath.
	 * </p>
	 *
	 * @param webElement the {@link WebElement} instance from which to extract the locator
	 * @return a {@link By} object representing the locator used to find the given element
	 * @throws IllegalArgumentException if the WebElement string format is unrecognized or unsupported
	 * @throws UnsupportedOperationException if the locator type is not supported
	 */	
	private By getByFromWebElement(WebElement webElement) {
	    String elementString = webElement.toString();

	    // Example: [[ChromeDriver: chrome on windows (session-id)] -> name: password]
	    int arrowIndex = elementString.indexOf("->");
	    if (arrowIndex == -1) {
	        throw new IllegalArgumentException("Unexpected WebElement format: " + elementString);
	    }

	    String locatorPart = elementString.substring(arrowIndex + 2).trim();

	    // Remove the trailing bracket if it exists
	    if (locatorPart.endsWith("]")) {
	        locatorPart = locatorPart.substring(0, locatorPart.length() - 1).trim();
	    }

	    // Split into locator type and value
	    int splitIndex = locatorPart.indexOf(":");
	    if (splitIndex == -1) {
	        throw new IllegalArgumentException("Unable to parse locator: " + locatorPart);
	    }

	    String locatorType = locatorPart.substring(0, splitIndex).trim();
	    String locatorValue = locatorPart.substring(splitIndex + 1).trim();

	    return switch (locatorType.toLowerCase()) {
	        case "id" -> By.id(locatorValue);
	        case "name" -> By.name(locatorValue);
	        case "css selector" -> By.cssSelector(locatorValue);
	        case "class name" -> By.className(locatorValue);
	        case "tag name" -> By.tagName(locatorValue);
	        case "link text" -> By.linkText(locatorValue);
	        case "partial link text" -> By.partialLinkText(locatorValue);
	        case "xpath" -> By.xpath(locatorValue);
	        default -> throw new UnsupportedOperationException("Unsupported locator type: " + locatorType);
	    };
	}
	
//	private By getByFromWebElement(WebElement webElement) {
//	    String elementString = webElement.toString();
//
//	    // Patterns that support both ':' and '=' with multi-word locator types
//	    Pattern[] patterns = new Pattern[]{
//	        // Matches: -> name: password  or -> css selector: input[name='email']
//	        Pattern.compile("->\\s*([\\w\\s]+):\\s*(.+?)\\]?$"),
//
//	        // Matches: -> css selector = '#password']
//	        Pattern.compile("->\\s*([\\w\\s]+)=\\s*['\"]?(.+?)['\"]?\\]?$"),
//
//	        // Fallback for WebElement(id='foo')
//	        Pattern.compile(".*\\((\\w+)='(.+?)'\\).*")
//	    };
//
//	    for (Pattern pattern : patterns) {
//	        Matcher matcher = pattern.matcher(elementString);
//	        if (matcher.find()) {
//	            String locatorType = matcher.group(1).trim().toLowerCase();
//	            String locatorValue = matcher.group(2).trim();
//
//	            return switch (locatorType) {
//	                case "id" -> By.id(locatorValue);
//	                case "name" -> By.name(locatorValue);
//	                case "css selector", "cssselector", "css" -> By.cssSelector(locatorValue);
//	                case "class name", "classname" -> By.className(locatorValue);
//	                case "tag name", "tagname" -> By.tagName(locatorValue);
//	                case "link text", "linktext" -> By.linkText(locatorValue);
//	                case "partial link text", "partiallinktext" -> By.partialLinkText(locatorValue);
//	                case "xpath" -> By.xpath(locatorValue);
//	                default -> throw new UnsupportedOperationException("Unsupported locator type: " + locatorType);
//	            };
//	        }
//	    }
//
//	    throw new IllegalArgumentException("Failed to extract locator from WebElement.toString(): " + elementString);
//	}

}
