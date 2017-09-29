package uk.co.gyotools.selfmetrics.ft.stepdefs;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.gyotools.selfmetrics.ft.client.ApiClient;
import uk.co.gyotools.selfmetrics.ft.model.SelfMetric;
import uk.co.gyotools.selfmetrics.ft.state.SelfMetricState;

public class SelfMetricsStepDef extends AbstractStepDef {
    @Autowired
    private SelfMetricState selfMetricState;
    @Autowired
    private ApiClient apiClient;

    @Given("^a self metric object$")
    public void aSelfMetricObject() throws Throwable {
        selfMetricState.setSelfMetric(new SelfMetric("metric", "metricdesc"));
    }

    @When("^the client '(.+)'$")
    public void theClientPostASelfMetric(String clientAction) throws Throwable {
        switch (clientAction){
            case "post a self metric": apiClient.createSelfMetric(selfMetricState.getSelfMetric()); break;
            default: throw new IllegalArgumentException("Invalid client action: " + clientAction);
        }
    }
}
