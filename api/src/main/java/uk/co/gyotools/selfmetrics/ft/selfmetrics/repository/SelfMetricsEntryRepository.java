package uk.co.gyotools.selfmetrics.ft.selfmetrics.repository;

import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.SelfMetricEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SelfMetricsEntryRepository extends JpaRepository<SelfMetricEntry,Long> {

    List<SelfMetricEntry> findByNameAndTimestampBetween(String name, LocalDateTime from, LocalDateTime to);

}
