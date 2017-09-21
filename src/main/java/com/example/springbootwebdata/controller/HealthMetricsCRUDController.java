package com.example.springbootwebdata.controller;

import com.example.springbootwebdata.model.HealthMetric;
import com.example.springbootwebdata.repository.HealthMetricsRepository;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/healthmetrics")
public class HealthMetricsCRUDController {

    private final HealthMetricsRepository healthMetricsRepository;

    @Autowired
    public HealthMetricsCRUDController(HealthMetricsRepository healthMetricsRepository) {
        this.healthMetricsRepository = healthMetricsRepository;
    }

    @RequestMapping(method = POST)
    public ResponseEntity<?> createMetric(@RequestBody HealthMetricPayload payload) {
        healthMetricsRepository.save(payload.toHealthMetric());
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path="/{id}", method = PUT)
    public ResponseEntity<?> updateMetric(@PathVariable("id") Long id, @RequestBody HealthMetricPayload payload) {
        if (!healthMetricsRepository.exists(id)) {
            return ResponseEntity.badRequest().build();
        }

        HealthMetric metric = payload.toHealthMetric();
        metric.setId(id);
        healthMetricsRepository.save(metric);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(path="/{id}", method = GET)
    public ResponseEntity<HealthMetric> getMetric(@PathVariable("id") Long id) {
        HealthMetric healthMetric = healthMetricsRepository.findOne(id);
        if (healthMetric == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<HealthMetric>(healthMetric, HttpStatus.OK);
    }

    private static class HealthMetricPayload {
        String name;
        String value;
        Date timestamp;

        @JsonCreator
        public HealthMetricPayload(
                @JsonProperty("name") String name,
                @JsonProperty("value") String value,
                @JsonProperty("timestamp") Date timestamp
        ) {
            this.name = name;
            this.value = value;
            this.timestamp = timestamp;
        }

        HealthMetric toHealthMetric() {
            HealthMetric metric = new HealthMetric();
            metric.setName(name);
            metric.setValue(value);
            metric.setTimestamp(timestamp);

            return metric;
        }
    }
}
