package uk.co.gyotools.selfmetrics.ft;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"cucumber.api.spring", "uk.co.gyotools.selfmetrics.ft.stepdefs"},
        features = "classpath:features",
        format = {"pretty", "html:target/cucumber-html-report", "json:target/cucumber-json-report.json"}
)
public class RunCukesTest {
}
