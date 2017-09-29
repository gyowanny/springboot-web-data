package uk.co.gyotools.selfmetrics.ft.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class HttpClient {
    private final String apiUrl;
    private final RestTemplate restTemplate;
    private ResponseEntity<?> lastResponse;

    public HttpClient(String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
    }

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
