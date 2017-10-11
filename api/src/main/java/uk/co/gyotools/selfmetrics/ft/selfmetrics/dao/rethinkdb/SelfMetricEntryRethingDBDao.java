package uk.co.gyotools.selfmetrics.ft.selfmetrics.dao.rethinkdb;

import com.rethinkdb.RethinkDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.config.RethinkDBConnectionFactory;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.dao.SelfMetricEntryDao;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.SelfMetric;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.SelfMetricEntry;

import java.util.Date;
import java.util.List;

public class SelfMetricEntryRethingDBDao implements SelfMetricEntryDao {
    private final CrudRethinkDBDao<SelfMetricEntry, String> crudRethinkDBDao;
    private final RethinkDB rethinkDB = RethinkDB.r;

    @Autowired
    public SelfMetricEntryRethingDBDao(RethinkDBConnectionFactory connectionFactory, Environment env) {
        this.crudRethinkDBDao = new CrudRethinkDBDao<SelfMetricEntry, String>(connectionFactory, rethinkDB,
                SelfMetricEntry.class, env.getProperty("rethinkdb.name"), "self_metric_entry");
    }

    @Override
    public List<SelfMetricEntry> findByNameAndTimestampBetween(String name, Date from, Date to) {
        return null;
    }

    @Override
    public String save(SelfMetricEntry obj) {
        return crudRethinkDBDao.save(obj);
    }

    @Override
    public void delete(String id) {
        crudRethinkDBDao.delete(id);
    }

    @Override
    public SelfMetricEntry findById(String id) {
        return crudRethinkDBDao.findById(id);
    }

    @Override
    public List<SelfMetricEntry> findAll() {
        return crudRethinkDBDao.findAll();
    }
}
