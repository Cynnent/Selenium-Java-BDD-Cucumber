package framework.utils;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import framework.Hooks;

public class WaitUtils {

	private final WebDriverWait wait;
	private final Logger logger;

	 public WaitUtils() {
	        this.wait = Hooks.getThreadSafeWait();
	        this.logger = Hooks.getThreadSafeLogger();
	    }

	public void waitForElementToBeClickable( By locator) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(locator));
		} catch (TimeoutException e) {
			logger.error("Element with locator '" + locator + "' not clickable within specified time.");
//            throw new RuntimeException("Element with locator '" + locator + "' not clickable within specified time.");
		}
	}

	public void waitForElementToBeVisible(Logger logger, By locator) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		} catch (TimeoutException e) {
			logger.error("Element with locator '" + locator + "' not visible within specified time.");
		}
	}

	public void waitForElementToBePresent(Logger logger, By locator) {
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (TimeoutException e) {
			logger.error("Element with locator '" + locator + "' not present within specified time.");
		}
	}

	public void waitForElementToBeInvisible(Logger logger, By locator) {
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
		} catch (TimeoutException e) {
			logger.error("Element with locator '" + locator + "' not invisible within specified time.");
		}
	}

	public void waitForTextToBePresentInElement(Logger logger, By locator, String text) {
		try {
			wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
		} catch (TimeoutException e) {
			logger.error(
					"Text '" + text + "' not present in element with locator '" + locator + "' within specified time.");
		}
	}
	// Add more custom wait methods based on needs

}
