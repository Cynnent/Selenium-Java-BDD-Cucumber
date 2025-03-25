package framework;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import framework.utils.LogUtils;
import framework.utils.PageLocatorsManager;

public abstract class SupportSteps {

	protected WebDriver driver;

	protected SupportSteps(WebDriver driver) {
		this.driver = driver;
	}

	protected By getLocator(String elementSelector) {
		PageLocatorsManager pageLocatorsManager = new PageLocatorsManager(driver);
		return pageLocatorsManager.getLocator(elementSelector);
	}

	protected WebElement getElement(String elementSelector) {
		By locatorvalue = getLocator(elementSelector);
		return driver.findElement(locatorvalue);
	}

	protected List<WebElement> getElements(String elementSelector) {
		By locatorvalue = getLocator(elementSelector);
		return driver.findElements(locatorvalue);
	}

	protected void pressKeys(String keyCombination) {
		System.out.println("Pressing key(s): '" + keyCombination + "'");

		WebElement targetElement = driver.switchTo().activeElement();
		Actions actions = new Actions(driver);
		String[] keys = keyCombination.trim().split("\\s*\\+\\s*");

		if (keys.length == 1) {
			CharSequence key = getSeleniumKey(keys[0]);
			actions.sendKeys(targetElement, key).perform();
		} else {
			for (int i = 0; i < keys.length - 1; i++) {
				CharSequence modifier = getSeleniumKey(keys[i]);
				actions.keyDown(targetElement, modifier);
			}
			CharSequence finalKey = getSeleniumKey(keys[keys.length - 1]);
			actions.sendKeys(targetElement, finalKey);
			for (int i = keys.length - 2; i >= 0; i--) {
				CharSequence modifier = getSeleniumKey(keys[i]);
				actions.keyUp(targetElement, modifier);
			}
			actions.perform();
		}

		System.out.println("Successfully pressed key(s): '" + keyCombination + "'");
	}

	private CharSequence getSeleniumKey(String key) {
		String trimmedKey = key.toUpperCase().trim();
		if (trimmedKey.length() == 1 && Character.isLetterOrDigit(trimmedKey.charAt(0))) {
			return trimmedKey; // Single character (e.g., "a", "1")
		}
		try {
			return Keys.valueOf(trimmedKey);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Unsupported key: " + key);
		}
	}

}
