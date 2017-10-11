package uk.co.gyotools.selfmetrics.ft.selfmetrics.dao.rethinkdb;

import com.rethinkdb.RethinkDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.config.RethinkDBConnectionFactory;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.dao.SelfMetricDao;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.SelfMetric;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.SelfMetricEntry;

import java.util.List;

public class SelfMetricRethinkDBDao implements SelfMetricDao {
    private final CrudRethinkDBDao<SelfMetric, String> crudRethinkDBDao;
    private final RethinkDB rethinkDB = RethinkDB.r;

    @Autowired
    public SelfMetricRethinkDBDao(RethinkDBConnectionFactory connectionFactory, Environment env) {
        this.crudRethinkDBDao = new CrudRethinkDBDao<SelfMetric, String>(connectionFactory, rethinkDB,
                SelfMetric.class, env.getProperty("rethinkdb.name"), "self_metric");
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
