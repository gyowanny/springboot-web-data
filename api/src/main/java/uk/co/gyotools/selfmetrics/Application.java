package uk.co.gyotools.selfmetrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import uk.co.gyotools.selfmetrics.config.RethinkDBConnectionFactory;
import uk.co.gyotools.selfmetrics.dao.SelfMetricDao;
import uk.co.gyotools.selfmetrics.dao.SelfMetricEntryDao;
import uk.co.gyotools.selfmetrics.dao.rethinkdb.SelfMetricEntryRethingDBDao;
import uk.co.gyotools.selfmetrics.dao.rethinkdb.SelfMetricRethinkDBDao;

import java.sql.SQLException;

@SpringBootApplication
public class Application {

    @Bean
    @Scope("singleton")
    @Autowired
    public RethinkDBConnectionFactory rethinkDBConnectionFactory(Environment env) {
        return new RethinkDBConnectionFactory(env.getProperty("databaseHost", "localhost"));
    }

    @Bean
    @Autowired
    public SelfMetricDao selfMetricDao(RethinkDBConnectionFactory connectionFactory, Environment env) {
        return new SelfMetricRethinkDBDao(connectionFactory, env);
    }

    @Bean
    @Autowired
    public SelfMetricEntryDao selfMetricEntryDao(RethinkDBConnectionFactory connectionFactory, Environment env) {
        return new SelfMetricEntryRethingDBDao(connectionFactory, env);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                //registry.addMapping("/healthmetrics").allowedOrigins("http://localhost:8080");
            }
        };
    }

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(Application.class, args);
	}
}
