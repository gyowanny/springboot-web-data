package com.example.springbootwebdata.controller;

import com.example.springbootwebdata.model.HealthMetric;
import com.example.springbootwebdata.model.payload.HealthMetricPayload;
import com.example.springbootwebdata.repository.HealthMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static javafx.scene.input.KeyCode.R;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path = "/healthmetrics")
public class HealthMetricsController {
    private final HealthMetricsRepository healthMetricsRepository;

    @Autowired
    public HealthMetricsController(HealthMetricsRepository healthMetricsRepository) {
        this.healthMetricsRepository = healthMetricsRepository;
    }

    @RequestMapping(method = POST, consumes = "application/json")
    public ResponseEntity<?> createHealthMetric(@RequestBody HealthMetricPayload payload) {
        HealthMetric healthMetric = payload.toHealthMetric();
        healthMetricsRepository.save(healthMetric);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(path="/{id}", method = PUT, consumes = "application/json")
    public ResponseEntity<?> updateHealthMetric(@PathVariable("id") Long id, @RequestBody HealthMetricPayload payload) {
        HealthMetric metric = healthMetricsRepository.findOne(id);
        if (metric == null) {
            return ResponseEntity.notFound().build();
        }

        metric.setName(payload.getName());
        metric.setDescription(payload.getDescription());

        healthMetricsRepository.save(metric);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(path="/{id}", method = GET, produces = "application/json")
    public ResponseEntity<HealthMetric> getHealthMetric(@PathVariable("id") Long id) {
        HealthMetric metric = healthMetricsRepository.findOne(id);
        if (metric == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<HealthMetric>(metric, HttpStatus.OK);
    }

    @RequestMapping(method = GET, produces = "application/json")
    public ResponseEntity<List<HealthMetric>> getAllHealthMetrics() {
        List<HealthMetric> metricList = healthMetricsRepository.findAll();
        if (metricList == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<List<HealthMetric>>(metricList, HttpStatus.OK);
    }
}
