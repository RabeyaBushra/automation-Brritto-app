package Screen;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.*;

import java.time.Duration;

public class liveExamBannerScreen {
    public AndroidDriver driver;
    public WebElement tab;
    @FindBy(xpath = "//*[contains(@content-desc, 'Live Exam')]")
    public WebElement liveExamTab_banner;
    @FindBy(xpath = "//*[contains(@content-desc,'Ongoing Tab')]")
    public WebElement ongoing_page;
    @FindBy(xpath = "//android.view.View[contains(@content-desc, 'Tab')]")
    public List<WebElement> tabList;

    public liveExamBannerScreen(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(15)), this);
    }

    public String visibilityOfLiveExamBanner() throws InterruptedException {

        tab = driver.findElement(By.xpath("//*[contains(@content-desc, 'Live Exam')]"));
        String contentDesc = tab.getAttribute("content-desc");
        return contentDesc;

    }
    public List<String> AlltabOrder() {

        List<String> order = new ArrayList<>();

        for (WebElement tab : tabList) {
            String text = tab.getAttribute("content-desc");
            String label = text.split("\n")[0].trim();
            order.add(label);
        }

        return order;
    }





    }






















