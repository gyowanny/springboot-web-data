package com.example.springbootwebdata.repository;

import com.example.springbootwebdata.model.HealthMetric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface HealthMetricsRepository extends JpaRepository<HealthMetric,Long> {

    List<HealthMetric> findByNameAndTimestampBetween(String name, LocalDateTime from, LocalDateTime to);

}
