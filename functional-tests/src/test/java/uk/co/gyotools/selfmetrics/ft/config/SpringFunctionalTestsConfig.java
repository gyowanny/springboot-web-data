package uk.co.gyotools.selfmetrics.ft.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import uk.co.gyotools.selfmetrics.ft.client.ApiClient;

import javax.sql.DataSource;
import java.io.IOException;

@ComponentScan(basePackages = "uk.co.gyotools.selfmetrics.ft")
@SpringBootConfiguration
public class SpringFunctionalTestsConfig {

    @Bean
    RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        RestTemplate template = new RestTemplate(requestFactory);
        // Rest template should not throw exceptions on server errors, but return the entity to us
        template.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            protected boolean hasError(HttpStatus statusCode) {
                return false;
            }
        });
        return template;
    }

    @Bean
    @Autowired
    ApiClient apiClient(@Value("${apiUrl}") String apiUrl, RestTemplate restTemplate) throws IOException {
        return new ApiClient(apiUrl, restTemplate);
    }

    @Bean
    @Autowired
    public DataSource dataSource(Environment env) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("databaseDriverClassName"));
        dataSource.setUrl(env.getRequiredProperty("databaseUrl"));
        dataSource.setUsername(env.getRequiredProperty("databaseUser"));
        dataSource.setPassword(env.getRequiredProperty("databasePassword"));
        System.out.println("##### Configured Datasource: "+dataSource.getUrl());
        return dataSource;
    }

    @Bean
    @Autowired
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        return jdbcTemplate;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
