package com.example.springbootwebdata.controller;

import com.example.springbootwebdata.model.HealthMetric;
import com.example.springbootwebdata.model.payload.HealthMetricPayload;
import com.example.springbootwebdata.repository.HealthMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/healthmetrics")
public class HealthMetricsController {

    private final HealthMetricsRepository healthMetricsRepository;

    @Autowired
    public HealthMetricsController(HealthMetricsRepository healthMetricsRepository) {
        this.healthMetricsRepository = healthMetricsRepository;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> createHealthMetric(@RequestBody HealthMetricPayload payload) {
        HealthMetric healthMetric = payload.toHealthMetric();
        healthMetricsRepository.save(healthMetric);

        return ResponseEntity.ok().build();
    }
}
