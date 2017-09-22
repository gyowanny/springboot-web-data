package uk.co.gyotools.healthmetrics.repository;

import uk.co.gyotools.healthmetrics.model.HealthMetric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthMetricsRepository extends JpaRepository<HealthMetric, Long> {
    Boolean existsByName(String name);
}
