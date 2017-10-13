package uk.co.gyotools.selfmetrics.controller;

import uk.co.gyotools.selfmetrics.model.SelfMetric;
import uk.co.gyotools.selfmetrics.model.payload.SelfMetricPayload;
import uk.co.gyotools.selfmetrics.dao.SelfMetricDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(path = "/selfmetrics")
public class SelfMetricsController {
    private final SelfMetricDao healthMetricsRepository;

    @Autowired
    public SelfMetricsController(SelfMetricDao healthMetricsRepository) {
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
    public ResponseEntity<?> updateHealthMetric(@PathVariable("id") String id, @RequestBody SelfMetricPayload payload) {
        SelfMetric metric = healthMetricsRepository.findById(id);
        if (metric == null) {
            return ResponseEntity.notFound().build();
        }

        metric.setName(payload.getName());
        metric.setDescription(payload.getDescription());

        healthMetricsRepository.save(metric);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(path="/{id}", method = GET, produces = "application/json")
    public ResponseEntity<SelfMetric> getHealthMetric(@PathVariable("id") String id) {
        SelfMetric metric = healthMetricsRepository.findById(id);
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
