package uk.co.gyotools.selfmetrics.dao;

import uk.co.gyotools.selfmetrics.model.SelfMetric;

public interface SelfMetricDao extends CrudDao<SelfMetric, String> {
    Boolean existsByName(String name);
}
