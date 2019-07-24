# Cucumber Reports
When ever we do test execution, it is also require to understand the out put of the execution.
## Cucumber Reports
When we executing Cucumber Scenarios, it automatically generates an output in the IDE console.
There is a default behavior associated with that output and we can also configure that output as per our needs.
So how do we modify the default behavior, let’s see this now.
### Pretty Report
The first plugin, we will talk about is **Pretty**.  It provides more verbose output.
To implement this, just specify plugin = “pretty” in **CucumberOptions**.
```
@CucumberOptions( plugin = { “pretty” } )
```
### Monochrome Mode Reporting
If the monochrome option is set to false, then the console output is not as readable as it should be.
The output when the monochrome option is set to false is shown in the above example.
It is just because, if the monochrome is not defined in Cucumber Options, it takes it as false by default.
How to specify it:
```
@CucumberOptions( monochrome = true );
```
CucumberOption code should look like this:
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
        plugin = { "pretty" },
        monochrome = true
)
public class TestRunner {
}
```

Console outpul will now look like this:
```
Feature: Automated End2End Tests
  Description: The purpose of this feature is to test End 2 End integration.

  @wip
  Scenario Outline: Customer place an order by purchasing an item from search - customer details are taken from JSON file # src/test/resources/features/End2End_Test.feature:17
    And I enter <customer> personal details
    And I place the order
    Then Order details are successfully verified

    Examples:

  Background:                                   # src/test/resources/features/End2End_Test.feature:4
    Given I am on Home Page                     # HomePageSteps.i_am_on_Home_Page()
    When I search for product in dress category # HomePageSteps.i_search_for_product_in_dress_category()
    And I choose to buy the first item          # ProductPageSteps.i_choose_to_buy_the_first_item()
    And I move to checkout from mini cart       # CartPageSteps.i_move_to_checkout_from_mini_cart()

```
### Usage Report
If we are more concerned about the time taken by each Step Definition, then we should use the usage plugin.
This is how we specify the same in @CucumberOptions:
```
@CucumberOptions( plugin = { “usage” })
```
Console output will now look like this:
```
[
  {
    "source": "I choose to buy the first item",
    "steps": [
      {
        "name": "I choose to buy the first item",
        "aggregatedDurations": {
          "average": 5.699256593,
          "median": 5.699256593
        },
        "durations": [
          {
            "duration": 7.602074066,
            "location": "src/test/resources/features/End2End_Test.feature:7"
          },
          {
            "duration": 3.79643912,
            "location": "src/test/resources/features/End2End_Test.feature:7"
          }
        ]
      }
    ]
  },
  {
    "source": "I place the order",
    "steps": [
      {
        "name": "I place the order",
        "aggregatedDurations": {
          "average": 0.392438454,
          "median": 0.392438454
        },
        "durations": [
          {
            "duration": 0.376548105,
            "location": "src/test/resources/features/End2End_Test.feature:19"
          },
          {
            "duration": 0.408328804,
            "location": "src/test/resources/features/End2End_Test.feature:19"
          }
        ]
      }
    ]
  } ...
  ```
## Cucumber Report Output
So far we have seen above is actually good for a test or for couple of tests. But if we run a full test suite,
this report is not much useful in that case. On top of that it is difficult to keep these console output safe for future use.

Cucumber gives us capability to generate reports as well in the form of HTML, XML, JSON & TXT.
Cucumber frameworks generate very good and detailed reports, which can be shared with all stake holders.
There are multiple options available for reports which can be used depending on the requirement.

### Cucumber HTML Reports
For HTML reports, add **html:target/cucumber-reports**  to the @CucumberOptions plugin option.
```
@CucumberOptions(
        features = "src/test/resources/features",
        glue= {"stepDefinitions"},
        tags={"@wip"},
        plugin = { "pretty", "html:target/cucumber-reports"  }

)
```
Note: We have specified the path of the Cucumber report, which we want it to generate it under target folder.

Run Test Runner and check HTML report output in target/cucumber-reports folder

### Cucumber JSON Report
For JSON reports, add **json:target/cucumber-reports/Cucumber.json**  to the @CucumberOptions plugin option.
```
@CucumberOptions(
        features = "src/test/resources/features",
        glue= {"stepDefinitions"},
        tags={"@wip"},
        plugin = { "pretty"
                , "html:target/cucumber-reports"
                , "json:target/cucumber-reports/Cucumber.json"}
)
```
Note : This report contains all the information from the gherkin source in the JSON format.
This report is meant to be post processed into another visual format by third-party tools.
JSON report output will look like this:
```
[
  {
    "line": 1,
    "elements": [
      {
        "line": 4,
        "name": "",
        "description": "",
        "type": "background",
        "keyword": "Background",
        "steps": [
          {
            "result": {
              "duration": 4848418704,
              "status": "passed"
            },
            "line": 5,
            "name": "I am on Home Page",
            "match": {
              "location": "HomePageSteps.i_am_on_Home_Page()"
            },
            "keyword": "Given "
          },
          {
            "result": {
              "duration": 3078654424,
              "status": "passed"
            }...
  ```
### Cucumber JUNIT XML Report
For JUNIT reports, add **junit:target/cucumber-reports/Cucumber.xml** to the @CucumberOptions plugin option.
```
@CucumberOptions(
        features = "src/test/resources/features",
        glue= {"stepDefinitions"},
        tags={"@wip"},
        plugin = { "pretty"
                , "html:target/cucumber-reports"
                , "json:target/cucumber-reports/Cucumber.json"
                , "junit:target/cucumber-reports/Cucumber.xml"}
)
```
Note : This report generates XML files just like Apache Ant’s junit report task.
This XML format is understood by most continuous integration servers, who will use it to generate visual reports.
XML Report Output:
```
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<testsuite failures="0" name="cucumber.runtime.formatter.JUnitFormatter" skipped="0" tests="2" time="57.044076">
<testcase classname="Automated End2End Tests" name="Customer place an order by purchasing an item from search - customer details are taken from JSON file" time="28.534119">
<system-out><![CDATA[Given I am on Home Page.....................................................passed
When I search for product in dress category.................................passed
And I choose to buy the first item..........................................passed
And I move to checkout from mini cart.......................................passed
And I enter Opencast personal details.......................................passed
And I place the order.......................................................passed
Then Order details are successfully verified................................passed
]]></system-out>
</testcase>
<testcase classname="Automated End2End Tests" name="Customer place an order by purchasing an item from search - customer details are taken from JSON file 2" time="28.509957">
<system-out><![CDATA[Given I am on Home Page.....................................................passed
When I search for product in dress category.................................passed
And I choose to buy the first item..........................................passed
And I move to checkout from mini cart.......................................passed
And I enter Testuser personal details.......................................passed
And I place the order.......................................................passed
Then Order details are successfully verified................................passed
]]></system-out>
</testcase>
</testsuite>
```


