package uk.co.gyotools.selfmetrics.ft.selfmetrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.config.RethinkDBConnectionFactory;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.dao.SelfMetricDao;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.dao.SelfMetricEntryDao;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.dao.rethinkdb.SelfMetricEntryRethingDBDao;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.dao.rethinkdb.SelfMetricRethinkDBDao;

import java.sql.SQLException;
import java.util.Collections;

import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
public class Application {

    @Bean
    @Scope("singleton")
    @Autowired
    public RethinkDBConnectionFactory rethinkDBConnectionFactory(Environment env) {
        return new RethinkDBConnectionFactory(env.getProperty("rethinkdb.host", "localhost"));
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
