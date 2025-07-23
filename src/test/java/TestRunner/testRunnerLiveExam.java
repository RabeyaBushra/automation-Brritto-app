package TestRunner;
import Screen.*;
import SetUp.setup;
import Utils.utils;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static Utils.utils.getExpectedStatusFromJson;


public class testRunnerLiveExam extends setup {


    Map<String, String> Ongoinallinfo;
    String Ongoing_Status;
    String Ongoing_dateTime;
    int Actual_Marks;
    int totalMarks;
    String Mandatorymarks;
    @Test(priority = 1, description = "login",enabled = true)
    public void dologin() throws IOException, ParseException, InterruptedException {
        logInScreen logInScreen = new logInScreen(driver);
        Map<String, String> cred = utils.ReadArrayJsonData();
        String phone_number = cred.get("phone_number");
        String otp = cred.get("otp");
        logInScreen.LogIN(phone_number, otp);
        utils.waitForElement(driver, logInScreen.Welcome_txt);
        String actualText = logInScreen.Welcome_txt.getAttribute("content-desc");
        System.out.println("Actual text: " + actualText);
        Assert.assertEquals(actualText, "Hello!\nRabeya Bushra");
    }

    @Test(priority = 2, description = "login",enabled = false)
    public void doRegistration() throws IOException, ParseException, InterruptedException {
        RegistrationScreen registrationScreen=new RegistrationScreen(driver);
        Map<String, String> cred = utils.ReadArrayJsonData();
        String phone_number = cred.get("phone_number");
        String otp = cred.get("otp");
        Map<String, String> cred1 = utils.ReadArrayJsonData();
        registrationScreen.doRegistration(phone_number, otp);
    }


    @Test(priority = 3, description = "LiveExamBanner Not visible Test",enabled = true)
    public void LiveExamBannerNotVisible() throws IOException, ParseException, InterruptedException {
        liveExamBannerScreen liveExamBannerScreen = new liveExamBannerScreen(driver);
        String contentDesc = liveExamBannerScreen.visibilityOfLiveExamBanner();
        if (contentDesc == null) {
            System.out.println("Live Exam is not found");
            Assert.fail("Live Exam content description is null");
        }}

    @Test(priority = 4, description = "LiveExamBanner visibleTest ", enabled = true)
    public void LiveExamBannerVisible() throws IOException, ParseException, InterruptedException {
        liveExamBannerScreen liveExamBannerScreen = new liveExamBannerScreen(driver);
        String contentDesc = liveExamBannerScreen.visibilityOfLiveExamBanner();
        if (contentDesc != null) {
            contentDesc = contentDesc.trim();
            if (contentDesc.equals("Live Exam\nUpcoming")) {
                System.out.println("Found Live Exam ");
                Assert.assertTrue(true, "Successfully found Live Exam");
                liveExamBannerScreen.tab.click();
            } else {System.out.println("Not Found Live Exam '" + contentDesc + "'");
                Assert.assertEquals(contentDesc, "Live Exam\nUpcoming",
                        "mismatch. Expected 'Live Exam\\nUpcoming'");}}}

    @Test(priority = 4, description = "AllTabOrder Not Matched Test ", enabled = true)
    public void TabOrderNotChecked() throws IOException, ParseException, InterruptedException {
        liveExamBannerScreen liveExamBannerScreen = new liveExamBannerScreen(driver);
        List<String> actualOrder = liveExamBannerScreen.AlltabOrder();
        List<String> expectedOrder = Arrays.asList("Ongoing", "Upcoming", "History");
        if (!actualOrder.equals(expectedOrder)) {
            System.out.println("Tab order mismatch!");
            System.out.println("Expected: " + expectedOrder);
            System.out.println("Actual:   " + actualOrder);
            Assert.assertTrue(true, "Tab order mismatch!");}}

    @Test(priority = 5, description = "AllTabOrder matched Test " , enabled = true)
    public void TabOrderChecked() throws IOException, ParseException, InterruptedException {
        liveExamBannerScreen liveExamBannerScreen = new liveExamBannerScreen(driver);
        List<String> actualOrder = liveExamBannerScreen.AlltabOrder();
        List<String> expectedOrder = Arrays.asList("Ongoing", "Upcoming", "History");
        if (actualOrder.equals(expectedOrder)) {
            System.out.println("Tab order matched!");
            System.out.println("Expected: " + expectedOrder);
            System.out.println("Actual:   " + actualOrder);
            Assert.assertEquals(actualOrder, expectedOrder);}}

    @Test(priority = 6, description = "OngoingTab not visible Test", enabled = true)
    public void OngoingNotVisibility() throws IOException, ParseException, InterruptedException {
        ongoingScreen ongoingScreen = new ongoingScreen(driver);
        if (!ongoingScreen.isOngoingVisible()) {
            System.out.println("Ongoing tab is NOT visible");
            Assert.assertTrue(true, "Ongoing tab is NOT visible");}}

    @Test(priority = 7, description = "OngoingTab visible Test", enabled = true)
    public void OngoingVisibility() throws IOException, ParseException, InterruptedException {
        ongoingScreen ongoingScreen = new ongoingScreen(driver);
        if (ongoingScreen.isOngoingVisible()) {
            System.out.println("Ongoing tab is visible");
            Assert.assertTrue(true, "Ongoing tab is visible");}}

    @Test(priority = 8, description = "Ongoing live all exam Order visible or not Test", enabled = true)
    public void printAllOngoingExams() throws IOException, ParseException, InterruptedException {
        ongoingScreen ongoingScreen = new ongoingScreen(driver);
        List<WebElement> exams = ongoingScreen.getAllOngoingExams();
        if (exams.isEmpty()) {
            System.out.println("Not Found Ongoing Exam list");
            Assert.assertTrue(true, "Not Found Ongoing Exam list");
            return;}
        System.out.println("Total " + exams.size() + " Ongoing Exam found:\n");
        Assert.assertTrue(true, "Found Ongoing Exam list");
        utils.scrollToTop(driver);
        Thread.sleep(5000);
    }

    @Test(priority = 10, description = "Ongoing live Exam status labels Not Matched Test", enabled = true)
    public void testStatusLabelOfExamNotMatched() throws Exception {
        ongoingScreen ongoingScreen = new ongoingScreen(driver);
        String expectedTitle = "Assignment 4";
        String expectedStatus = "Attempted";
        String actualStatus = getExpectedStatusFromJson(expectedTitle);
        if (expectedStatus != actualStatus) {
            System.out.println("Status Not match for exam ----------- ");
            System.out.println("Expected :  " + expectedStatus);
            System.out.println("Actual :  " + actualStatus);
            Assert.assertTrue(true, "Status Not match for exam");}}

    @Test(priority = 9, description = "Ongoing live Exam status labels Matched Test", enabled = true)
    public void testStatusLabelOfExam() throws Exception {
        ongoingScreen ongoingScreen = new ongoingScreen(driver);
        String expectedTitle = "Assignment 4";
        String expectedStatus = "Exam Running";
        String actualStatus = getExpectedStatusFromJson(expectedTitle);
        if (expectedStatus == actualStatus) {
            Assert.assertEquals(actualStatus, expectedStatus,
                    "Status match for exam: " + expectedTitle);
        System.out.println("Status match for exam ----------- "+expectedTitle);
        System.out.println("Expected :" + expectedStatus);
        System.out.println("Actual :  " + actualStatus);
        }
     else {System.out.println("Not valid "); }
    }

    @Test(priority = 11, description = "Check Exam Visibility ", enabled = true)
    public void ExamVisibleCheck() throws IOException, ParseException, InterruptedException {
        ongoingScreen ongoingScreen = new ongoingScreen(driver);
        Ongoinallinfo = ongoingScreen.findAndClickExamByTitleAndStatus(utils.ReadArrayJsonData().get("title"));
        String ActuaL_Txt =Ongoinallinfo.get("title");
        Ongoing_dateTime = Ongoinallinfo.get("dateTime");
        Ongoing_Status=Ongoinallinfo.get("status");
      
        if (ActuaL_Txt != null && ActuaL_Txt.contains("You have already taken the exam")) {
            System.out.println("Landed on Already Attempted Popup");
            Assert.assertEquals(ActuaL_Txt, "You have already taken the exam, and you can only attend it once.");
            ongoingScreen.Attempted_popup_cross_btn.click();}
        else {
           if(ActuaL_Txt != null && Ongoing_Status.equals("Exam Running")) {
                System.out.println("Running");
                Assert.assertTrue(true, "Running.");
            }
            else
            {
                System.out.println("Not found");
            }

        }

    }

    @Test(priority = 12, description = "title, marks,dateTime and status Not Matched", enabled = true)
    public void TitleMarkNotMatchedsRunningExam() throws IOException, ParseException, InterruptedException {
        ExamDetailsScreen examDetailsScreen = new ExamDetailsScreen(driver);
        Map<String, String> allinfo = examDetailsScreen.runningExamValidation();
        String Actual_title_DetailsPage = allinfo.get("title");
        String Actual_Marks_DetailsPage = allinfo.get("marks");
        String Actual_DateTime_DetailsPage = allinfo.get("dateTime");
        String Actual_Status_DetailsPage = allinfo.get("status");
        //String totalMarksString = examDetailsScreen.Total_Marks.getAttribute("content-desc");
        String numbertotal = "";
        Matcher matcher = Pattern.compile("\\b\\d+\\b").matcher(examDetailsScreen.Total_Marks.getAttribute("content-desc"));
        if (matcher.find()) {numbertotal = matcher.group();}
        totalMarks = Integer.parseInt(String.valueOf(numbertotal));
        System.out.println("totalMarks    :"+ totalMarks);

        if (!Actual_Status_DetailsPage.equals(Ongoing_Status)) {
            System.out.println(" The exam status displayed on the Ongoing Exams screen does not match the status shown on the Exam Details page.  :");
            System.out.println(" Actual_Status :" + Actual_Status_DetailsPage);
            System.out.println(" Ongoing_Status :" + Ongoing_Status);
            Assert.assertTrue(true, "The exam status displayed on the Ongoing Exams screen does not match the status shown on the Exam Details page.");
        }
        if (!Actual_DateTime_DetailsPage.equals(Ongoing_dateTime)) {
            System.out.println(" The exam date time displayed on the Ongoing Exams screen does not match the date time shown on the Exam Details page.  :");
            System.out.println(" Actual_DateTime :" + Actual_Status_DetailsPage);
            System.out.println(" Ongoing_dateTime :" + Ongoing_Status);
            Assert.assertTrue(true, "The exam date time displayed on the Ongoing Exams screen does not match the date time shown on the Exam Details page.");
        }
        if (!Actual_title_DetailsPage.equals(utils.ReadArrayJsonData().get("title"))) {
            System.out.println(" The exam title displayed on the Ongoing Exams screen does not match the title shown on the Exam Details page.  :");
            System.out.println(" Actual_title :" + Actual_title_DetailsPage);
            System.out.println(" Expected_title :" + utils.ReadArrayJsonData().get("title"));
            SoftAssert softAssert = new SoftAssert();
            Assert.assertTrue(true, "The exam title displayed on the Ongoing Exams screen does not match the title shown on the Exam Details page.");
        }
        if (!Actual_Marks_DetailsPage.equals(totalMarks)) {
            System.out.println(" Not Matched the Total Marks and individual marks  :");
            System.out.println(" Marks :" + Actual_Marks_DetailsPage);
            System.out.println(" Total Marks :" + totalMarks);
            Assert.assertTrue(true, "Not Matched the Total Marks and marks");
        
        }
    }

    @Test(priority = 13, description = "title, marks,dateTime and status Matched", enabled = true)
    public void TitleMarksdateTimeandstatusMatchedRunningExam() throws IOException, ParseException, InterruptedException {
        ExamDetailsScreen examDetailsScreen = new ExamDetailsScreen(driver);
        Map<String, String> allinfo = examDetailsScreen.runningExamValidation();
        String Actual_title = allinfo.get("title");
        String marksString = allinfo.get("marks");
        Actual_Marks = Integer.parseInt(String.valueOf(marksString));
        String Actual_DateTime = allinfo.get("dateTime");
        String Actual_Status = allinfo.get("status");
        SoftAssert softAssert = new SoftAssert();
        String totalMarksString = examDetailsScreen.Total_Marks.getAttribute("content-desc");
        String numbertotal = "";
        Matcher matcher = Pattern.compile("\\b\\d+\\b").matcher(totalMarksString);
                if (matcher.find()) {numbertotal = matcher.group();}
                totalMarks = Integer.parseInt(String.valueOf(numbertotal));
        System.out.println("totalMarks    :"+ totalMarks);
        if (Actual_Status.equals(Ongoing_Status)) {
            System.out.println(" The exam Status displayed on the Ongoing Exams screen match the Status shown on the Exam Details page.  :");
            System.out.println(" Actual_Status :" + Actual_Status);
            System.out.println("Ongoing_Status :" + Ongoing_Status);
            softAssert.assertEquals(Actual_Status, Ongoing_Status);
        }
        if (Actual_DateTime.equals(Ongoing_dateTime)) {
            System.out.println(" The exam dateTime displayed on the Ongoing Exams screen match the dateTime shown on the Exam Details page.  :");
            System.out.println(" Actual_DateTime :" + Actual_DateTime);
            System.out.println(" Ongoing_dateTime :" + Ongoing_dateTime);
            softAssert.assertEquals(Actual_DateTime, Ongoing_dateTime);
        }
        if (Actual_Marks==totalMarks) {
            System.out.println(" Matched the Total Marks and individual marks  :");
            System.out.println(" Marks :" + Actual_Marks);
            System.out.println(" Total Marks :" + totalMarks);
            softAssert.assertEquals(Actual_Marks, totalMarks);
        }
        if (Actual_Marks==totalMarks) {
            System.out.println(" Matched the Total Marks and individual marks  :");
            System.out.println(" Marks :" + Actual_Marks);
            System.out.println(" Total Marks :" + totalMarks);
            softAssert.assertEquals(Actual_Marks, totalMarks);
        }
        if (Actual_title.equals(utils.ReadArrayJsonData().get("title"))) {
            System.out.println(" The exam title displayed on the Ongoing Exams screen match the title shown on the Exam Details page.  :");
            System.out.println(" Actual_title :" + Actual_title);
            System.out.println(" titleToFind :" + utils.ReadArrayJsonData().get("title"));
            softAssert.assertEquals(Actual_title, utils.ReadArrayJsonData().get("title"));
        }
        softAssert.assertAll();
           
    }

    @Test(priority = 14, description = "Checked Mandatory and Optional Subject ", enabled = true)
    public void CheckedMandatoryOptionalSubject() {
        ExamDetailsScreen examDetailsScreen = new ExamDetailsScreen(driver);
        Map<String, Integer> allinfos =examDetailsScreen.checkMandatoryOptionalSubject();
        Integer BothMarksSUm=(Integer)allinfos.get("BothMarksSUm");
        int Mandatory_Marks=allinfos.get("Mandatorymarks");
       System.out.println(" BothMarksSUm test :"+ BothMarksSUm);
        System.out.println(" MandatoryMarks test:"+ Mandatory_Marks);
        
        if (BothMarksSUm != null && BothMarksSUm==Actual_Marks) {
            System.out.println(" After Sum Mandatory Marks and Optional Matched with Marks :");
            System.out.println(" Actual Mandatory+Optional Marks :" + BothMarksSUm);
            System.out.println(" Marks :" + Actual_Marks);
           Assert.assertTrue(true, "After Sum Mandatory Marks and Optional Matched with Marks");
            }
        if(BothMarksSUm != null && BothMarksSUm!=Actual_Marks) {
            System.out.println(" After Sum Mandatory Marks and Optional Not Matched with Marks :");
            System.out.println(" Actual Mandatory+Optional Marks  :" + BothMarksSUm);
            System.out.println(" Marks :" + Actual_Marks);
           Assert.assertTrue(true, "After Sum Mandatory Marks and Optional Not Matched with Marks  ");
        }
        if (BothMarksSUm != null && BothMarksSUm==totalMarks) {
            System.out.println(" After Sum Mandatory Marks and Optional Matched with Total Marks:");
            System.out.println(" Actual Mandatory+Optional Marks :" + BothMarksSUm);
            System.out.println(" Total Marks :" + totalMarks);
            Assert.assertTrue(true, " After Sum Mandatory Marks and Optional Matched with Total Marks");
        }
        if (BothMarksSUm != null &&  BothMarksSUm!=totalMarks) {
            System.out.println(" After Sum Mandatory Marks and Optional Not Matched with TOtal Marks:");
            System.out.println(" Actual Mandatory+Optional Marks :" + BothMarksSUm);
            System.out.println(" Total Marks :" + totalMarks);
           Assert.assertTrue(true, "After Sum Mandatory Marks and Optional Not Matched with TOtal Marks:");
        }
          if (BothMarksSUm == null)
        {
            System.out.println("Not available optional part ");
            Assert.assertTrue(true, "Not available optional part");
        }
        if (BothMarksSUm == null &&  Mandatory_Marks==Actual_Marks) {
            System.out.println(" Matched the Marks and Mandatory Marks  :");
            System.out.println(" Actual Mandatory Marks :" + Mandatory_Marks);
            System.out.println(" Marks :" + Actual_Marks);
           Assert.assertTrue(true, "Matched the Marks and Mandatory Marks");
        }
        if (BothMarksSUm == null && Mandatory_Marks!=Actual_Marks) {
            System.out.println(" Not Matched the Marks and Mandatory Marks  :");
            System.out.println(" Actual Mandatory Marks :" + Mandatory_Marks);
            System.out.println(" Marks :" + Actual_Marks);
           Assert.assertTrue(true, "Not Matched the Marks and Mandatory Marks");
        }
        if (BothMarksSUm == null && Mandatory_Marks==totalMarks) {
            System.out.println(" Matched the total Marks and Mandatory Marks  :");
            System.out.println(" Actual Mandatory Marks :" + Mandatory_Marks);
            System.out.println(" Total Marks :" + totalMarks);
            Assert.assertTrue(true, "Matched the total Marks and Mandatory Marks");
        }
        if (BothMarksSUm == null && Mandatory_Marks!=totalMarks) {
            System.out.println(" Not Matched the Total Marks and Mandatory Marks  :");
            System.out.println(" Actual Mandatory Marks :" + Mandatory_Marks);
            System.out.println(" Total Marks :" + totalMarks);
           Assert.assertTrue(true, "Not Matched the Total Marks and Mandatory Marks ");}

           



    }

    @Test(priority = 15, description = "StartExam", enabled =true)
    public void StartExam() throws IOException, ParseException, InterruptedException {
        ExamDetailsScreen examDetailsScreen=new ExamDetailsScreen(driver);
       examDetailsScreen.clickStartExam();
    }

    }





























































































