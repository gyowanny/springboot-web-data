package uk.co.gyotools.healthmetrics.model.payload;

import uk.co.gyotools.healthmetrics.model.HealthMetricEntry;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class HealthMetricEntryPayload {
    private Long metricId;
    private String value;
    private Date timestamp;

    @JsonCreator
    public HealthMetricEntryPayload(
            @JsonProperty("metricId") Long metricId,
            @JsonProperty("value") String value,
            @JsonProperty("timestamp") Date timestamp
    ) {
        this.metricId = metricId;
        this.value = value;
        this.timestamp = timestamp;
    }

    public Long getMetricId() {
        return metricId;
    }

    public String getValue() {
        return value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public HealthMetricEntry toHealthMetric(String name) {
        HealthMetricEntry metric = new HealthMetricEntry();
        metric.setName(name);
        metric.setValue(value);
        metric.setTimestamp(timestamp);

        return metric;
    }
}
