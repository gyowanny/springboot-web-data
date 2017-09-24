package uk.co.gyotools.selfmetrics.ft.selfmetrics.controller;

import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.SelfMetric;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.SelfMetricEntry;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.payload.SelfMetricEntryPayload;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.repository.SelfMetricsEntryRepository;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.repository.SelfMetricsRepository;
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
public class SelfMetricsEntryController {
    private final SelfMetricsRepository healthMetricsRepository;
    private final SelfMetricsEntryRepository healthMetricsEntryRepository;

    @Autowired
    public SelfMetricsEntryController(
            SelfMetricsRepository healthMetricsRepository,
            SelfMetricsEntryRepository healthMetricsEntryRepository
    ) {
        this.healthMetricsRepository = healthMetricsRepository;
        this.healthMetricsEntryRepository = healthMetricsEntryRepository;
    }

    @RequestMapping(method = POST)
    @ApiOperation(value = "Create a self metric in the database")
    public ResponseEntity<?> createMetric(@RequestBody SelfMetricEntryPayload payload) {
        SelfMetric healthMetric = healthMetricsRepository.findOne(payload.getMetricId());
        if (healthMetric == null) {
            return ResponseEntity.notFound().build();
        }
        healthMetricsEntryRepository.save(payload.toHealthMetric(healthMetric.getName()));
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path="/{id}", method = PUT)
    public ResponseEntity<?> updateMetric(
            @PathVariable("id") Long id,
            @RequestBody SelfMetricEntryPayload payload
    ) {
        SelfMetricEntry existingEntry = healthMetricsEntryRepository.findOne(id);
        if (existingEntry == null) {
            return ResponseEntity.badRequest().build();
        }

        SelfMetricEntry metric = payload.toHealthMetric(existingEntry.getName());
        metric.setId(id);
        healthMetricsEntryRepository.save(metric);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(path="/{id}", method = GET)
    public ResponseEntity<SelfMetricEntry> getMetric(@PathVariable("id") Long id) {
        SelfMetricEntry healthMetric = healthMetricsEntryRepository.findOne(id);
        if (healthMetric == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<SelfMetricEntry>(healthMetric, HttpStatus.OK);
    }

    @RequestMapping(path="/{name}/{from}/{to}", method = GET)
    public ResponseEntity<List<SelfMetricEntry>> getMetrics(
            @PathVariable("name") String name,
            @PathVariable("from") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime from,
            @PathVariable("to") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime to
    ) throws ParseException {
        List<SelfMetricEntry> metricList = healthMetricsEntryRepository
                .findByNameAndTimestampBetween(name, from, to);
        return new ResponseEntity<List<SelfMetricEntry>>(
                metricList,
                HttpStatus.OK);
    }

}
