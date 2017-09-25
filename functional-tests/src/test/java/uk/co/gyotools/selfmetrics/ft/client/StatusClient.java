package uk.co.gyotools.selfmetrics.ft.client;

import org.springframework.stereotype.Component;

@Component
public class StatusClient extends ApiHttpClient {

    public void status() {
        setLastResponse(getRestTemplate().getForEntity(getApiUrl() + "/private/status", String.class));
    }

}
