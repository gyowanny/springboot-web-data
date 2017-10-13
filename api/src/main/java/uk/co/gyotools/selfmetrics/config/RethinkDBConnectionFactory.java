package uk.co.gyotools.selfmetrics.config;

import com.rethinkdb.net.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.rethinkdb.RethinkDB.r;

public class RethinkDBConnectionFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(RethinkDBConnectionFactory.class);
    private static final Object DB_NAME = "self_metrics";
    private static final String DB_TABLE_SELF_METRIC = "self_metric";
    private static final String DB_TABLE_SELF_METRIC_ENTRY = "self_metric_entry";
    private final String host;

    public RethinkDBConnectionFactory(String host) {
        this.host = host;
    }

    public Connection createConnection() {
        try {
            LOGGER.debug("Retrieving database connection at host " + host);
            return r.connection().hostname(host).connect();
        } catch (Exception e) {
            LOGGER.error("Error retrieving database connection at host " + host,e);
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void createDatabase() {
        LOGGER.info("Creating the database and tables");

        Connection connection = createConnection();

        List<String> dbList = r.dbList().run(connection);
        if (!dbList.contains(DB_NAME)) {
            r.dbCreate(DB_NAME).run(connection);
        }

        List<String> tableList = r.db(DB_NAME).tableList().run(connection);

        createTableIfNotExists(connection, tableList, DB_TABLE_SELF_METRIC);
        createTableIfNotExists(connection, tableList, DB_TABLE_SELF_METRIC_ENTRY);

        connection.close();
    }

    private void createTableIfNotExists(Connection connection, List<String> tableList, String tableName) {
        if (!tableList.contains(tableName)) {
            r.db(DB_NAME).tableCreate(tableName).run(connection);
        }
    }
}
