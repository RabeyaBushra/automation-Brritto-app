package Screen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExamDetailsScreen {

    public AndroidDriver driver;
    @FindBy(xpath = "//android.view.View[contains(@content-desc, 'Date & Time:') and " +
            "contains(@content-desc, 'Questions') and " +
            "contains(@content-desc, 'Min') and " +
            "contains(@content-desc, 'Marks')]")
    WebElement currentExam;
    @FindBy(xpath ="//android.view.View[contains(@content-desc, 'Total')]")
    //@FindBy(xpath = "//android.view.View[contains(@content-desc, 'Total:') and contains(@content-desc, 'marks')]")
    public WebElement Total_Marks;
    @FindBy(xpath = "//android.view.View[contains(@content-desc, 'marks')]")
    public WebElement Mandatory_subject_marks;
    @FindBy(xpath = "//android.widget.CheckBox[contains(@content-desc, 'marks')]")
    public WebElement optional_subject_marks;
    @FindBy(xpath = "//android.widget.Button[@content-desc=\"Start Exam\"]")
    public WebElement start_exam;


    public ExamDetailsScreen(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(15)), this);
    }


    public HashMap<String, String> runningExamValidation() {
        HashMap<String, String> examInfo = new HashMap<>();
        try {
            String desc = currentExam.getAttribute("content-desc");
            Pattern pattern = Pattern.compile("(\\d+)\\s*Marks(?!.*\\d+\\s*Marks)");
            Matcher matcher = pattern.matcher(desc);
            String marks = matcher.find() ? matcher.group(1) : "N/A";
            examInfo.put("marks", marks);

            WebElement statusElement = currentExam.findElement(
                    By.xpath(".//android.view.View[contains(@content-desc, 'Exam')]")
            );
            String status = statusElement.getAttribute("content-desc");
            examInfo.put("status", status);
            Pattern datePattern = Pattern.compile("Date & Time:\\s*(\\d{1,2}\\s\\w+,\\s*\\d{1,2}:\\d{2}\\s*[ap]m\\s*-\\s*\\d{1,2}\\s\\w+,\\s*\\d{1,2}:\\d{2}\\s*[ap]m)");
            Matcher dateMatcher = datePattern.matcher(desc);

            String dateTime = "";
            if (dateMatcher.find()) {
                dateTime = dateMatcher.group(1).trim();
            }
            examInfo.put("dateTime", dateTime);
            String title;
            try {
                title = desc.split("Date & Time:")[0].trim();
            } catch (Exception e) {
                title = desc;
            }
            examInfo.put("title", title);

        } catch (Exception e) {
            System.err.println("Error during validation: " + e.getMessage());
            examInfo.put("error", e.getMessage());
        }
        System.out.println("Title: " + examInfo.get("title"));
        System.out.println("Status: " + examInfo.get("status"));
        System.out.println("Marks: " + examInfo.get("marks"));
        System.out.println("dateTime: " + examInfo.get("dateTime"));

        return examInfo;
    }


    public HashMap<String, Integer> checkMandatoryOptionalSubject() {
        HashMap<String, Integer> examDeatilsInfo = new HashMap<>();
        String Mandatorymarks;
        String Mandatory_checked = Mandatory_subject_marks.getAttribute("checkable");
            if (("false".equalsIgnoreCase(Mandatory_checked))) {
                System.out.println("Already selected");
                Assert.assertTrue(true, "Mandatory Subject is already selected");
                String fullDesc = Mandatory_subject_marks.getAttribute("content-desc");
                String number = "";
                Matcher matcher = Pattern.compile("\\b\\d+\\b").matcher(fullDesc);
                if (matcher.find()) {
                    number = matcher.group();
                }
                Mandatorymarks = number;
                int Mandatorymark = Integer.parseInt(String.valueOf(Mandatorymarks));
                examDeatilsInfo.put("Mandatorymarks", Mandatorymark);
                System.out.println(" Actual Mandatory Marks :" + Mandatorymark);
                 try {
                   WebElement optional_subject_marks = driver.findElement(
                            By.xpath("//android.widget.CheckBox[contains(@content-desc, 'marks')]")
                    );

                if ("true".equalsIgnoreCase(optional_subject_marks.getAttribute("checkable"))) {
                    optional_subject_marks.click();
                    String fullDesc1 = optional_subject_marks.getAttribute("content-desc");
                    String number2 = "";
                    Matcher matcher2 = Pattern.compile("\\b\\d+\\b").matcher(fullDesc1);
                    if (matcher2.find()) {
                        number2 = matcher2.group();
                    }
                    String Optionalmarks = number2;
                    System.out.println("Optional marks: " + Optionalmarks);
                    int BothMarksSUm = Integer.parseInt(String.valueOf(Optionalmarks)) + Mandatorymark;
                    System.out.println(" Actual Mandatory+Optional Marks :" + BothMarksSUm);
                    examDeatilsInfo.put("BothMarksSUm", BothMarksSUm);
                } else {
                    System.out.println("Optional subject Not Found");

                }
                     } catch (NoSuchElementException e) {
                    System.out.println("✅ Optional subject checkbox not found. Skipping optional marks calculation.");
                }

            }

        return examDeatilsInfo;
    }
        public void clickStartExam () {
            start_exam.click();
        }

}
