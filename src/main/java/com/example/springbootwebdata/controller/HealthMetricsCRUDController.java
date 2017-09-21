package com.example.springbootwebdata.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/healthmetrics")
public class HealthMetricsCRUDController {

    @RequestMapping(method = POST)
    public ResponseEntity<?> postMetric(@RequestBody MetricPayload payload) {
        return ResponseEntity.ok().build();
    }

    private static class MetricPayload {
        String name;
        String value;
        Date timestamp;

        @JsonCreator
        public MetricPayload(
                @JsonProperty("name") String name,
                @JsonProperty("value") String value,
                @JsonProperty("timestamp") Date timestamp
        ) {
            this.name = name;
            this.value = value;
            this.timestamp = timestamp;
        }
    }
}
