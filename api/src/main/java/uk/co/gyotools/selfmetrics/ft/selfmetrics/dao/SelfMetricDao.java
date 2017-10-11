package uk.co.gyotools.selfmetrics.ft.selfmetrics.dao;

import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.SelfMetric;

public interface SelfMetricDao extends CrudDao<SelfMetric, String> {
    Boolean existsByName(String name);
}
