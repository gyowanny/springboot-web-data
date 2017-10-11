package uk.co.gyotools.selfmetrics.ft.selfmetrics.controller;

import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.SelfMetric;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.SelfMetricEntry;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.payload.SelfMetricEntryPayload;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.dao.SelfMetricEntryDao;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.dao.SelfMetricDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/selfmetrics/entry")
@Api(value="healthmetrics", description="Operations pertaining to health metrics")
public class SelfMetricsEntryController {
    private final SelfMetricDao selfMetricDao;
    private final SelfMetricEntryDao selfMetricEntryDao;

    @Autowired
    public SelfMetricsEntryController(
            SelfMetricDao selfMetricDao,
            SelfMetricEntryDao selfMetricEntryDao
    ) {
        this.selfMetricDao = selfMetricDao;
        this.selfMetricEntryDao = selfMetricEntryDao;
    }

    @RequestMapping(method = POST)
    @ApiOperation(value = "Create a self metric in the database")
    public ResponseEntity<?> createMetric(@RequestBody SelfMetricEntryPayload payload) {
        SelfMetric healthMetric = selfMetricDao.findById(payload.getMetricId());
        if (healthMetric == null) {
            return ResponseEntity.notFound().build();
        }
        selfMetricEntryDao.save(payload.toHealthMetric(healthMetric.getName()));
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path="/{id}", method = PUT)
    public ResponseEntity<?> updateMetric(
            @PathVariable("id") String id,
            @RequestBody SelfMetricEntryPayload payload
    ) {
        SelfMetricEntry existingEntry = selfMetricEntryDao.findById(id);
        if (existingEntry == null) {
            return ResponseEntity.badRequest().build();
        }

        SelfMetricEntry metric = payload.toHealthMetric(existingEntry.getName());
        metric.setId(id);
        selfMetricEntryDao.save(metric);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(path="/{id}", method = GET)
    public ResponseEntity<SelfMetricEntry> getMetric(@PathVariable("id") String id) {
        SelfMetricEntry healthMetric = selfMetricEntryDao.findById(id);
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
        List<SelfMetricEntry> metricList = selfMetricEntryDao
                .findByNameAndTimestampBetween(name, toDate(from), toDate(to));
        return new ResponseEntity<List<SelfMetricEntry>>(
                metricList,
                HttpStatus.OK);
    }

    private static Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.toInstant(ZoneOffset.UTC));
    }

}
