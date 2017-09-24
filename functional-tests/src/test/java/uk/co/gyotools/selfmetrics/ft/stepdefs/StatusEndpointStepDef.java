package uk.co.gyotools.selfmetrics.ft.stepdefs;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.co.gyotools.selfmetrics.ft.client.StatusClient;

public class StatusEndpointStepDef extends AbstractStepDef {

    @Autowired
    private StatusClient statusClient;

    @When("^the status endpoint is called$")
    public void theStatusEndpointIsCalled() throws Throwable {
        statusClient.status();
    }

    @Then("^response is successful and body is 'OK'$")
    public void responseIsSuccessfulAndBodyIsOK() throws Throwable {

    }
}
