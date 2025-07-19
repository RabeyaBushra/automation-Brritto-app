package Utils;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Sequence;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class utils {
    public static Map<String, String> ReadArrayJsonData() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(new FileReader(".\\src\\test\\resources\\user.json"));
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
        String phone_number = jsonObject.get("phone_number").toString();
        String otp = jsonObject.get("otp").toString();
        String title = jsonObject.get("title").toString();
        Map<String, String> credentials = new HashMap<>();
        credentials.put("phone_number", phone_number);
        credentials.put("otp", otp);
        credentials.put("title", title);
        return credentials;
    }

    public static void waitForElement(AndroidDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static boolean ScrollDown(AndroidDriver driver) {
        try {

            List<WebElement> scrollableElements = driver.findElements(By.xpath("//*[@scrollable='true']"));

            if (!scrollableElements.isEmpty()) {
                WebElement scrollableElement = scrollableElements.get(0);
                Map<String, Object> params = new HashMap<>();
                params.put("elementId", ((RemoteWebElement)scrollableElement).getId());
                params.put("direction", "down");
                params.put("percent", 1.0);
                params.put("speed", 1500);
                driver.executeScript("mobile: scrollGesture", params);
            } else {

                Dimension size = driver.manage().window().getSize();
                int startX = size.width / 2;
                int startY = (int)(size.height * 0.85);
                int endY = (int)(size.height * 0.15);

                new TouchAction(driver)
                        .press(PointOption.point(startX, startY))
                        .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1200)))
                        .moveTo(PointOption.point(startX, endY))
                        .release()
                        .perform();
            }

            return true;
        } catch (Exception e) {
            System.out.println("Scrolling Issue: " + e.getMessage());
            return false;
        }
    }




    public static void waitForElementToLoad(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void saveCredentials( int index,String title ,String status,String desc) throws IOException, ParseException {
        JSONParser jsonParser=new JSONParser();
        JSONArray jsonArray= (JSONArray) jsonParser.parse(new FileReader("./src/test/resources/ExamList.json"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("index", index);
        jsonObject.put("title", title);
        jsonObject.put("status", status);
        jsonObject.put("details", desc);

        jsonArray.add(jsonObject);
        try (FileWriter file = new FileWriter("./src/test/resources/ExamList.json")) {
            file.write(jsonArray.toJSONString());
            file.flush();
            System.out.println("Credentials saved to ExamList.json");
        } catch (IOException e) {
            System.err.println("Failed to write to user.json: " + e.getMessage());
        }
    }

    public static void scrollToTop(AndroidDriver driver) {
        try {
            System.out.println("Starting scroll to top on Vivo device...");
            try {
                driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true)).scrollToBeginning(10)"));
                System.out.println("Scrolled to top using UIAutomator");
                return;
            } catch (Exception e) {
                System.out.println("UIAutomator scroll failed: " + e.getMessage());
            }
            try {
                Dimension size = driver.manage().window().getSize();
                Map<String, Object> params = new HashMap<>();
                params.put("left", size.width * 0.1);
                params.put("top", size.height * 0.1);
                params.put("width", size.width * 0.8);
                params.put("height", size.height * 0.8);
                params.put("direction", "up");
                params.put("percent", 3.0);
                params.put("speed", 20000);
                driver.executeScript("mobile: scrollGesture", params);
                System.out.println("Used mobile scroll gesture");
                return;
            } catch (Exception e) {
                System.out.println("Mobile gesture failed: " + e.getMessage());
            }
            try {
                Dimension size = driver.manage().window().getSize();
                int startX = size.width / 2;
                int startY = (int)(size.height * 0.8);
                int endY = (int)(size.height * 0.2);
                new TouchAction(driver)
                        .press(PointOption.point(startX, startY))
                        .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1500)))
                        .moveTo(PointOption.point(startX, endY))
                        .release()
                        .perform();

                System.out.println("scroll performed");
            } catch (Exception e) {
                System.out.println("scroll failed: " + e.getMessage());
            }
            try {
                WebElement firstExam = driver.findElement(By.xpath(
                        "(//*[contains(@content-desc,'Date & Time:')])[1]"));
                System.out.println("Verified top exam: " +
                        firstExam.getAttribute("content-desc").split("Date & Time:")[0].trim());
            } catch (Exception e) {
                System.out.println("verification failed");
            }
        } catch (Exception e) {
            System.out.println("Error in scrollToTop: " + e.getMessage());
        }
    }


    public static String getExpectedStatusFromJson(String titleToSearch) throws Exception {
        JSONParser parser = new JSONParser();
        JSONArray exams = (JSONArray) parser.parse(new FileReader("./src/test/resources/ExamList.json"));

        for (Object obj : exams) {
            JSONObject exam = (JSONObject) obj;
            String title = (String) exam.get("title");

            if (title.equalsIgnoreCase(titleToSearch)) {
                return (String) exam.get("status");
            }
        }

        throw new Exception("Exam titled '" + titleToSearch + "' not found in JSON.");
    }

public  static boolean alternativeScroll(AndroidDriver driver) {
    try {
        // Try JavaScript scroll first
        ((JavascriptExecutor)driver).executeScript(
                "mobile: scrollGesture",
                ImmutableMap.of(
                        "left", 100, "top", 100,
                        "width", 500, "height", 1000,
                        "direction", "down",
                        "percent", 3.0
                )
        );
        return true;
    } catch (Exception e) {
        System.out.println("Standard scroll failed, trying touch action...");
        try {
            Dimension size = driver.manage().window().getSize();
            new TouchAction(driver)
                    .press(PointOption.point(size.width/2, (int)(size.height*0.8)))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
                    .moveTo(PointOption.point(size.width/2, (int)(size.height*0.2)))
                    .release()
                    .perform();
            return true;
        } catch (Exception e2) {
            System.out.println("Scroll failed completely: " + e2.getMessage());
            return false;
        }
    }
}


}
