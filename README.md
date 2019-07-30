# Cucumber Extent Report
In the last section of Cucumber Reports  we got to know about the Plugins which are provided by Cucumber itself to generate
various kind of reports like HTML, JSON, XML, etc. Generally, those reports are enough to give us the overall execution results
with detailed time logs and other things. But there are many third party plugins also available which help to produce
reports with improved test logs capacity and better visual graphics.
## Extend Report
One of such plugins is 'Extent Report' by Anshoo Arora. This is currently considered one of the best reporting plugin on the market.
This report fits fine with any test framework you use. With Cucumber as well it works fine but it requires to
have some hacks to produce reports.
It would have been easy if Cucumber also had annotations like @beforeScenario and @beforeFeature.
These annotations are available in SpecFLow which is Cucumber in C#.
But there is an alternative for Cucumber called 'Cucumber Extent Report'.

## Cucumber Extent Report
This plugin is build on Extent Report specifically for Cucumber by Vimal Selvam. This is why it is named
**Cucumber Extent Report**. This one is actually made to ease the implementation of Extent Report in Cucumber Framework.
Let’s start with implementing the same in our Selenium Cucumber Framework.

## Step 1 : Add Cucumber Extent Report dependencies to the Project
The latest version of the extentreports-cucumber4-adapter jar is 1.0.7 which will be primarily used here.

### Cucumber version 4.0.0 to 4.2.0
The extentreports-cucumber4-adapter version 1.0.7 jar from maven repository works by default for the following Cucumber
versions 4.0.0, 4.0.1, 4.0.2, 4.1.0, 4.1.1, 4.2.0.

### Cucumber version 4.2.1 to 4.7.0
The extentreports-cucumber4-adapter version 1.0.7 jar from maven repository does not work by default for
Cucumber versions 4.2.1 - 4.7.0.

The reason is that the adapter requires the class URLOutputStream, which was moved to cucumber.runtime.formatter package
and access changed to package protected in version 4.2.1.
In the previous version it was a public class in cucumber.runtime.io package.
Though this class exists in the github source of extentreports-cucumber4-adapter, it has not been included in the jar.
Therefore, the extentreports-cucumber4-adapter jar needs to be modified for running with Cucumber version greater than 4.2.0

As we are using cucumber version 4.2.0, so extentreports-cucumber4-adapter will work for us by default, however, we need to be mindful
if/when upgrading cucumber to a higher version as it may break our reporting functionality if extentreports-cucumber4-adapter modification
is not addressed as a part of the upgrade process.

1) Add Extent Report Adapter to pom.xml
```
     <dependency>
         <groupId>com.aventstack</groupId>
         <artifactId>extentreports-cucumber4-adapter</artifactId>
         <version>1.0.7</version>
     </dependency>
```
2) Add Extent Report library
```
     <dependency>
         <groupId>com.aventstack</groupId>
         <artifactId>extentreports</artifactId>
         <version>4.0.9</version>
     </dependency>
```
## Step 2: Create Extent Report Configuration file
Extent Config is required by the Cucumber Extent Report plugin to read the report configuration.
It gives us the capability to set many useful setting to the report from the XML configuration file.
By default, it is read from the resources folder in scr/test so
we need to place our Extent Report Configuration file into this specific location.

1) Create a New File in src/test/ inside resources folder and name it 'extent-config.xml'
### extent-config.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<extentreports>
    <configuration>
        <!-- report theme -->
        <!-- standard, dark -->
        <theme>dark</theme>

        <!-- document encoding -->
        <!-- defaults to UTF-8 -->
        <encoding>UTF-8</encoding>

        <!-- protocol for script and stylesheets -->
        <!-- defaults to https -->
        <protocol>https</protocol>

        <!-- title of the document -->
        <documentTitle>Automation Reports</documentTitle>

        <!-- report name - displayed at top-nav -->
        <reportName>Automation Report for Cucumber Framework Tutorial</reportName>

        <!-- report headline - displayed at top-nav, after reportHeadline -->
        <reportHeadline>Behaviour Driven Framework</reportHeadline>

        <!-- global date format override -->
        <!-- defaults to yyyy-MM-dd -->
        <dateFormat>yyyy-MM-dd</dateFormat>

        <!-- global time format override -->
        <!-- defaults to HH:mm:ss -->
        <timeFormat>HH:mm:ss</timeFormat>

        <!-- custom javascript -->
        <scripts>
            <![CDATA[
                $(document).ready(function() {

                });
            ]]>
        </scripts>

        <!-- custom styles -->
        <styles>
            <![CDATA[

            ]]>
        </styles>
    </configuration>
</extentreports>
```
## Step 3: Report Activation
First step of activating the report generation is to place extent.properties file in the src/test/resources folder
to be picked up by the adapter.
1) Create a New File in src/test/ inside resources folder and name it 'extent.properties'
Note: We will only be activating HTML Report in this tutorial:
 - set flag to true for **extent.reporter.html.start** key
 - specify the location for the report file: html report needs to be mentioned as a value for the key
 **extent.reporter.html.config**
 - specify the location for the report path for **extent.reporter.html.out** key
### extent.properties
```
extent.reporter.avent.start=false
extent.reporter.bdd.start=false
extent.reporter.cards.start=false
extent.reporter.email.start=false
extent.reporter.html.start=true
extent.reporter.klov.start=false
extent.reporter.logger.start=false
extent.reporter.tabular.start=false

extent.reporter.avent.config=
extent.reporter.bdd.config=
extent.reporter.cards.config=
extent.reporter.email.config=
extent.reporter.html.config=src/test/resources/ReportsConfig.xml
extent.reporter.klov.config=
extent.reporter.logger.config=
extent.reporter.tabular.config=

extent.reporter.avent.out=
extent.reporter.bdd.out=
extent.reporter.cards.out=
extent.reporter.email.out=
extent.reporter.html.out=test-output/HtmlReport/ExtentHtml.html
extent.reporter.logger.out=
extent.reporter.tabular.out=

```
2) Add **com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:** plugin to @CucumberOptions
inside our Test Runner
### TestRunner.java
```
package runners;
import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue= {"stepDefinitions"},
        tags={"@wip"},
        plugin = { "pretty"
                , "html:target/cucumber-reports"
                , "json:target/cucumber-reports/Cucumber.json"
                , "junit:target/cucumber-reports/Cucumber.xml"
                , "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
)
public class TestRunner {
}
```
The above setup will generate the report in test-output/HtmlReport/ directory with the name of ExtentHtml.html.
Run Test Runner and check that the report has been generated.

# Cucumber Extent Report Features
As it was mentioned above, Cucumber Extent Report provides nice features to make  a report very useful. We will look at them one by one.

## Set System Information in the Report
This gives us a nice feature to set multiple System properties to the report, so that we know under which
system configurations our test suite was executed, when, and by whom.
In order to implement this, we do the following:
1) Create a new Class in src/main/java under utils package and name it 'ExtentReportBuilder'
2) Inside ExtentReportBuilder class we will make use of  WebDriver's getCapabilities()method. I provides information
regarding the current instance of a driver. We will use it for getting browser name and version. We also make use of
System.getProperty() method to get information about Platform and OC version alongside with the details of a user
who executed the test.
Extent Report Builder Class should look like this:
```
package utils;

import cucumber.TestContext;
import cucumber.api.Scenario;
import managers.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;


public class ExtentReportBuilder {

    TestContext testContext;
    WebDriverManager driverManager;

public ExtentReportBuilder (TestContext context) {
        testContext = context;
        driverManager = testContext.getWebDriverManager();
    }
    public void additionalReportInfo(Scenario scenario)  {
        Capabilities cap = ((RemoteWebDriver) driverManager.getDriver()).getCapabilities();
        String browserName = cap.getBrowserName().toUpperCase();
        String browserVersion = cap.getVersion();
        scenario.write("Executed by: " + System.getProperty("user.name"));
        scenario.write("Platform: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") v." + System.getProperty("os.version"));
        scenario.write("Browser: " + browserName + " v. " + browserVersion);
    }

}
```
3) Invoke additionalReportInfo() method from @After Hook like so:
```
package stepDefinitions;

import cucumber.TestContext;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import utils.ExtentReportBuilder;

public class Hooks {

    TestContext testContext;
    ExtentReportBuilder reportBuilder;

    public Hooks(TestContext context) {
        testContext = context;
        reportBuilder = new ExtentReportBuilder(context);
    }

    @Before
    public void BeforeSteps() {
        testContext.getWebDriverManager().getDriver();
    }

    @After
    public void AfterSteps(Scenario scenario) {
        try {
            reportBuilder.additionalReportInfo(scenario);
            testContext.getWebDriverManager().closeDriver();

        } catch (Exception e) {
            System.out.println("Methods failed: tearDownAndScreenShotOnFailure, Exception:" + e.getMessage());
        }
    }
}

```
Run Test Runner and check ExtentHtml.html by opening it in a browser. It should now have System Information in it inside 'Hooks.AfterStep' step;
```
Hooks.AfterSteps(Scenario)
Executed by: testowner
Platform: Mac OS X (x86_64) v.10.14.3
Browser: CHROME v. 75.0.3770.142
```
## Add Screenshot for a Failed Scenario
This feature gives us the capability to embed a screenshot in the report.
Below we will step by step capture a screenshot and attach it to the report, only if the scenario is Failed.
In case of Pass, no screenshot will be taken.
1) Add 'commons-io' library to our pom.xml file

The **Apache Commons-io** library contains utility classes, stream implementations, file filters and other features related to
file manipulations.
```
    <dependency>
         <groupId>commons-io</groupId>
         <artifactId>commons-io</artifactId>
         <version>2.6</version>
    </dependency>
```
 2) Add a new variable to **src/test/resources/extent.properties** file: a path where we save our screenshots:
 ```
 saveScreenshotsTo = test-output/Screenshots/
 ```

 3) Create a new empty folder inside test-output folder and name it 'Screenshots'


 4) Create a new config reader in src/main/java under 'dataProviders' package and name it
 'ExtentReportConfigReader'. It will read reports related data from extent.properties file

 ### ExtentReportConfigReader.java
 ```
 package dataProviders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ExtentReportConfigReader {
    private Properties properties;
    private final String propertyFilePath= "src/test/resources/extent.properties";

    public ExtentReportConfigReader(){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            properties = new Properties();
            try { properties.load(reader); }
            catch (IOException e) { e.printStackTrace(); }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Properties file not found at path : " + propertyFilePath);
        }finally {
            try { if(reader != null) reader.close(); }
            catch (IOException ignore) {}
        }
    }

    public String getSaveScreenShotsTo(){
        String saveScreenShotsTo = properties.getProperty("saveScreenshotsTo");
        if(saveScreenShotsTo!= null) return saveScreenShotsTo;
        else throw new RuntimeException("Screenshots path not specified in the Configuration.properties file for the Key: saveScreenshotsTo");
    }

}

```
5) Add ExtentReportConfigReader to FileReader Manager class
### FileReaderManager.java
```
package managers;

import dataProviders.ConfigFileReader;
import dataProviders.ExtentReportConfigReader;
import dataProviders.JsonDataReader;

public class FileReaderManager {

    private static FileReaderManager fileReaderManager = new FileReaderManager();
    private static ConfigFileReader configFileReader;
    private static JsonDataReader jsonDataReader;
    private static ExtentReportConfigReader extentReportConfigReader;

    private FileReaderManager() {
    }
    public static FileReaderManager getInstance( ) {
        return fileReaderManager;
    }
    public ConfigFileReader getConfigReader() {
        return (configFileReader == null) ? new ConfigFileReader() : configFileReader;
    }
    public JsonDataReader getJsonReader(){
        return (jsonDataReader == null) ? new JsonDataReader() : jsonDataReader;
    }
    public ExtentReportConfigReader getExtentReportConfigReader() {
        return (extentReportConfigReader == null) ? new ExtentReportConfigReader() : extentReportConfigReader;
    }
}

```
6) Add methods to ExtentReportBuilder Class which will capture a screenshot for a failing scenario

### ExtentReportBuilder.java
```
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
}

```
### Explanation
**returnDateStamp() :**
is a helper method aiding in constructing screenshot filename which is a date stamp
**returnScreenshotName() :**
Uses returnScreenshotPath() returns file path where screenshot should be saved
**captureScreenshot(Scenario scenario) :**
This method does all the main work - captures screenshot, saves it to a file, constructs file name and assign it to the file and place it into the report

7) Invoke **captureScreenshot()** method from cucumber @after hook

Note: We will give a more meaningful name to our AfterSteps() method. It will be called tearDownAndScreenShotOnFailure()

### Hooks.java
```
package stepDefinitions;

import cucumber.TestContext;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import utils.ExtentReportBuilder;

public class Hooks {

    TestContext testContext;
    ExtentReportBuilder reportBuilder;

    public Hooks(TestContext context) {
        testContext = context;
        reportBuilder = new ExtentReportBuilder(context);
    }

    @Before
    public void BeforeSteps() {
        testContext.getWebDriverManager().getDriver();
    }

    @After
    public void tearDownAndScreenShotOnFailure(Scenario scenario) {
        try {
            reportBuilder.additionalReportInfo(scenario);
            reportBuilder.captureScreenshot(scenario);
            testContext.getWebDriverManager().closeDriver();

        } catch (Exception e) {
            System.out.println("Methods failed: tearDownAndScreenShotOnFailure, Exception:" + e.getMessage());
        }
    }
}
```
Now we all set to test our screenshot taken feature. Let's make a change in your test code so we intentionally failing it.
(e.g. change one of the @FindBy locators to non-existing). Run the TestRunner and check that screenshot is saved at the specified location and is added to the report.

## Archiving previous Reports
When dealing with a real life project, business requirement might be to keep track of all previous test execution results.
In this case we need to find a way how we preserve a previous report before it is overwritten with a new report data.

To achieve the above, we will add a new variable into our extent.properties file to specify previous reports storage
location and write some methods which will be responsible for copying a previous report before its being updated after
new test is run. We will make use of our returnDateStamp() method again so we can capture the date,
previous report was archived. We will be following the below steps:

1) Add a new variable to **src/test/resources/extent.properties** file: a path where we save our previous reports:
 ```
 archiveReportsTo = test-output/ArchivedReports/
 ```

 2) Create a new empty folder inside test-output folder and name it 'ArchivedReports'


 3) Add two methods to our ExtentReportConfigReader Class which will read current report path from "extent.reporter.html.out" key and a path for previous report to be stored;
 ### ExtentReportConfigReader.java
 ```
 package dataProviders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ExtentReportConfigReader {
    private Properties properties;
    private final String propertyFilePath= "src/test/resources/extent.properties";

    public ExtentReportConfigReader(){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            properties = new Properties();
            try { properties.load(reader); }
            catch (IOException e) { e.printStackTrace(); }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Properties file not found at path : " + propertyFilePath);
        }finally {
            try { if(reader != null) reader.close(); }
            catch (IOException ignore) {}
        }
    }

    public String getSaveScreenShotsTo(){
        String saveScreenShotsTo = properties.getProperty("saveScreenshotsTo");
        if(saveScreenShotsTo!= null) return saveScreenShotsTo;
        else throw new RuntimeException("Screenshots path not specified in the Configuration.properties file for the Key: saveScreenshotsTo");
    }

    public String getSaveArchiveReportsTo(){
        String archiveReportsTo = properties.getProperty("archiveReportsTo");
        if(archiveReportsTo!= null) return archiveReportsTo;
        else throw new RuntimeException("Archived Reports path not specified in the Configuration.properties file for the Key: archiveReportsTo");
    }

    public String getCurrentReportPath(){
        String latestReportPath = properties.getProperty("extent.reporter.html.out");
        if(latestReportPath!= null) return latestReportPath;
        else throw new RuntimeException("Latest Reports path not specified in the Configuration.properties file for the Key: extent.reporter.html.out");
    }

}
```
4) Add two new methods to ExtentReportBuilder Class for renaming and copying previous report file:

```
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
        archivedReportName = "ArchivedOn_" + returnDateStamp(".html");
        File source = new File(System.getProperty("user.dir") +"/"+ latestReportPath);
        File dest = new File(System.getProperty("user.dir") +"/"+ saveArchiveReportsTo + archivedReportName);
        copyFileUsingStream(source, dest);
    }
```
### Explanation
**copyFileUsingStream() :**
This is a standard Java method for copying files
**copyLatestExtentReport() :**
This method specifies what file needs to be copied and where to. We will invoke this method from our
@after Hook in Hooks Class

5) Invoke **copyLatestExtentReport()** method from cucumber @after hook
### Hooks.java
```
package stepDefinitions;

import cucumber.TestContext;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import utils.ExtentReportBuilder;

public class Hooks {

    TestContext testContext;
    ExtentReportBuilder reportBuilder;

    public Hooks(TestContext context) {
        testContext = context;
        reportBuilder = new ExtentReportBuilder(context);
    }

    @Before
    public void BeforeSteps() {
        testContext.getWebDriverManager().getDriver();
    }

    @After
    public void tearDownAndScreenShotOnFailure(Scenario scenario) {
        try {
            reportBuilder.additionalReportInfo(scenario);
            reportBuilder.captureScreenshot(scenario);
            reportBuilder.copyLatestExtentReport();
            testContext.getWebDriverManager().closeDriver();

        } catch (Exception e) {
            System.out.println("Methods failed: tearDownAndScreenShotOnFailure, Exception:" + e.getMessage());
        }
    }
}
```
 Run the Test Runner and check that the previous report is now stored in ArchivedReport folder. Run test again and check that another
 previous report is added too.