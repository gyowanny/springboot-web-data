package uk.co.gyotools.selfmetrics.ft.selfmetrics.model.payload;

import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.SelfMetric;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SelfMetricPayload {
    private String name;
    private String description;

    @JsonCreator
    public SelfMetricPayload(
            @JsonProperty("name") String name,
            @JsonProperty(value = "description", required = false) String description
    ) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public SelfMetric toHealthMetric() {
        SelfMetric healthMetric = new SelfMetric();
        healthMetric.setName(name);
        healthMetric.setDescription(description);
        return healthMetric;
    }
}
