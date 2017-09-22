package uk.co.gyotools.healthmetrics.controller;

import uk.co.gyotools.healthmetrics.model.HealthMetric;
import uk.co.gyotools.healthmetrics.model.HealthMetricEntry;
import uk.co.gyotools.healthmetrics.model.payload.HealthMetricEntryPayload;
import uk.co.gyotools.healthmetrics.repository.HealthMetricsEntryRepository;
import uk.co.gyotools.healthmetrics.repository.HealthMetricsRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/healthmetrics/entry")
@Api(value="healthmetrics", description="Operations pertaining to health metrics")
public class HealthMetricsEntryController {
    private final HealthMetricsRepository healthMetricsRepository;
    private final HealthMetricsEntryRepository healthMetricsEntryRepository;

    @Autowired
    public HealthMetricsEntryController(
            HealthMetricsRepository healthMetricsRepository,
            HealthMetricsEntryRepository healthMetricsEntryRepository
    ) {
        this.healthMetricsRepository = healthMetricsRepository;
        this.healthMetricsEntryRepository = healthMetricsEntryRepository;
    }

    @RequestMapping(method = POST)
    @ApiOperation(value = "Create a health metric in the database")
    public ResponseEntity<?> createMetric(@RequestBody HealthMetricEntryPayload payload) {
        HealthMetric healthMetric = healthMetricsRepository.findOne(payload.getMetricId());
        if (healthMetric == null) {
            return ResponseEntity.notFound().build();
        }
        healthMetricsEntryRepository.save(payload.toHealthMetric(healthMetric.getName()));
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path="/{id}", method = PUT)
    public ResponseEntity<?> updateMetric(
            @PathVariable("id") Long id,
            @RequestBody HealthMetricEntryPayload payload
    ) {
        HealthMetricEntry existingEntry = healthMetricsEntryRepository.findOne(id);
        if (existingEntry == null) {
            return ResponseEntity.badRequest().build();
        }

        HealthMetricEntry metric = payload.toHealthMetric(existingEntry.getName());
        metric.setId(id);
        healthMetricsEntryRepository.save(metric);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(path="/{id}", method = GET)
    public ResponseEntity<HealthMetricEntry> getMetric(@PathVariable("id") Long id) {
        HealthMetricEntry healthMetric = healthMetricsEntryRepository.findOne(id);
        if (healthMetric == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<HealthMetricEntry>(healthMetric, HttpStatus.OK);
    }

    @RequestMapping(path="/{name}/{from}/{to}", method = GET)
    public ResponseEntity<List<HealthMetricEntry>> getMetrics(
            @PathVariable("name") String name,
            @PathVariable("from") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime from,
            @PathVariable("to") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime to
    ) throws ParseException {
        List<HealthMetricEntry> metricList = healthMetricsEntryRepository
                .findByNameAndTimestampBetween(name, from, to);
        return new ResponseEntity<List<HealthMetricEntry>>(
                metricList,
                HttpStatus.OK);
    }

}
