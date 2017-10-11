package uk.co.gyotools.selfmetrics.ft.selfmetrics.model.payload;

import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.SelfMetricEntry;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class SelfMetricEntryPayload {
    private String metricId;
    private String value;
    private Date timestamp;

    @JsonCreator
    public SelfMetricEntryPayload(
            @JsonProperty("metricId") String metricId,
            @JsonProperty("value") String value,
            @JsonProperty("timestamp") Date timestamp
    ) {
        this.metricId = metricId;
        this.value = value;
        this.timestamp = timestamp;
    }

    public SelfMetricEntryPayload(String metricId, SelfMetricEntry metricEntry) {
        this.metricId = metricId;
        this.value = metricEntry.getValue();
        this.timestamp = metricEntry.getTimestamp();
    }

    public String getMetricId() {
        return metricId;
    }

    public String getValue() {
        return value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public SelfMetricEntry toHealthMetric(String name) {
        SelfMetricEntry metric = new SelfMetricEntry();
        metric.setName(name);
        metric.setValue(value);
        metric.setTimestamp(timestamp);

        return metric;
    }
}
