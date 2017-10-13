package uk.co.gyotools.selfmetrics.async.dao;


import uk.co.gyotools.selfmetrics.async.model.SelfMetric;

public interface SelfMetricDao extends CrudDao<SelfMetric, String> {
    Boolean existsByName(String name);
}
