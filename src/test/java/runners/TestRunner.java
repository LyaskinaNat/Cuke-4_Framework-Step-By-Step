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
