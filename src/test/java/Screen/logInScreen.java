package Screen;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

public class logInScreen {
    public AndroidDriver driver;

    @FindBy(xpath= "\t\n" +
            "//android.widget.Button[@content-desc=\"Get Started\"]")
    WebElement Get_Started_Button;

    @FindBy(xpath =
            "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.EditText")
    WebElement phoneNumber;

    @FindBy(xpath = "\t\n" +
            "//android.widget.Button[@content-desc=\"Continue\"]")
    WebElement Continue_Button;

    @FindBy(xpath ="/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.widget.EditText")
    WebElement OTP;

    @FindBy(xpath = "//android.widget.Button[@content-desc=\"Continue\"]")
    WebElement ContinueOtp_btn;

    @AndroidFindBy(xpath = "//*[contains(@content-desc, 'Rabeya Bushra')]")
    public WebElement Welcome_txt;

    public logInScreen(AndroidDriver driver) {
        this.driver=driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(15)), this);
    }

    public void LogIN(String phone_number,String otp) throws InterruptedException {
       Get_Started_Button.click();
       phoneNumber.click();
       Thread.sleep(2000);
       phoneNumber.sendKeys(phone_number);
       Continue_Button.click();
       OTP.click();
       Thread.sleep(2000);
       OTP.sendKeys(otp);
       ContinueOtp_btn.click();





   }


}
