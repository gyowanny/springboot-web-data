package uk.co.gyotools.selfmetrics.async.dao.rethinkdb;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import uk.co.gyotools.selfmetrics.async.dao.CrudDao;

import java.util.List;

import static com.rethinkdb.RethinkDB.r;

public class CrudRethinkDBDao<T, ID> implements CrudDao<T, ID> {
    private final Connection connection;
    private final Class<T> entityClass;
    private final String table;

    protected CrudRethinkDBDao(
            Connection connection,
            Class<T> entityClass,
            String table
    ) {
        this.connection = connection;
        this.entityClass = entityClass;
        this.table = table;
    }

    @Override
    public ID save(T obj) {
        return r.table(table).insert(obj).run(connection);
    }

    @Override
    public void delete(ID id) {
        r.table(table).deleteAt(id).run(connection);
    }

    @Override
    public T findById(ID id) {
        return r.table(table).get(id).run(connection, entityClass);
    }

    @Override
    public List<T> findAll() {
        return r.table(table).run(connection, entityClass);
    }
}
