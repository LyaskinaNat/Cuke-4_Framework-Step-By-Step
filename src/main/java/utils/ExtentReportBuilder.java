package utils;

import cucumber.TestContext;
import cucumber.api.Scenario;
import managers.FileReaderManager;
import managers.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.apache.commons.io.FileUtils;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ExtentReportBuilder {

    TestContext testContext;
    WebDriverManager driverManager;
    private  String screenshotName;
    private String saveScreenshotsTo;
    private  String archivedReportName;
    private String saveArchiveReportsTo;
    private String latestReportPath;

    public ExtentReportBuilder (TestContext context) {
        testContext = context;
        driverManager = testContext.getWebDriverManager();
        saveScreenshotsTo = FileReaderManager.getInstance().getExtentReportConfigReader().getSaveScreenShotsTo();
        saveArchiveReportsTo = FileReaderManager.getInstance().getExtentReportConfigReader().getSaveArchiveReportsTo();
        latestReportPath = FileReaderManager.getInstance().getExtentReportConfigReader().getCurrentReportPath();
    }

    public void additionalReportInfo(Scenario scenario)  {
        Capabilities cap = ((RemoteWebDriver) driverManager.getDriver()).getCapabilities();
        String browserName = cap.getBrowserName().toUpperCase();
        String browserVersion = cap.getVersion();
        scenario.write("Executed by: " + System.getProperty("user.name"));
        scenario.write("Platform: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") v." + System.getProperty("os.version"));
        scenario.write("Browser: " + browserName + " v. " + browserVersion);
    }

    public String dateAdjustment(int number) {
        return (number < 10) ? ("0" + number) : Integer.toString(number);
    }

    public String returnDateStamp() {
        Date d = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        int yr = calendar.get(Calendar.YEAR);
        String year = dateAdjustment(yr);
        int mo = calendar.get(Calendar.MONTH) + 1;
        String month = dateAdjustment(mo);
        int dt = calendar.get(Calendar.DATE);
        String dates = dateAdjustment(dt);
        int hr = calendar.get(Calendar.HOUR_OF_DAY);
        String hours = dateAdjustment(hr);
        int mn = calendar.get(Calendar.MINUTE);
        String min = dateAdjustment(mn);
        int sc = calendar.get(Calendar.SECOND);
        String sec = dateAdjustment(sc);
        String date = (dates + "_" + month + "_" + year + "_" + hours + "_" + min + "_" + sec);
        return date;
    }

    public String returnScreenshotPath() {
        return (System.getProperty("user.dir") + "/"+ saveScreenshotsTo + screenshotName);
    }

    public void captureScreenshot(Scenario scenario) throws IOException {
        if (scenario.isFailed()) {
            File srcFile = ((TakesScreenshot) driverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            screenshotName = returnDateStamp() + ".png";
            FileUtils.copyFile(srcFile, new File(returnScreenshotPath()));
            scenario.write("<br>");
            scenario.write("Taking a screenshot for a failing step");
            scenario.write("<a target=\"_blank\", href=" + returnScreenshotPath() + ">" + screenshotName);
            scenario.write("<a target=\"_blank\", href=" + returnScreenshotPath() + "><img src=" + returnScreenshotPath() + " height=200 width=300></img></a>");
        }
    }

    public void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;

        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }catch (Exception e) {
            System.out.println("Unable to copy the report" + e.getMessage());

        } finally {
            is.close();
            os.close();
        }
    }

    public void copyLatestExtentReport() throws IOException {
        archivedReportName = "ArchivedOn_" + returnDateStamp() + ".html";
        File source = new File(System.getProperty("user.dir") +"/"+ latestReportPath);
        File dest = new File(System.getProperty("user.dir") +"/"+ saveArchiveReportsTo + archivedReportName);
        copyFileUsingStream(source, dest);

    }



}
