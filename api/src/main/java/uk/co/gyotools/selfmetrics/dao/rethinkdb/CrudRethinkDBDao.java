package uk.co.gyotools.selfmetrics.dao.rethinkdb;

import com.rethinkdb.RethinkDB;
import uk.co.gyotools.selfmetrics.config.RethinkDBConnectionFactory;
import uk.co.gyotools.selfmetrics.dao.CrudDao;

import java.util.List;

public class CrudRethinkDBDao<T, ID> implements CrudDao<T, ID> {
    private final RethinkDBConnectionFactory connectionFactory;
    private final RethinkDB rethinkDB;
    private final Class<T> entityClass;
    private final String dbName;
    private final String table;

    protected CrudRethinkDBDao(
            RethinkDBConnectionFactory connectionFactory,
            RethinkDB rethinkDB,
            Class<T> entityClass,
            String dbName,
            String table
    ) {
        this.connectionFactory = connectionFactory;
        this.rethinkDB = rethinkDB;
        this.entityClass = entityClass;
        this.dbName = dbName;
        this.table = table;
    }

    @Override
    public ID save(T obj) {
        return rethinkDB.db(dbName).table(table).insert(obj).run(connectionFactory.createConnection());
    }

    @Override
    public void delete(ID id) {
        rethinkDB.db(dbName).table(table).deleteAt(id).run(connectionFactory.createConnection());
    }

    @Override
    public T findById(ID id) {
        return rethinkDB.db(dbName).table(table).get(id).run(connectionFactory.createConnection(), entityClass);
    }

    @Override
    public List<T> findAll() {
        return rethinkDB.db(dbName).table(table).run(connectionFactory.createConnection(), entityClass);
    }
}
