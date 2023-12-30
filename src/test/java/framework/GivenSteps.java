package framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import framework.utils.TestContext;
import framework.utils.WaitUtils;
import io.cucumber.java.en.Given;

public class GivenSteps extends SupportSteps{

	private Logger logger;
	private TestContext testContext;
	private WaitUtils waitUtils;

	public GivenSteps() {
		super(Hooks.getThreadSafeDriver());
//		driver = super.driver;
		logger = Hooks.getThreadSafeLogger();
		testContext = Hooks.getThreadSafeTestContext();
		waitUtils = new WaitUtils();
	}

	@Given("^I open the url \"(.*)\"$")

	public void openUrlOrSite(String url) {
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
	}

	@Given("^The element \"(.*)\" is( not)* displayed$")
	public void checkElementVisibility(String elementSelector, String notPresent) {
		logger.info("Checking visibility of element: {}", elementSelector);
		WebElement element = getElement(elementSelector);
		waitUtils.waitForElementToBeClickable(getLocator(elementSelector));
		boolean isDisplayed = element.isDisplayed();

		if ("not".equals(notPresent)) {
			assertFalse("Element is displayed but shouldn't be", isDisplayed);
		} else {
			assertTrue("Element is not displayed but should be", isDisplayed);
		}
	}

	@Given("^The element \"(.*)\" is( not)* enabled$")
	public void checkElementEnabled(String elementSelector, String notEnabled) {
		logger.info("Checking if element is enabled: {}", elementSelector);
		WebElement element = getElement(elementSelector);

		boolean isEnabled = element.isEnabled();

		if ("not".equals(notEnabled)) {
			assertFalse("Element is enabled but shouldn't be", isEnabled);
		} else {
			assertTrue("Element is not enabled but should be", isEnabled);
		}
	}

	@Given("^The element \"(.*)\" is( not)* selected$")
	public void checkElementSelected(String elementSelector, String notSelected) {
		logger.info("Checking if checkbox is checked: {}", elementSelector);
		WebElement element = getElement(elementSelector);

		boolean isSelected = element.isSelected();

		if ("not".equals(notSelected)) {
			assertFalse("Element is selected but shouldn't be", isSelected);
		} else {
			assertTrue("Element is not selected but should be", isSelected);
		}
	}

	@Given("^The checkbox \"(.*)\" is( not)* checked$")
	public void checkCheckboxChecked(String elementSelector, String notChecked) {
		logger.info("Checking if element exists on the page: {}", elementSelector);
		WebElement checkbox = getElement(elementSelector);

		boolean isChecked = checkbox.isSelected();

		if ("not".equals(notChecked)) {
			assertFalse("Checkbox is checked but shouldn't be", isChecked);
		} else {
			assertTrue("Checkbox is not checked but should be", isChecked);
		}
	}

	@Given("^There is (an|no) element \"(.*)\" on the page$")
	public void checkElementExists(String exists, String elementSelector) {
		logger.info("Checking if element exists on the page: {}", elementSelector);
		boolean elementExists = getElement(elementSelector) != null;

		if ("no".equals(exists)) {
			assertFalse("Element exists but shouldn't", elementExists);
		} else {
			assertTrue("Element does not exist but should", elementExists);
		}
	}

	@Given("^The title is( not)* \"(.*)\"$")
	public void checkTitle(String not, String expectedTitle) {
		logger.info("Checking if title is: {}", expectedTitle);
		String actualTitle = driver.getTitle();

		if ("not".equals(not)) {
			assertNotEquals("Title is not expected", expectedTitle, actualTitle);
		} else {
			assertEquals("Title is not as expected", expectedTitle, actualTitle);
		}
	}

	@Given("^The element \"(.*)\" contains( not)* the same text as element \"(.*)\"$")
	public void compareText(String elementSelector1, String not, String elementSelector2) {

		WebElement element1 = getElement(elementSelector1);
		WebElement element2 = getElement(elementSelector2);

		String text1 = element1.getText();
		String text2 = element2.getText();

		if ("not".equals(not)) {
			assertNotEquals("Elements have the same text but shouldn't", text1, text2);
		} else {
			assertEquals("Elements do not have the same text but should", text1, text2);
		}
	}

	@Given("^The (button|element) \"(.*)\"( not)* matches the text \"(.*)\"$")
	public void checkEqualsText(String elementSelector, String not, String expectedText) {
		logger.info("Checking if text matches for {}: {}", elementSelector, expectedText);
		WebElement element = getElement(elementSelector);
		String actualText = element.getText();

		if ("not".equals(not)) {
			assertNotEquals("Text is as expected but it shouldn't be", expectedText, actualText);
		} else {
			assertEquals("Text is not as expected", expectedText, actualText);
		}
	}

	@Given("^The (button|element|container) \"(.*)\"( not)* contains the text \"(.*)\"$")
	public void checkContainsText(String elementSelector, String not, String expectedText) {
		logger.info("Checking if text contains for {}: {}", elementSelector, expectedText);

		WebElement element = getElement(elementSelector);
		String actualText = element.getText();

		if ("not".equals(not)) {
			assertFalse("Text contains the expected value but it shouldn't", actualText.contains(expectedText));
		} else {
			assertTrue("Text does not contain the expected value", actualText.contains(expectedText));
		}
	}

	@Given("^The (button|element) \"(.*)\"( not)* contains any text$")
	public void checkContainsAnyText(String elementSelector, String not) {
		logger.info("Checking if element contains any text: {}", elementSelector);
		WebElement element = getElement(elementSelector);
		String text = element.getText();

		if ("not".equals(not)) {
			assertTrue("Text is present but it shouldn't be", text.isEmpty());
		} else {
			assertFalse("Text is not present but it should be", text.isEmpty());
		}
	}

	@Given("^The (button|element) \"(.*)\" is( not)* empty$")
	public void checkIsEmpty(String elementSelector, String notEmpty) {
		logger.info("Checking if element is empty: {}", elementSelector);
		WebElement element = getElement(elementSelector);
		String text = element.getText();

		if ("not".equals(notEmpty)) {
			assertFalse("Element is empty but it shouldn't be", text.isEmpty());
		} else {
			assertTrue("Element is not empty but it should be", text.isEmpty());
		}
	}

	@Given("^The page url is( not)* \"(.*)\"$")
	public void checkUrl(String not, String expectedUrl) {
		logger.info("Checking if URL is: {}", expectedUrl);
		String actualUrl = driver.getCurrentUrl();

		if ("not".equals(not)) {
			assertNotEquals("URL is not expected", expectedUrl, actualUrl);
		} else {
			assertEquals("URL is not as expected", expectedUrl, actualUrl);
		}
	}

	@Given("^The( css)* attribute \"(.*)\" from element \"(.*)\" is( not)* \"(.*)\"$")
	public void checkProperty(String css, String attributeName, String elementSelector, String not,
			String expectedValue) {
		String propertyType = (css != null && !css.isEmpty()) ? "CSS attribute" : "Attribute";
		logger.info("Checking if {} '{}' from element '{}' is: {}", propertyType, attributeName, elementSelector,
				expectedValue);
		WebElement element = getElement(elementSelector);
		String actualValue;

		if (css != null && !css.isEmpty()) {
			actualValue = element.getCssValue(attributeName);
		} else {
			actualValue = element.getAttribute(attributeName);
		}

		if ("not".equals(not)) {
			assertNotEquals("Attribute value is as expected but it shouldn't be", expectedValue, actualValue);
		} else {
			assertEquals("Attribute value is not as expected", expectedValue, actualValue);
		}
	}

	@Given("^The element \"(.*)\" is( not)* ([\\d]+)px (broad|tall)$")
	public void checkDimension(String elementSelector, String not, int expectedDimension, String type) {
		logger.info("Checking if dimension of element '{}' is: {} pixels {}", elementSelector, expectedDimension, type);
		WebElement element = getElement(elementSelector);
		Dimension actualDimension = element.getSize();
		int actualValue = type.equals("broad") ? actualDimension.getWidth() : actualDimension.getHeight();

		if ("not".equals(not)) {
			assertNotEquals("Element dimension is as expected but it shouldn't be", expectedDimension, actualValue);
		} else {
			assertEquals("Element dimension is not as expected", expectedDimension, actualValue);
		}
	}

	@Given("^the element \"(.*)\" is( not)* positioned at ([\\d]+)px on the (x|y) axis$")
	public void checkOffset(String elementSelector, String not, int expectedOffset, String axis) {
		logger.info("Checking if element '{}' is positioned at: {}px on the {} axis", elementSelector, expectedOffset,
				axis);
		WebElement element = getElement(elementSelector);
		Point actualOffset = element.getLocation();
		int actualValue = axis.equals("x") ? actualOffset.getX() : actualOffset.getY();

		if ("not".equals(not)) {
			assertNotEquals("Element offset is as expected but it shouldn't be", expectedOffset, actualValue);
		} else {
			assertEquals("Element offset is not as expected", expectedOffset, actualValue);
		}
	}

	@Given("^The cookie \"(.*)\" contains( not)* the value \"(.*)\"$")
	public void checkCookieContent(String cookieName, String not, String expectedValue) {
		logger.info("Checking if cookie '{}' contains{} the value: '{}'", cookieName, (not.isEmpty() ? "" : " not"),
				expectedValue);
		Cookie cookie = driver.manage().getCookieNamed(cookieName);

		if ("not".equals(not)) {
			assertNotEquals("Cookie value is as expected but it shouldn't be", expectedValue, cookie.getValue());
		} else {
			assertEquals("Cookie value is not as expected", expectedValue, cookie.getValue());
		}
	}

	@Given("^I have a screen that is ([\\d]+) by ([\\d]+) pixels$")
	public void setWindowSize(int width, int height) {
		logger.info("Setting window size to {} by {} pixels", width, height);
		driver.manage().window().setSize(new Dimension(width, height));

	}

	@Given("^The cookie \"(.*)\" does( not)* exist$")
	public void checkCookieExists(String cookieName, String notExists) {
		logger.info("Checking if cookie '{}' does{} exist", cookieName, (notExists.isEmpty() ? " not" : ""));
		Set<Cookie> cookies = driver.manage().getCookies();

		if ("not".equals(notExists)) {
			assertFalse("Cookie exists but it shouldn't",
					cookies.stream().anyMatch(cookie -> cookie.getName().equals(cookieName)));
		} else {
			assertTrue("Cookie does not exist but it should",
					cookies.stream().anyMatch(cookie -> cookie.getName().equals(cookieName)));
		}
	}

	@Given("^I have closed all but the first (window|tab)$")
	public void closeAllButFirstTab() {
		logger.info("Closing all but the first tab");
		String originalWindow = driver.getWindowHandle();
		Set<String> windowHandles = driver.getWindowHandles();

		for (String handle : windowHandles) {
			if (!handle.equals(originalWindow)) {
				driver.switchTo().window(handle);
				driver.close();
			}
		}
		driver.switchTo().window(originalWindow);
	}

	@Given("^a (alertbox|confirmbox|prompt) is( not)* opened$")
	public void checkModal(String modalType, String notOpened) {
		logger.info("Checking if {} is{} opened", modalType, (notOpened.isEmpty() ? "" : " not"));
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
			logger.error("Modal not opened : {}", e.getLocalizedMessage());
		}

		if (expectedOpen) {
			assertTrue("Expected " + modalType + " to be open, but it was not.", actualOpen);
		} else {
			assertFalse("Expected " + modalType + " to not be open, but it was.", actualOpen);
		}
	}
}
