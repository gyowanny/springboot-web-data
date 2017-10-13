package uk.co.gyotools.selfmetrics.async.dao.rethinkdb;

import com.rethinkdb.net.Connection;
import uk.co.gyotools.selfmetrics.async.dao.SelfMetricEntryDao;
import uk.co.gyotools.selfmetrics.async.model.SelfMetricEntry;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class SelfMetricEntryRethinkDBDao implements SelfMetricEntryDao {
    private final CrudRethinkDBDao<SelfMetricEntry, String> crudRethinkDBDao;
    private final Connection connection;

    @Inject
    public SelfMetricEntryRethinkDBDao(Connection connection) {
        this.connection = connection;
        this.crudRethinkDBDao = new CrudRethinkDBDao<SelfMetricEntry, String>(connection,
                SelfMetricEntry.class, "self_metric_entry");
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
