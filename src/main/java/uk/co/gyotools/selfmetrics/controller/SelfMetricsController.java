package uk.co.gyotools.selfmetrics.controller;

import uk.co.gyotools.selfmetrics.model.SelfMetric;
import uk.co.gyotools.selfmetrics.model.payload.SelfMetricPayload;
import uk.co.gyotools.selfmetrics.repository.SelfMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path = "/healthmetrics")
public class SelfMetricsController {
    private final SelfMetricsRepository healthMetricsRepository;

    @Autowired
    public SelfMetricsController(SelfMetricsRepository healthMetricsRepository) {
        this.healthMetricsRepository = healthMetricsRepository;
    }

    @RequestMapping(method = POST, consumes = "application/json")
    public ResponseEntity<?> createHealthMetric(@RequestBody SelfMetricPayload payload) {
        if (healthMetricsRepository.existsByName(payload.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Metric name already exists");
        }

        SelfMetric healthMetric = payload.toHealthMetric();
        healthMetricsRepository.save(healthMetric);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(path="/{id}", method = PUT, consumes = "application/json")
    public ResponseEntity<?> updateHealthMetric(@PathVariable("id") Long id, @RequestBody SelfMetricPayload payload) {
        SelfMetric metric = healthMetricsRepository.findOne(id);
        if (metric == null) {
            return ResponseEntity.notFound().build();
        }

        metric.setName(payload.getName());
        metric.setDescription(payload.getDescription());

        healthMetricsRepository.save(metric);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(path="/{id}", method = GET, produces = "application/json")
    public ResponseEntity<SelfMetric> getHealthMetric(@PathVariable("id") Long id) {
        SelfMetric metric = healthMetricsRepository.findOne(id);
        if (metric == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<SelfMetric>(metric, HttpStatus.OK);
    }

    @RequestMapping(method = GET, produces = "application/json")
    public ResponseEntity<List<SelfMetric>> getAllHealthMetrics() {
        List<SelfMetric> metricList = healthMetricsRepository.findAll();
        if (metricList == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<List<SelfMetric>>(metricList, HttpStatus.OK);
    }
}
