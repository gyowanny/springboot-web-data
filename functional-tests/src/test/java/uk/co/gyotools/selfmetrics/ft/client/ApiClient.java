package uk.co.gyotools.selfmetrics.ft.client;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import uk.co.gyotools.selfmetrics.ft.model.SelfMetric;

import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpMethod.POST;

public class ApiClient extends HttpClient {

    public ApiClient(String apiUrl, RestTemplate restTemplate) {
        super(apiUrl, restTemplate);
    }

    public void status() {
        setLastResponse(getRestTemplate().getForEntity(getApiUrl() + "/private/status", String.class));
    }

    public void createSelfMetric(SelfMetric selfMetric) {
        setLastResponse(getRestTemplate()
                .postForEntity(getApiUrl() + "/healthmetrics", selfMetric, String.class));
    }
}
