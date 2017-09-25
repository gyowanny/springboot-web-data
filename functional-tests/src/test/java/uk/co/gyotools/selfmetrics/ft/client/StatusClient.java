package uk.co.gyotools.selfmetrics.ft.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StatusClient {
    @Value("${apiUrl}")
    private String apiUrl;
    @Autowired
    private RestTemplate restTemplate;
    private ResponseEntity<?> lastResponse;

    public void status() {
        lastResponse = restTemplate.getForEntity(apiUrl + "/private/status", String.class);
    }

    public ResponseEntity<?> getLastResponse() {
        return lastResponse;
    }
}
