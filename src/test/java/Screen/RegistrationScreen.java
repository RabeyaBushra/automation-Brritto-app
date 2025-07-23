package Screen;

import Utils.utils;
import com.github.javafaker.Faker;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.List;

public class RegistrationScreen {

    public AndroidDriver driver;

    @FindBy(xpath="//android.widget.Button[@content-desc=\"Get Started\"]")
    WebElement Get_Started_Button;
    @FindBy(xpath =
            "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.EditText")
    WebElement RegistrationphoneNumber;
    @FindBy(xpath ="//android.widget.Button[@content-desc=\"Continue\"]")
    WebElement Continue_Button;
    @FindBy(xpath ="/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.widget.EditText")
    WebElement RegistrationOTP;
    @FindBy(xpath = "//android.widget.Button[@content-desc=\"Continue\"]")
    WebElement ContinueOtp_btn;
    @FindBy(className = "android.widget.EditText")
    public List<WebElement> Txt_field;
    @AndroidFindBy(accessibility="Select district")
    public WebElement districtSelector;
    @FindBy(xpath ="//android.widget.Button[@content-desc='Bandarban']")
    public WebElement Bandarban_option;
    @FindBy(xpath = "//android.widget.RadioButton[@content-desc=\"Female\"]")
    public WebElement Gender ;
        @FindBy(xpath ="/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.widget.ScrollView")
    public WebElement scrolling;
    @AndroidFindBy(accessibility="Select class")
    public WebElement your_class;
    @AndroidFindBy(accessibility ="aaaa dummy 3")
    public WebElement select_text;
    @FindBy(xpath ="//android.widget.Button[@content-desc=\"Class Test 4.0\"]")
    public WebElement class_select;
    @FindBy(xpath ="//android.widget.Button[@content-desc=\"Sign up\"]")
    public WebElement sign_up;



    public RegistrationScreen(AndroidDriver driver) {
        this.driver=driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(15)), this);
    }

    public void doRegistration(String phone_number,String otp) throws InterruptedException {
        Get_Started_Button.click();
        RegistrationphoneNumber.click();
        Thread.sleep(2000);
        RegistrationphoneNumber.sendKeys(phone_number);
        Continue_Button.click();
        RegistrationOTP.click();
        Thread.sleep(2000);
        RegistrationOTP.sendKeys(otp);
        ContinueOtp_btn.click();
        Thread.sleep(2000);
        Txt_field.get(0).click();
        Faker faker = new Faker();
        String name = faker.name().fullName();
        Thread.sleep(2000);
        Txt_field.get(0).sendKeys(name);
        districtSelector.click();
        Thread.sleep(1000);
        Bandarban_option.click();
        Thread.sleep(2000);
        Gender.click();
        Thread.sleep(2000);
        Txt_field.get(1).click();
        String email = "user" +"@test.com";
        Txt_field.get(1).sendKeys(email);
        Thread.sleep(7000);
            ((JavascriptExecutor) driver).executeScript(
                    "mobile: scrollGesture",
                    ImmutableMap.of(
                            "left", 100, "top", 100,
                            "width", 500, "height", 1000,
                            "direction", "down",
                            "percent", 1.5  // Scroll 1.5x screen height
                    )
            );

        Txt_field.get(2).click();
        select_text.click();
        Thread.sleep(7000);
        your_class.click();
        class_select.click();
        sign_up.click();
        System.out.println("SuccessFully Registered-------------------");


    }
}
