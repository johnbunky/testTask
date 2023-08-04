package testTask;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UITest {
    private WebDriver driver;

    @BeforeClass
    public void setUp() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(new URL("http://52.14.11.186:4444/wd/hub"), options);
    }

    @Test
    public void testBookstore() {
        driver.get("https://www.google.com");

        /*WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("Selenium testing");
        searchBox.submit();

        WebElement resultStats = driver.findElement(By.id("resultStats"));
        Assert.assertNotNull(resultStats);*/
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

