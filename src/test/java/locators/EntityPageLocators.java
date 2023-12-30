package locators;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EntityPageLocators {
	@FindBy(css = "span.labelStyle")
	public WebElement loc_txt_entityname;

	@FindBy(css = "input[type='text']")
	public WebElement loc_txt_entityid;

	public EntityPageLocators(WebDriver driver) {
		PageFactory.initElements(driver, this);

	}

}
