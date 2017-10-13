package uk.co.gyotools.selfmetrics.async.dao.rethinkdb;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import uk.co.gyotools.selfmetrics.async.dao.SelfMetricDao;
import uk.co.gyotools.selfmetrics.async.model.SelfMetric;

import javax.inject.Inject;
import java.util.List;

public class SelfMetricRethinkDBDao implements SelfMetricDao {
    private final CrudRethinkDBDao<SelfMetric, String> crudRethinkDBDao;
    private final Connection connection;

    @Inject
    public SelfMetricRethinkDBDao(Connection connection) {
        this.connection = connection;
        this.crudRethinkDBDao = new CrudRethinkDBDao<>(connection, SelfMetric.class, "self_metric");
    }

    @Override
    public Boolean existsByName(String name) {
        return null;
    }

    @Override
    public String save(SelfMetric obj) {
        return crudRethinkDBDao.save(obj);
    }

    @Override
    public void delete(String id) {
        crudRethinkDBDao.delete(id);
    }

    @Override
    public SelfMetric findById(String id) {
        return crudRethinkDBDao.findById(id);
    }

    @Override
    public List<SelfMetric> findAll() {
        return crudRethinkDBDao.findAll();
    }
}
