package uk.co.gyotools.selfmetrics.ft.stepdefs;

import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.StepDefAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.gyotools.selfmetrics.ft.client.ApiClient;

import static org.assertj.core.api.Assertions.assertThat;
public class HttpCommonStepDef extends AbstractStepDef {

    @Autowired
    private ApiClient apiClient;

    @Then("^response is successful$")
    public void responseIsSuccessful() {
        assertThat(apiClient.getLastResponse().getStatusCode().is2xxSuccessful()).isTrue();

    }

    @Then("^response body contains '(.+)'$")
    public void responseBodyContainsText(String text) {
        assertThat(apiClient.getLastResponse().getBody()).isEqualTo(text);
    }
}
