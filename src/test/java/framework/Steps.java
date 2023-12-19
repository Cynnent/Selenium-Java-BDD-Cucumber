package framework;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class Steps {

	private WebDriver driver;

	@Given("user is on home page")
	public void user_is_on_home_page() {
//		driver = new ChromeDriver();
		driver=Hooks.getThreadSafeDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		driver.get("https://shop.demoqa.com");
	}

	@When("he search for {string}")
	public void he_search_for(String string) {
		driver.navigate().to("https://shop.demoqa.com/?s=" + string + "&post_type=product");
	}

	@When("choose to buy the first item")
	public void choose_to_buy_the_first_item() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<WebElement> items = driver.findElements(By.cssSelector(".noo-product-inner"));
		items.get(0).click();

		Select color_drp = new Select(driver.findElement(By.id("pa_color")));
		color_drp.selectByVisibleText("White");

		Select size_drp = new Select(driver.findElement(By.id("pa_size")));
		size_drp.selectByVisibleText("Small");

		WebElement addToCart = driver.findElement(By.cssSelector("button.single_add_to_cart_button"));
		addToCart.click();

	}

	@When("moves to check the mini cart")
	public void moves_to_check_the_mini_cart() {
		WebElement cart = driver.findElement(By.cssSelector(".cart-button"));
		cart.click();

		WebElement continueToCheckout = driver.findElement(By.cssSelector(".checkout-button.alt"));
		continueToCheckout.click();
	}

	@When("enter personal details on checkout page")
	public void enter_personal_details_on_checkout_page() {
		WebElement firstName = driver.findElement(By.cssSelector("#billing_first_name"));
		firstName.sendKeys("Demo");

		WebElement lastName = driver.findElement(By.cssSelector("#billing_last_name"));
		lastName.sendKeys("Test");

		WebElement emailAddress = driver.findElement(By.cssSelector("#billing_email"));
		emailAddress.sendKeys("test@gmail.com");

		WebElement phone = driver.findElement(By.cssSelector("#billing_phone"));
		phone.sendKeys("0126345789");// 07438862327

		Select billing_country = new Select(driver.findElement(By.id("billing_country")));
		billing_country.selectByVisibleText("India");

		WebElement city = driver.findElement(By.cssSelector("#billing_city"));
		city.sendKeys("Delhi");

		WebElement address = driver.findElement(By.cssSelector("#billing_address_1"));
		address.sendKeys("Shalimar Bagh");

		WebElement postcode = driver.findElement(By.cssSelector("#billing_postcode"));
		postcode.sendKeys("110088");

	}

	@When("place the order")
	public void place_the_order() {
		WebElement acceptTC = driver.findElement(By.cssSelector("#terms.input-checkbox"));
		acceptTC.click();

		WebElement placeOrder = driver.findElement(By.cssSelector("#place_order"));
		placeOrder.submit();
	}

}
