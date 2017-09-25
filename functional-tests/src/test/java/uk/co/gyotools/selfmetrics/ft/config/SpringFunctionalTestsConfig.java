package uk.co.gyotools.selfmetrics.ft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@ComponentScan(basePackages = "uk.co.gyotools.selfmetrics.ft")
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

}
