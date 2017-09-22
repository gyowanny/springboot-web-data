package com.example.springbootwebdata.repository;

import com.example.springbootwebdata.model.HealthMetric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthMetricsRepository extends JpaRepository<HealthMetric, Long>{
}
