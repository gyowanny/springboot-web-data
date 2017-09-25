package uk.co.gyotools.selfmetrics.ft.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class ApiHttpClient {
    @Value("${apiUrl}")
    private String apiUrl;
    @Autowired
    private RestTemplate restTemplate;
    private ResponseEntity<?> lastResponse;

    public ResponseEntity<?> getLastResponse() {
        return lastResponse;
    }

    protected void setLastResponse(ResponseEntity<?> lastResponse) {
        this.lastResponse = lastResponse;
    }

    protected String getApiUrl() {
        return apiUrl;
    }

    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
