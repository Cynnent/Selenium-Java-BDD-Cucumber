package framework;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import framework.utils.LogUtils;
import framework.utils.TestContext;
import framework.utils.WaitUtils;
import io.cucumber.java.en.When;

public class WhenSteps extends SupportSteps {

	private Logger logger;
	private TestContext testContext;
	private WaitUtils waitUtils;

	public WhenSteps() {
		super(Hooks.getThreadSafeDriver());
//		driver = super.driver;
		logger = Hooks.getThreadSafeLogger();
		testContext = Hooks.getThreadSafeTestContext();
		waitUtils = new WaitUtils();
	}

	@When("I (click|doubleclick) on the (link|button|element|inputfield) \"(.*)\"$")
	public void clickElement(String action, String elementType, String elementSelector) {
		WebElement element = super.getElement(elementSelector);
		LogUtils.logInfo(logger, "Performing {} on {} with selector '{}'", action, elementType, elementSelector);
		if ("click".equals(action)) {
			waitUtils.waitForElementToBeClickable(getLocator(elementSelector));
			element.click();
		} else if ("doubleclick".equals(action)) {
			new Actions(driver).doubleClick(element).build().perform();
		} else {
			throw new IllegalArgumentException("Unsupported action: " + action);
		}
	}

	@When("^I (add|set) \"(.*)\" to the inputfield \"(.*)\"$")
	public void setInputField(String action, String value, String elementSelector) {
		WebElement inputField = super.getElement(elementSelector);
		LogUtils.logInfo(logger, "{} value '{}' to the input field with selector '{}'", action, value, elementSelector);

		if ("add".equalsIgnoreCase(action)) {
			String currentValue = inputField.getAttribute("value");
			inputField.sendKeys(currentValue + value);
		} else {
			inputField.sendKeys(value);
		}
	}

	@When("I clear the inputfield \"(.*)\"$")
	public void clearInputField(String elementSelector) {
		WebElement inputField = getElement(elementSelector);
		LogUtils.logInfo(logger, "Clearing the input field with selector '{}'", elementSelector);
		inputField.clear();
	}

	@When("I drag element \"(.*)\" to element \"(.*)\"$")
	public void dragElement(String sourceElementSelector, String targetElementSelector) {
		WebElement sourceElement = getElement(sourceElementSelector);
		WebElement targetElement = getElement(targetElementSelector);
		LogUtils.logInfo(logger, "Dragging element from '{}' to '{}'", sourceElementSelector, targetElementSelector);
		Actions actions = new Actions(driver);
		actions.dragAndDrop(sourceElement, targetElement).build().perform();
	}

	@When("I pause for (\\d+)ms$")
	public void pause(long timeInMs) {
		LogUtils.logInfo(logger, "Pausing for {} milliseconds", timeInMs);
		try {
			Thread.sleep(timeInMs);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@When("I set a cookie \"(.*)\" with the content \"(.*)\"$")
	public void setCookie(String cookieName, String cookieValue) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		driver.manage().addCookie(cookie);
		LogUtils.logInfo(logger, "Setting cookie '{}' with content '{}'", cookieName, cookieValue);
	}

	@When("I delete the cookie \"(.*)\"$")
	public void deleteCookie(String cookieName) {
		driver.manage().deleteCookieNamed(cookieName);
		LogUtils.logInfo(logger, "Deleting cookie '{}'", cookieName);
	}

	@When("I press \"(.*)\"$")
	public void pressButton(String key) {
		LogUtils.logInfo(logger, "Pressing key '{}'", key);
		// Implement the logic for pressing a key if needed
	}

	@When("I (accept|dismiss) the (alertbox|confirmbox|prompt)$")
	public void handleModal(String action, String modalType) {
		Alert alert = driver.switchTo().alert();
		if ("accept".equals(action)) {
			alert.accept();
			LogUtils.logInfo(logger, "Accepting the {} modal", modalType);
		} else if ("dismiss".equals(action)) {
			alert.dismiss();
			LogUtils.logInfo(logger, "Dismissing the {} modal", modalType);
		} else {
			throw new IllegalArgumentException("Unsupported action: " + action);
		}
	}

	@When("I enter \"(.*)\" into the prompt$")
	public void setPromptText(String text) {
		Alert alert = driver.switchTo().alert();
		alert.sendKeys(text);
		LogUtils.logInfo(logger, "Entering text '{}' into the prompt", text);
	}

	@When("I scroll to element \"(.*)\"$")
	public void scroll(String elementSelector) {
		WebElement element = getElement(elementSelector);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
		LogUtils.logInfo(logger, "Scrolling to element with selector '{}'", elementSelector);
	}

	@When("I close the last opened (window|tab)$")
	public void closeLastOpenedWindow(String type) {
		if ("window".equals(type)) {
			driver.close();
			LogUtils.logInfo(logger, "Closing the last opened window");
		} else if ("tab".equals(type)) {
			Set<String> windowHandles = driver.getWindowHandles();
			String currentWindow = driver.getWindowHandle();
			for (String handle : windowHandles) {
				if (!handle.equals(currentWindow)) {
					driver.switchTo().window(handle).close();
					LogUtils.logInfo(logger, "Closing the last opened tab");
					break; // Close only the last opened tab
				}
			}
		} else {
			throw new IllegalArgumentException("Unsupported type: " + type);
		}
	}

	@When("I focus the last opened (window|tab)$")
	public void focusLastOpenedWindow(String type) {
		if ("window".equals(type)) {
			// Not applicable for the single driver used here
			LogUtils.logWarn(logger, "Cannot focus on the last opened window. This action is not supported.");
		} else if ("tab".equals(type)) {
			Set<String> windowHandles = driver.getWindowHandles();
			String lastWindow = windowHandles.iterator().next(); // Assuming last added is last opened
			driver.switchTo().window(lastWindow);
			LogUtils.logInfo(logger, "Focusing on the last opened tab");
		} else {
			throw new IllegalArgumentException("Unsupported type: " + type);
		}
	}

	@When("I select the (\\d+)(st|nd|rd|th) option for element \"(.*)\"$")
	public void selectOptionByIndex(int index, String suffix, String elementSelector) {
		WebElement selectElement = getElement(elementSelector);
		Select select = new Select(selectElement);
		if (suffix.equals("st")) {
			select.selectByIndex(index - 1);
		} else if (suffix.equals("nd")) {
			select.selectByIndex(index - 2);
		} else if (suffix.equals("rd")) {
			select.selectByIndex(index - 3);
		} else {
			select.selectByIndex(index);
		}
		LogUtils.logInfo(logger, "Selecting the {} option for element with selector '{}'", index + suffix,
				elementSelector);
	}

	@When("I select the option with the (name|value|text) \"(.*)\" for element \"(.*)\"$")
	public void selectOption(String attributeName, String attributeValue, String elementSelector) {
		WebElement selectElement = getElement(elementSelector);
		Select select = new Select(selectElement);
		List<WebElement> options = select.getOptions();
		for (WebElement option : options) {
			if (attributeName.equals("name") && option.getAttribute("name").equals(attributeValue)) {
				select.selectByVisibleText(option.getText());
				LogUtils.logInfo(logger, "Selecting the option with name '{}' for element with selector '{}'",
						attributeValue, elementSelector);
				return;
			} else if (attributeName.equals("value") && option.getAttribute("value").equals(attributeValue)) {
				select.selectByValue(option.getAttribute("value"));
				LogUtils.logInfo(logger, "Selecting the option with value '{}' for element with selector '{}'",
						attributeValue, elementSelector);
				return;
			} else if (attributeName.equals("text") && option.getText().equals(attributeValue)) {
				select.selectByVisibleText(option.getText());
				LogUtils.logInfo(logger, "Selecting the option with text '{}' for element with selector '{}'",
						attributeValue, elementSelector);
				return;
			}
		}
		throw new IllegalArgumentException("Option with " + attributeName + "=\"" + attributeValue
				+ "\" not found in element \"" + elementSelector + "\".");
	}

	@When("I move to element \"(.*)\"(?: with an offset of (\\d+),(\\d+))*$")
	public void moveTo(String elementSelector, Integer offsetX, Integer offsetY) {
		WebElement element = getElement(elementSelector);
		Actions actions = new Actions(driver);
		if (offsetX == null && offsetY == null) {
			actions.moveToElement(element).build().perform();
			LogUtils.logInfo(logger, "Moving to element with selector '{}'", elementSelector);
		} else if (offsetX != null && offsetY != null) {
			actions.moveToElement(element, offsetX, offsetY).build().perform();
			LogUtils.logInfo(logger, "Moving to element with selector '{}' and offset ({},{})", elementSelector,
					offsetX, offsetY);
		} else {
			throw new IllegalArgumentException(
					"Invalid offset combination. Both offsetX and offsetY must be provided together or neither.");
		}
	}

	@When("I switch to the iframe \"(.*)\"$")
	public void switchIFrame(String iframeSelector) {
		By locator = getLocator(iframeSelector);
		if (driver.findElements(locator).size() > 1) {
			throw new IllegalArgumentException("Multiple iFrames found with selector \"" + iframeSelector
					+ "\". Please specify an index or unique identifier.");
		}
		driver.switchTo().frame(driver.findElement(locator));
		LogUtils.logInfo(logger, "Switching to the iframe with selector '{}'", iframeSelector);
	}
}
