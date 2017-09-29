package uk.co.gyotools.selfmetrics.ft.stepdefs;

import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.gyotools.selfmetrics.ft.client.ApiClient;

import static org.assertj.core.api.Assertions.assertThat;

public class StatusEndpointStepDef extends AbstractStepDef {

    @Autowired
    private ApiClient apiClient;

    @When("^the status endpoint is called$")
    public void theStatusEndpointIsCalled() throws Throwable {
        apiClient.status();
    }

}
