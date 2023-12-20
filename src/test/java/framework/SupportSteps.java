package framework;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import framework.utils.PageLocatorsManager;

public abstract class SupportSteps {

	
	protected WebDriver driver;
	
	protected SupportSteps(WebDriver driver) {
		this.driver=driver;
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
}
