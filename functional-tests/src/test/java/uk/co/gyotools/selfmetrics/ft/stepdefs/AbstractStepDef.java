package uk.co.gyotools.selfmetrics.ft.stepdefs;

import cucumber.api.java.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import uk.co.gyotools.selfmetrics.ft.config.SpringFunctionalTestsConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringFunctionalTestsConfig.class, loader = SpringBootContextLoader.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
public class AbstractStepDef {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void cleanupDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE HEALTH_METRIC");
        jdbcTemplate.execute("TRUNCATE TABLE HEALTH_METRIC_ENTRY");
    }
}
