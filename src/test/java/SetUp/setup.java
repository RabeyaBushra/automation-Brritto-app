package SetUp;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Base64;
import java.nio.file.Files;
public class setup {
    public AndroidDriver driver;
    @BeforeTest
    public AndroidDriver setUP() throws MalformedURLException {
        DesiredCapabilities caps=new DesiredCapabilities();
        caps.setCapability("appium:deviceName","my device");
        caps.setCapability("platformName","Android");
        caps.setCapability("appium:automationName","UiAutomator2");
        caps.setCapability("appium:platformVersion","13");
        caps.setCapability("appium:uuid","1361160077000IA");
        caps.setCapability("appium:appPackage","tech.innospace.brritto");
        caps.setCapability("appium:appActivity","tech.innospace.brritto.MainActivity");
        caps.setCapability("appium:app",System.getProperty("user.dir") + "/src/test/resources/brritto-automation-test.apk");
        URL url =new URL("http://127.0.0.1:4723");

        driver=new AndroidDriver(url,caps);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(40));

        return driver;
    }

    }
   @AfterTest
   public void quit_()
   {
       driver.quit();
   }

