package uk.co.gyotools.healthmetrics.repository;

import uk.co.gyotools.healthmetrics.model.HealthMetricEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HealthMetricsEntryRepository extends JpaRepository<HealthMetricEntry,Long> {

    List<HealthMetricEntry> findByNameAndTimestampBetween(String name, LocalDateTime from, LocalDateTime to);

}
