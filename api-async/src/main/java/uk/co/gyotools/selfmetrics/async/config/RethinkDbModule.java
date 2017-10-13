package uk.co.gyotools.selfmetrics.async.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.gyotools.selfmetrics.async.dao.SelfMetricDao;
import uk.co.gyotools.selfmetrics.async.dao.SelfMetricEntryDao;
import uk.co.gyotools.selfmetrics.async.dao.rethinkdb.SelfMetricEntryRethinkDBDao;
import uk.co.gyotools.selfmetrics.async.dao.rethinkdb.SelfMetricRethinkDBDao;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static com.rethinkdb.RethinkDB.r;
import static java.util.Arrays.asList;

public class RethinkDbModule extends AbstractModule {
    private static final Logger LOGGER = LoggerFactory.getLogger(RethinkDbModule.class);
    private static final String DB_NAME = "self_metrics";
    private static final List<String> TABLES = asList("self_metric", "self_metric_entry");

    @Override
    protected void configure() {
        bind(CreateDatabase.class).asEagerSingleton();
        bind(SelfMetricDao.class).to(SelfMetricRethinkDBDao.class);
        bind(SelfMetricEntryDao.class).to(SelfMetricEntryRethinkDBDao.class);
    }

    @Provides
    private Connection provideConnection(
            @Named("databaseHost") String dbHost,
            @Named("databasePort") String dbPort) {
        LOGGER.info("Connecting to database at "+dbHost+":"+dbPort);

        return RethinkDB.r.connection()
                .db(DB_NAME)
                .hostname(dbHost)
                .port(Integer.valueOf(dbPort))
                .connect();
    }


    private static class CreateDatabase {
        private String dbHost;
        private String dbPort;

        @Inject
        public CreateDatabase(@Named("databaseHost") String dbHost, @Named("databasePort") String dbPort) {
            this.dbHost = dbHost;
            this.dbPort = dbPort;
        }

        @Inject
        public void create() {
            LOGGER.info("Creating the database and tables...");

            Connection connection = r.connection().hostname(dbHost).port(Integer.valueOf(dbPort)).connect();

            List<String> dbList = r.dbList().run(connection);
            if (!dbList.contains(DB_NAME)) {
                r.dbCreate(DB_NAME).run(connection);
            }

            List<String> tableList = r.db(DB_NAME).tableList().run(connection);

            TABLES.forEach(table -> createTableIfNotExists(connection, tableList, table));

            connection.close();

            LOGGER.info("Tables created.");
        }

        private static void createTableIfNotExists(Connection connection, List<String> tableList, String tableName) {
            if (!tableList.contains(tableName)) {
                r.db(DB_NAME).tableCreate(tableName).run(connection);
            }
        }
    }
}
