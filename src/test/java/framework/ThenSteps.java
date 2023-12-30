package framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import framework.utils.TestContext;
import framework.utils.WaitUtils;
import io.cucumber.java.en.Then;

public class ThenSteps extends SupportSteps{

	private Logger logger;
	private TestContext testContext;
	private WaitUtils waitUtils;

	public ThenSteps() {
		super(Hooks.getThreadSafeDriver());
//		driver = super.driver;
		logger = Hooks.getThreadSafeLogger();
		testContext = Hooks.getThreadSafeTestContext();
		waitUtils = new WaitUtils();
	}

	@Then("^I expect that the title is( not)* \"(.*)\"$")
	public void checkTitle(String not, String expectedTitle) {
		String actualTitle = driver.getTitle();

		if ("not".equals(not)) {
			logger.info("Verifying that the title is not '{}'. Actual title: '{}'", expectedTitle, actualTitle);
			assertNotEquals("Title is not expected", expectedTitle, actualTitle);
		} else {
			logger.info("Verifying that the title is '{}'. Actual title: '{}'", expectedTitle, actualTitle);
			assertEquals("Title is not as expected", expectedTitle, actualTitle);
		}
	}

	@Then("^I expect that the title( not)* contains \"(.*)\"$")
	public void checkTitleContains(String not, String expectedTitle) {
		String actualTitle = driver.getTitle();

		if ("not".equals(not)) {
			logger.info("Verifying that the title does not contain '{}'. Actual title: '{}'", expectedTitle,
					actualTitle);
			assertFalse("Title contains the expected text but it shouldn't", actualTitle.contains(expectedTitle));
		} else {
			logger.info("Verifying that the title contains '{}'. Actual title: '{}'", expectedTitle, actualTitle);
			assertTrue("Title does not contain the expected text", actualTitle.contains(expectedTitle));
		}
	}

	@Then("^I expect that element \"(.*)\" does( not)* appear exactly \"(.*)\" times$")
	public void checkIfElementExists(String elementSelector, String not, int expectedCount) {
		List<WebElement> elements = getElements(elementSelector);
		int actualCount = elements.size();

		if ("not".equals(not)) {
			logger.info("Verifying that element '{}' does not appear exactly '{}' times. Actual count: '{}'",
					elementSelector, expectedCount, actualCount);
			assertNotEquals("Element appears exactly " + expectedCount + " times but it shouldn't", expectedCount,
					actualCount);
		} else {
			logger.info("Verifying that element '{}' appears exactly '{}' times. Actual count: '{}'", elementSelector,
					expectedCount, actualCount);
			assertEquals("Element does not appear exactly " + expectedCount + " times", expectedCount, actualCount);
		}
	}

	@Then("^I expect that element \"(.*)\" is( not)* displayed$")
	public void checkIfElementDisplayed(String elementSelector, String not) {
		WebElement element = getElement(elementSelector);

		if ("not".equals(not)) {
			logger.info("Verifying that element '{}' is not displayed", elementSelector);
			assertFalse("Element is displayed but it shouldn't be", element.isDisplayed());
		} else {
			logger.info("Verifying that element '{}' is displayed", elementSelector);
			assertTrue("Element is not displayed but it should be", element.isDisplayed());
		}
	}

	@Then("^I expect that element \"(.*)\" becomes( not)* displayed$")
	public void waitForElementToBeDisplayed(String elementSelector, String not) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement element = getElement(elementSelector);

		if ("not".equals(not)) {
			logger.info("Waiting for element '{}' to become not displayed", elementSelector);
			wait.until(ExpectedConditions.invisibilityOf(element));
		} else {
			logger.info("Waiting for element '{}' to become displayed", elementSelector);
			wait.until(ExpectedConditions.visibilityOf(element));
		}
	}

//	@Then("^I expect that element \"(.*)\" is( not)* within the viewport$")
	public void checkWithinViewport(String elementSelector, String not) {
//		WebElement element = getElement(elementSelector);
//	        boolean isWithinViewport = ((JavascriptExecutor) driver).executeScript("return arguments[0].getBoundingClientRect().top >= 0", element);
//
//	        if ("not".equals(not)) {
//	            assertFalse("Element is within the viewport but it shouldn't be", isWithinViewport);
//	        } else {
//	            assertTrue("Element is not within the viewport but it should be", isWithinViewport);
//	        }
	}

	@Then("^I expect that element \"(.*)\" does( not)* exist$")
	public void checkIsExisting(String elementSelector, String not) {
		WebElement element = getElement(elementSelector);

		if ("not".equals(not)) {
			logger.info("Verifying that element '{}' does not exist", elementSelector);
			assertNull("Element exists but it shouldn't", element);
		} else {
			logger.info("Verifying that element '{}' exists", elementSelector);
			assertNotNull("Element does not exist but it should", element);
		}
	}

	@Then("^I expect that element \"(.*)\"( not)* contains the same text as element \"(.*)\"$")
	public void compareText(String elementSelector1, String not, String elementSelector2) {
		WebElement element1 = getElement(elementSelector1);
		WebElement element2 = getElement(elementSelector2);

		String text1 = element1.getText();
		String text2 = element2.getText();

		if ("not".equals(not)) {
			assertNotEquals("Elements have the same text but they shouldn't", text1, text2);
		} else {
			assertEquals("Elements do not have the same text but they should", text1, text2);
		}
	}

	@Then("^I expect that (button|element) \"(.*)\"( not)* matches the text \"(.*)\"$")
	public void checkEqualsText(String elementType, String elementSelector, String not, String expected_text) {
		expected_text = expected_text.replace("\\n", "\n");
		String actual_text = getElement(elementSelector).getText().trim();
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if ("not".equals(not)) {
			logger.info("Verifying that {} '{}' does not match the text '{}'. Actual text: '{}'", elementType,
					elementSelector, expected_text, actual_text);
			assertNotEquals(expected_text, actual_text);
		} else {
			logger.info("Verifying that {} '{}' matches the text '{}'. Actual text: '{}'", elementType, elementSelector,
					expected_text, actual_text);
			assertEquals(expected_text, actual_text);
		}
	}

	@Then("^I expect that (button|element|container) \"(.*)\"( not)* contains the text \"(.*)\"$")
	public void checkContainsText(String elementType, String elementSelector, String not, String expectedText) {
		WebElement element = getElement(elementSelector);
		String actualText = element.getText();

		if ("not".equals(not)) {
			logger.info("Verifying that {} '{}' does not contain the text '{}'. Actual text: '{}'", elementType,
					elementSelector, expectedText, actualText);
			assertFalse(actualText.contains(expectedText));
		} else {
			logger.info("Verifying that {} '{}' contains the text '{}'. Actual text: '{}'", elementType,
					elementSelector, expectedText, actualText);
			assertTrue(actualText.contains(expectedText));
		}
	}

	@Then("^I expect that (button|element) \"(.*)\"( not)* contains any text$")
	public void checkContainsAnyText(String elementType, String elementSelector, String not) {
		WebElement element = getElement(elementSelector);
		String text = element.getText();

		if ("not".equals(not)) {
			logger.info("Verifying that {} '{}' does not contain any text", elementType, elementSelector);
			assertTrue(text.isEmpty());
		} else {
			logger.info("Verifying that {} '{}' contains any text", elementType, elementSelector);
			assertFalse(text.isEmpty());
		}
	}

	@Then("^I expect that (button|element) \"(.*)\" is( not)* empty$")
	public void checkIsEmpty(String elementType, String elementSelector, String not) {
		WebElement element = getElement(elementSelector);
		String text = element.getText();

		if ("not".equals(not)) {
			logger.info("Verifying that {} '{}' is not empty", elementType, elementSelector);
			assertFalse(text.isEmpty());
		} else {
			logger.info("Verifying that {} '{}' is empty", elementType, elementSelector);
			assertTrue(text.isEmpty());
		}
	}

	@Then("^I expect that the url is( not)* \"(.*)\"$")
	public void checkURL(String not, String expectedUrl) {
		String actualUrl = driver.getCurrentUrl();

		if ("not".equals(not)) {
			logger.info("Verifying that the URL is not '{}'. Actual URL: '{}'", expectedUrl, actualUrl);
			assertNotEquals(expectedUrl, actualUrl);
		} else {
			logger.info("Verifying that the URL is '{}'. Actual URL: '{}'", expectedUrl, actualUrl);
			assertEquals(expectedUrl, actualUrl);
		}
	}

//	@Then("^I expect that the path is( not)* \"(.*)\"$") -- need to work
//	public void checkURLPath(String not, String expectedPath) {
//		 String actualPath = new Url(driver.getCurrentUrl()).getPath();
//
//	        if ("not".equals(not)) {
//	            assertNotEquals("Path is not expected", expectedPath, actualPath);
//	        } else {
//	            assertEquals("Path is not as expected", expectedPath, actualPath);
//	        }
//	}

	@Then("^I expect the url to( not)* contain \"(.*)\"$")
	public void checkInURLPath(String not, String expectedText) {
		String actualUrl = driver.getCurrentUrl();

		if ("not".equals(not)) {
			logger.info("Verifying that the URL does not contain '{}'. Actual URL: '{}'", expectedText, actualUrl);
			assertFalse(actualUrl.contains(expectedText));
		} else {
			logger.info("Verifying that the URL contains '{}'. Actual URL: '{}'", expectedText, actualUrl);
			assertTrue(actualUrl.contains(expectedText));
		}
	}

	@Then("^I expect that the( css)* attribute \"(.*)\" from element \"(.*)\" is( not)* \"(.*)\"$")
	public void checkProperty(String css, String attributeName, String elementSelector, String not,
			String expectedValue) {
		WebElement element = getElement(elementSelector);
		String actualValue;

		if (css != null && !css.isEmpty()) {
			actualValue = element.getCssValue(attributeName);
		} else {
			actualValue = element.getAttribute(attributeName);
		}

		if ("not".equals(not)) {
			logger.info("Verifying that the attribute '{}' from {} '{}' is not '{}'. Actual value: '{}'", attributeName,
					elementSelector, css, expectedValue, actualValue);
			assertNotEquals(expectedValue, actualValue);
		} else {
			logger.info("Verifying that the attribute '{}' from {} '{}' is '{}'. Actual value: '{}'", attributeName,
					elementSelector, css, expectedValue, actualValue);
			assertEquals(expectedValue, actualValue);
		}
	}

	@Then("^I expect that the font( css)* attribute \"(.*)\" from element \"(.*)\" is( not)* \"(.*)\"$")
	public void checkFontProperty(String css, String attributeName, String elementSelector, String not,
			String expectedValue) {
		WebElement element = getElement(elementSelector);
		String actualValue;

		if (css != null && !css.isEmpty()) {
			actualValue = element.getCssValue(attributeName);
		} else {
			actualValue = element.getAttribute(attributeName);
		}

		if ("not".equals(not)) {
			logger.info("Verifying that the font attribute '{}' from {} '{}' is not '{}'. Actual value: '{}'",
					attributeName, elementSelector, css, expectedValue, actualValue);
			assertNotEquals(expectedValue, actualValue);
		} else {
			logger.info("Verifying that the font attribute '{}' from {} '{}' is '{}'. Actual value: '{}'",
					attributeName, elementSelector, css, expectedValue, actualValue);
			assertEquals(expectedValue, actualValue);
		}
	}

	@Then("^I expect that checkbox \"(.*)\" is( not)* checked$")
	public void checkSelected(String elementSelector, String not) {
		WebElement checkbox = getElement(elementSelector);

		if ("not".equals(not)) {
			logger.info("Verifying that checkbox '{}' is not checked", elementSelector);
			assertFalse(checkbox.isSelected());
		} else {
			logger.info("Verifying that checkbox '{}' is checked", elementSelector);
			assertTrue(checkbox.isSelected());
		}
	}

	@Then("^I expect that element \"(.*)\" is( not)* selected$")
	public void radioSelected(String elementSelector, String not) {
		WebElement radio = getElement(elementSelector);

		if ("not".equals(not)) {
			logger.info("Verifying that radio button '{}' is not selected", elementSelector);
			assertFalse(radio.isSelected());
		} else {
			logger.info("Verifying that radio button '{}' is selected", elementSelector);
			assertTrue(radio.isSelected());
		}

	}

	@Then("^I expect that element \"(.*)\" is( not)* enabled$")
	public void isEnabled(String elementSelector, String not) {
		WebElement element = getElement(elementSelector);

		if ("not".equals(not)) {
			logger.info("Verifying that element '{}' is not enabled", elementSelector);
			assertFalse(element.isEnabled());
		} else {
			logger.info("Verifying that element '{}' is enabled", elementSelector);
			assertTrue(element.isEnabled());
		}
	}

	@Then("^I expect that cookie \"(.*)\"( not)* contains \"(.*)\"$")
	public void checkCookieContent(String cookieName, String not, String expectedCookieValue) {
		Cookie cookie = driver.manage().getCookieNamed(cookieName);

		if ("not".equals(not)) {
			logger.info("Verifying that cookie '{}' does not contain '{}'. Actual value: '{}'", cookieName,
					expectedCookieValue, cookie.getValue());
			assertNotEquals(expectedCookieValue, cookie.getValue());
		} else {
			logger.info("Verifying that cookie '{}' contains '{}'. Actual value: '{}'", cookieName, expectedCookieValue,
					cookie.getValue());
			assertEquals(expectedCookieValue, cookie.getValue());
		}
	}

	@Then("^I expect that cookie \"(.*)\"( not)* exists$")
	public void checkCookieExists(String cookieName, String not) {
		Set<Cookie> cookies = driver.manage().getCookies();

		if ("not".equals(not)) {
			logger.info("Verifying that cookie '{}' does not exist", cookieName);
			assertFalse(cookies.stream().anyMatch(cookie -> cookie.getName().equals(cookieName)));
		} else {
			logger.info("Verifying that cookie '{}' exists", cookieName);
			assertTrue(cookies.stream().anyMatch(cookie -> cookie.getName().equals(cookieName)));
		}
	}

	@Then("^I expect that element \"(.*)\" is( not)* ([\\d]+)px (broad|tall)$")
	public void verifyElementDimension(String elementSelector, String not, int expectedDimension, String type) {
		WebElement element = getElement(elementSelector);
		Dimension actualDimension = element.getSize();
		int actualValue = type.equals("broad") ? actualDimension.getWidth() : actualDimension.getHeight();

		if ("not".equals(not)) {
			logger.info("Verifying that element '{}' is not {}px {} (Actual: {}px)", elementSelector, expectedDimension,
					type, actualValue);
			assertNotEquals(expectedDimension, actualValue);
		} else {
			logger.info("Verifying that element '{}' is {}px {} (Actual: {}px)", elementSelector, expectedDimension,
					type, actualValue);
			assertEquals(expectedDimension, actualValue);
		}
	}

	@Then("^I expect that element \"(.*)\" is( not)* positioned at ([\\d+.?\\d*]+)px on the (x|y) axis$")
	public void verifyElementPosition(String elementSelector, String not, double expectedPosition, String axis) {
		WebElement element = getElement(elementSelector);
		Point actualPosition = element.getLocation();
		double actualValue = axis.equals("x") ? actualPosition.getX() : actualPosition.getY();

		if ("not".equals(not)) {
			logger.info("Verifying that element '{}' is not positioned at {}px on the {} axis (Actual: {}px)",
					elementSelector, expectedPosition, axis, actualValue);
			assertNotEquals("Element position is as expected but it shouldn't be", expectedPosition, actualValue);
		} else {
			logger.info("Verifying that element '{}' is positioned at {}px on the {} axis (Actual: {}px)",
					elementSelector, expectedPosition, axis, actualValue);
			assertEquals("Element position is not as expected", expectedPosition, actualValue, 0.01); // Adjust the
																										// delta value
																										// based on
																										// precision
																										// needs
		}
	}

//	@Then("^I expect that element \"(.*)\" (has|does not have) the class \"(.*)\"$")
	public void verifyElementHasClass(String elementSelector, String hasOrNot, String className) {
//		WebElement element = getElement(elementSelector);
//		String[] classes = element.getAttribute("class").split(" ");
//
//		if ("has".equals(hasOrNot)) {
//			logger.info("Verifying that element '{}' has the class '{}'", elementSelector, className);
//			assertTrue(Arrays.asList(classes).contains(className));
//		} else {
//			logger.info("Verifying that element '{}' does not have the class '{}'", elementSelector, className);
//			assertFalse(Arrays.asList(classes).contains(className));
//		}
	}

	@Then("^I expect a new (window|tab) has( not)* been opened$")
	public void verifyNewWindowOrTabOpened(String type, String not) {
		Set<String> windowHandles = driver.getWindowHandles();

		if ("not".equals(not)) {
			logger.info("Verifying that a new {} has not been opened", type);
			assertEquals("A new window or tab has been opened but it shouldn't", 1, windowHandles.size());
		} else {
			logger.info("Verifying that a new {} has been opened", type);
			assertNotEquals("A new window or tab has not been opened but it should", 1, windowHandles.size());
		}
	}

	@Then("^I expect the url \"(.*)\" is opened in a new (tab|window)$")
	public void verifyURLOpenedInNewWindowOrTab(String expectedUrl, String type) {
		Set<String> windowHandles = driver.getWindowHandles();
		boolean urlOpenedInNewWindowOrTab = false;

		for (String handle : windowHandles) {
			driver.switchTo().window(handle);
			if (driver.getCurrentUrl().equals(expectedUrl)) {
				urlOpenedInNewWindowOrTab = true;
				break;
			}
		}

		if (urlOpenedInNewWindowOrTab) {
			logger.info("Verifying that the URL '{}' is opened in a new {}", expectedUrl, type);
			assertTrue("URL is not opened in a new window or tab", true);
		} else {
			logger.info("Verifying that the URL '{}' is not opened in a new {}", expectedUrl, type);
			assertFalse("URL is not opened in a new window or tab", false);
		}
	}

	@Then("^I expect that element \"(.*)\" is( not)* focused$")
	public void verifyElementIsFocused(String elementSelector, String not) {
		WebElement element = getElement(elementSelector);
		boolean isFocused = element.equals(driver.switchTo().activeElement());

		if ("not".equals(not)) {
			logger.info("Verifying that element '{}' is not focused", elementSelector);
			assertFalse("Element is focused but it shouldn't be", isFocused);
		} else {
			logger.info("Verifying that element '{}' is focused", elementSelector);
			assertTrue("Element is not focused but it should be", isFocused);
		}
	}

	@Then("^I wait on element \"(.*)\"(?: for (\\d+)ms)*(?: to( not)* (be checked|be enabled|be selected|be displayed|contain a text|contain a value|exist))*$")
	public void waitForElement(String elementSelector, Integer timeout, String not, String state) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		if (timeout != null) {
			wait = new WebDriverWait(driver, Duration.ofMillis(timeout));
		}
		WebElement element = getElement(elementSelector);

		switch (state) {
		case "be checked":
			if ("not".equals(not)) {
				logger.info("Waiting for element '{}' to not be checked", elementSelector);
				wait.until(ExpectedConditions.not(ExpectedConditions.elementToBeSelected(element)));
			} else {
				logger.info("Waiting for element '{}' to be checked", elementSelector);
				wait.until(ExpectedConditions.elementToBeSelected(element));
			}
			break;
		case "be enabled":
			if ("not".equals(not)) {
				logger.info("Waiting for element '{}' to not be enabled", elementSelector);
				wait.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(element)));
			} else {
				logger.info("Waiting for element '{}' to be enabled", elementSelector);
				wait.until(ExpectedConditions.elementToBeClickable(element));
			}
			break;
		case "be selected":
			if ("not".equals(not)) {
				logger.info("Waiting for element '{}' to not be selected", elementSelector);
				wait.until(ExpectedConditions.not(ExpectedConditions.elementToBeSelected(element)));
			} else {
				logger.info("Waiting for element '{}' to be selected", elementSelector);
				wait.until(ExpectedConditions.elementToBeSelected(element));
			}
			break;
		case "be displayed":
			if ("not".equals(not)) {
				logger.info("Waiting for element '{}' to not be displayed", elementSelector);
				wait.until(ExpectedConditions.invisibilityOf(element));
			} else {
				logger.info("Waiting for element '{}' to be displayed", elementSelector);
				wait.until(ExpectedConditions.visibilityOf(element));
			}
			break;
		case "contain a text":
			if ("not".equals(not)) {
				logger.info("Waiting for element '{}' to not contain any text", elementSelector);
				wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(element, "")));
			} else {
				logger.info("Waiting for element '{}' to contain any text", elementSelector);
				wait.until(ExpectedConditions.textToBePresentInElement(element, ""));
			}
			break;
		case "contain a value":
			if ("not".equals(not)) {
				logger.info("Waiting for element '{}' to not contain any value", elementSelector);
				wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementValue(element, "")));
			} else {
				logger.info("Waiting for element '{}' to contain any value", elementSelector);
				wait.until(ExpectedConditions.textToBePresentInElementValue(element, ""));
			}
			break;
		case "exist":
			if ("not".equals(not)) {
				logger.info("Waiting for element '{}' to not exist", elementSelector);
				wait.until(ExpectedConditions.stalenessOf(element));
			} else {
				logger.info("Waiting for element '{}' to exist", elementSelector);
				wait.until(ExpectedConditions.presenceOfElementLocated(getLocator(elementSelector)));
			}
			break;
		default:
			throw new IllegalArgumentException("Unsupported state: " + state);
		}
	}

	@Then("^I expect that a (alertbox|confirmbox|prompt) is( not)* opened$")
	public void verifyModalOpened(String modalType, String notOpened) {
		boolean expectedOpen = !notOpened.contains("not");
		boolean actualOpen = false;
		try {
			switch (modalType) {
			case "alertbox":
				actualOpen = driver.switchTo().alert() != null;
				break;
			case "confirmbox":
				actualOpen = driver.switchTo().alert() != null;
				break;
			case "prompt":
				actualOpen = driver.switchTo().alert() != null;
				break;
			default:
				throw new IllegalArgumentException("Unsupported modal type: " + modalType);
			}
		} catch (NoAlertPresentException e) {
			// No modal present, considered not open
		}

		if (expectedOpen) {
			logger.info("Verifying that a {} is open", modalType);
			assertTrue("Expected " + modalType + " to be open, but it was not.", actualOpen);
		} else {
			logger.info("Verifying that a {} is not open", modalType);
			assertFalse("Expected " + modalType + " to not be open, but it was.", actualOpen);
		}
	}

	@Then("^I expect that a (alertbox|confirmbox|prompt)( not)* contains the text \"(.*)\"$")
	public void verifyModalText(String type, String not, String expectedText) {
		boolean unexpectedPresent = not.contains("not");
		String actualText = "";
		try {
			// Switch to the appropriate modal based on type
			switch (type) {
			case "alertbox":
				actualText = driver.switchTo().alert().getText();
				break;
			case "confirmbox":
				actualText = driver.switchTo().alert().getText();
				break;
			case "prompt":
				actualText = driver.switchTo().alert().getText();
				break;
			default:
				throw new IllegalArgumentException("Unsupported modal type: " + type);
			}
		} catch (NoAlertPresentException e) {
			// Modal not present, considered not unexpected if we expected it
			unexpectedPresent = false;
		}
		if (unexpectedPresent) {
			logger.info("Verifying that {} contains the text '{}'", type, expectedText);
			assertFalse("Alertbox contains the expected text but it shouldn't", actualText.contains(expectedText));
		} else {
			logger.info("Verifying that {} does not contain the text '{}'", type, expectedText);
			assertTrue("Alertbox does not contain the expected text", actualText.contains(expectedText));
		}
	}

}
