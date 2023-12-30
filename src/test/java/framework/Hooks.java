package framework;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import framework.utils.LogUtils;
import framework.utils.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Hooks {

	private static final ThreadLocal<Logger> logger = ThreadLocal.withInitial(LogManager::getLogger);
	private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static final ThreadLocal<TestContext> testContext = ThreadLocal.withInitial(TestContext::new);
	private static final ThreadLocal<WebDriverWait> wait=new ThreadLocal<>();

	@Before
	public void setup() {
		String browser = System.getProperty("browser");
		String version = "1.0"; // System.getProperty("version");
		try {
			// Initialize the logger
			logger.set(LogManager.getLogger("[" + browser + "/" + version + "] - " + this.getClass().getName()));

			if (browser == null) {
				chromedriver();
			} else {
				switch (browser) {
				case "chrome":
					chromedriver();
					break;
				case "firefox":
					firefoxdriver();
					break;
				case "edge":
					edgedriver();
					break;
				default:
					chromedriver();
					break;
				}
			}

			LogUtils.logInfo(logger.get(), "Opening Browser...." + browser);
			LogUtils.logInfo(logger.get(), "Navigating to the home page");
		} catch (Exception e) {
			LogUtils.logError(logger.get(), "WebDriver initialization: {}", e.toString());
		}
	}

	private void chromedriver() {
		WebDriverManager.chromedriver().setup();
		driver.set(new ChromeDriver());
		wait.set(new WebDriverWait(getThreadSafeDriver(), Duration.ofSeconds(10,0)));
	}

	private void edgedriver() {
//		System.setProperty("webdriver.edge.driver", "./Drivers/edgedriver.exe");
		WebDriverManager.edgedriver().setup();
		driver.set(new EdgeDriver());
		wait.set(new WebDriverWait(getThreadSafeDriver(), Duration.ofSeconds(10,0)));
	}
	
	private void firefoxdriver() {
//		System.setProperty("webdriver.gecko.driver", "./Drivers/GeckoDriver.exe");
		WebDriverManager.firefoxdriver().setup();
		FirefoxOptions options = new FirefoxOptions();
		driver.set(new FirefoxDriver(options));
		wait.set(new WebDriverWait(getThreadSafeDriver(), Duration.ofSeconds(10,0)));
	}

	@After
	public void teardown(Scenario scenario) {
		afterScenario(scenario);
		getThreadSafeDriver().quit();
	}

	public static synchronized WebDriver getThreadSafeDriver() {
		WebDriver localDriver = driver.get();
		if (localDriver == null) {
			synchronized (driver) {
				localDriver = driver.get();
//				if (localDriver == null) {
//					localDriver = new ChromeDriver();
//					driver.set(localDriver);
//				}
			}
		}
		return localDriver;
	}

	public void afterScenario(Scenario scenario) {
		if (scenario.isFailed()) {
			// Take a screenshot
			byte[] screenshot = ((TakesScreenshot) getThreadSafeDriver()).getScreenshotAs(OutputType.BYTES);
			// Embed the screenshot in the report
			scenario.attach(screenshot, "image/png", "Screenshot");

		}
		// Close or quit the WebDriver instance
	}

	public static TestContext getThreadSafeTestContext() {
		return testContext.get();
	}

	public static Logger getThreadSafeLogger() {
		return logger.get();
	}
	
	public static WebDriverWait getThreadSafeWait() {
		return wait.get();
	}
	
}
