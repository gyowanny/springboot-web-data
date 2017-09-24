package uk.co.gyotools.selfmetrics.ft.selfmetrics.repository;

import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.SelfMetric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelfMetricsRepository extends JpaRepository<SelfMetric, Long> {
    Boolean existsByName(String name);
}
