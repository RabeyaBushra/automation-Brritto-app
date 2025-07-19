package Screen;

import Utils.utils;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Utils.utils.waitForElementToLoad;

public class ongoingScreen {
    public AndroidDriver driver;

    @FindBy(xpath = "//android.view.View[@content-desc='Ongoing\nTab 1 of 3']")
    WebElement ongoingTab;

    @FindBy(xpath = "//android.view.View[@content-desc=\"Dismiss\"]/android.view.View/android.view.View/android.view.View/android.view.View\n")
    WebElement Attempted_popup;

    @FindBy(xpath = "//android.view.View[contains(@content-desc, 'You have already taken')]")
    WebElement Attempted_text;

    @FindBy(xpath = "//android.view.View[@content-desc=\"Dismiss\"]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]")
    public WebElement Attempted_popup_cross_btn;


    public ongoingScreen(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(15)), this);
    }


    public boolean isOngoingVisible() {
        return ongoingTab.isDisplayed();
    }


    public List<WebElement> getAllOngoingExams() {
        List<WebElement> allExams = new ArrayList<>();
        Set<String> foundExamTitles = new HashSet<>();
        int initialVisibleExams = driver.findElements(By.xpath(
                "//android.view.View[contains(@content-desc, 'Date & Time:')]")).size();
        int maxScrolls = initialVisibleExams > 0 ? (50 / initialVisibleExams) + 5 : 5;
        int scrollCount = 0;
        boolean foundNewExam = true;
        int retryCount = 0;
        final int MAX_RETRY = 2;

        int idx = 0;

        while (scrollCount < maxScrolls && foundNewExam) {
            foundNewExam = false;

            utils.waitForElementToLoad(2);

            List<WebElement> currentExams = driver.findElements(By.xpath(
                    "//android.view.View[contains(@content-desc, 'Date & Time:') and " +
                            "contains(@content-desc, 'Questions') and " +
                            "contains(@content-desc, 'Min') and " +
                            "contains(@content-desc, 'Marks')]"
            ));

            for (WebElement exam : currentExams) {
                String examTitle = exam.getAttribute("content-desc").split("Date & Time:")[0].trim();
                if (!foundExamTitles.contains(examTitle)) {
                    foundExamTitles.add(examTitle);
                    allExams.add(exam);
                    printExamInfo(exam, idx);
                    idx++;
                    foundNewExam = true;
                    retryCount = 0;
                }
            }

            if (foundNewExam) {
                if (!utils.ScrollDown(driver)) {
                    break;
                }
                utils.waitForElementToLoad(2);
            } else {
                retryCount++;
                if (retryCount >= MAX_RETRY) {
                    System.out.println("New Live Exam Not found");
                    break;
                }
                if (!utils.ScrollDown(driver)) {
                    break;
                }
                utils.waitForElementToLoad(2);
            }

            scrollCount++;
        }
        return allExams;
    }


    public void printExamInfo(WebElement examBlock, int index) {
        try {
            String desc = examBlock.getAttribute("content-desc");
            String title = desc.split("Date & Time:")[0].trim();
            String status = "Unknown";
            try {
                WebElement statusElement = examBlock.findElement(By.xpath(".//android.view.View[contains(@content-desc, 'Exam')]"));
                status = statusElement.getAttribute("content-desc");
            } catch (Exception e) {
                System.out.println("Could not find status element.");

                if (desc.contains("Exam Attempted")) {
                    status = "Exam Attempted";
                } else if (desc.contains("Exam Running")) {
                    status = "Exam Running";
                }
            }

            System.out.println("\nExam #" + index);
            System.out.println("Title: " + title);
            System.out.println("Status: " + status);
            System.out.println("Details: " + desc);
            System.out.println("----------------------------------");

            utils.saveCredentials(index, title, status, desc);

        } catch (Exception e) {
            System.out.println("Error parsing exam :" + index + ": " + e.getMessage());
        }
    }

    public HashMap<String, String> findAndClickExamByTitleAndStatus(String titleToFind) {
        final int MAX_SCROLLS = 15;
        HashMap<String, String> examInfo = new HashMap<>();
        Set<String> seenTitles = new HashSet<>();
        String lastTitle = "";
        for (int scrollCount = 0; scrollCount < MAX_SCROLLS; scrollCount++) {
            List<WebElement> exams = driver.findElements(
                    By.xpath("//android.view.View[contains(@content-desc, 'Date & Time:')]")
            );
            System.out.println("Found " + exams.size() + " exams in scroll " + (scrollCount + 1));
            if (!exams.isEmpty()) {
                String currentFirstTitle = exams.get(0).getAttribute("content-desc").split("Date & Time:")[0].trim();
                if (currentFirstTitle.equals(lastTitle)) {
                    System.out.println("Stuck on same content - trying alternative scroll");
                    utils.alternativeScroll(driver);
                }
                lastTitle = currentFirstTitle;
            }
            for (WebElement exam : exams) {
                String desc = exam.getAttribute("content-desc");
                if (desc == null) continue;
                String title = desc.split("Date & Time:")[0]
                        .split("[,.\n]")[0]
                        .trim()
                        .replaceAll("\\s+", " ");
                if (seenTitles.contains(title)) continue;
                seenTitles.add(title);
                if (title.equals(titleToFind)) {
                    System.out.println("Checking title : '" + title + "'");
                        Pattern datePattern = Pattern.compile("Date & Time:\\s*(\\d{1,2}\\s\\w+,\\s*\\d{1,2}:\\d{2}\\s*[ap]m\\s*-\\s*\\d{1,2}\\s\\w+,\\s*\\d{1,2}:\\d{2}\\s*[ap]m)");
                        Matcher dateMatcher = datePattern.matcher(desc);
                        String dateTime = "";
                        if (dateMatcher.find()) {
                dateTime = dateMatcher.group(1).trim();}
                   System.out.println("Checking   date : '" + dateTime + "'");
                    WebElement statusElement = exam.findElement(
                            By.xpath(".//android.view.View[contains(@content-desc, 'Exam Running') or contains(@content-desc, 'Exam Attempted')]")
                    );

                    String status;
                    status = statusElement.getAttribute("content-desc");
                    System.out.println(status + "' is Running. Clicking now...");
                    if ("Exam Running".equals(status)) {
                        System.out.println(title + "' is Running. Clicking now...");
                        exam.click();
                    } else {
                        System.out.println(title + "' is not Running (status: " + status + ")");
                        //exam.click();
                    }
                    examInfo.put("title", title);
                    examInfo.put("dateTime", dateTime);
                    examInfo.put("status", status);
                    return examInfo;
                    }
            }
            System.out.println("scrolling down (attempt " + (scrollCount + 1) + ")...");
            if (!utils.alternativeScroll(driver)) {
                System.out.println("Scroll failed - ending search");
                break;
            }
            try { Thread.sleep(3000); } catch (InterruptedException e) { }
        }
        System.out.println("Exam '" + titleToFind + "' not found after " + MAX_SCROLLS + " scrolls");
        return examInfo;
    }


}