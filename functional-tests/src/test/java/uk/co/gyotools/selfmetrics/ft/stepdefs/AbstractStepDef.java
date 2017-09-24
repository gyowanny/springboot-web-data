package uk.co.gyotools.selfmetrics.ft.stepdefs;

import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.co.gyotools.selfmetrics.ft.config.SpringFunctionalTestsConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringFunctionalTestsConfig.class, loader = SpringBootContextLoader.class)
public class AbstractStepDef {
}
