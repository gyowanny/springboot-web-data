package uk.co.gyotools.selfmetrics.ft.selfmetrics.config;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;

import java.util.concurrent.TimeoutException;

public class RethinkDBConnectionFactory {
    private String host;

    public RethinkDBConnectionFactory(String host) {
        this.host = host;
    }

    public Connection createConnection() {
        try {
            return RethinkDB.r.connection().hostname(host).connect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
