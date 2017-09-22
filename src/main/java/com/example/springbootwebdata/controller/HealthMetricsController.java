package com.example.springbootwebdata.controller;

import com.example.springbootwebdata.model.HealthMetric;
import com.example.springbootwebdata.repository.HealthMetricsRepository;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/healthmetrics")
@Api(value="healthmetrics", description="Operations pertaining to health metrics")
public class HealthMetricsController {
    private static final ISO8601DateFormat ISO_8601_DATE_FORMAT = new ISO8601DateFormat();

    private final HealthMetricsRepository healthMetricsRepository;

    @Autowired
    public HealthMetricsController(HealthMetricsRepository healthMetricsRepository) {
        this.healthMetricsRepository = healthMetricsRepository;
    }

    @RequestMapping(method = POST)
    @ApiOperation(value = "Create a health metric in the database")
    public ResponseEntity<?> createMetric(@RequestBody HealthMetricPayload payload) {
        healthMetricsRepository.save(payload.toHealthMetric());
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path="/{id}", method = PUT)
    public ResponseEntity<?> updateMetric(
            @PathVariable("id") Long id,
            @RequestBody HealthMetricPayload payload
    ) {
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

    @RequestMapping(path="/{name}/{from}/{to}", method = GET)
    public ResponseEntity<List<HealthMetric>> getMetrics(
            @PathVariable("name") String name,
            @PathVariable("from") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime from,
            @PathVariable("to") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime to
    ) throws ParseException {
        List<HealthMetric> metricList = healthMetricsRepository
                .findByNameAndTimestampBetween(name, from, to);
        return new ResponseEntity<List<HealthMetric>>(
                metricList,
                HttpStatus.OK);
    }

    private static Date toDate(LocalDateTime from) {
        return Date.from(from.atZone(ZoneId.systemDefault()).toInstant());
    }

    private static LocalDateTime parseIsoLocalDateTime(String value) throws ParseException {
        return ZonedDateTime.parse(value).toLocalDateTime();
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
