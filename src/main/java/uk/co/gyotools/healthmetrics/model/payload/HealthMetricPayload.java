package uk.co.gyotools.healthmetrics.model.payload;

import uk.co.gyotools.healthmetrics.model.HealthMetric;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HealthMetricPayload {
    private String name;
    private String description;

    @JsonCreator
    public HealthMetricPayload(
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

    public HealthMetric toHealthMetric() {
        HealthMetric healthMetric = new HealthMetric();
        healthMetric.setName(name);
        healthMetric.setDescription(description);
        return healthMetric;
    }
}
