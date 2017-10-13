package uk.co.gyotools.selfmetrics.async.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ratpack.test.handling.HandlingResult;
import uk.co.gyotools.selfmetrics.async.dao.SelfMetricDao;
import uk.co.gyotools.selfmetrics.async.model.SelfMetric;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static ratpack.test.handling.RequestFixture.requestFixture;

@RunWith(MockitoJUnitRunner.class)
public class CreateSelfMetricHandlerTest {
    @Mock
    private SelfMetricDao selfMetricDao;

    private CreateSelfMetricHandler instance;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        instance = new CreateSelfMetricHandler(selfMetricDao);
    }

    @Test
    public void shouldSaveANewSelfMetricAndReturn201StatusCode() throws Exception {
        // Given
        when(selfMetricDao.save(any())).thenReturn("1");
        SelfMetric selfMetric = new SelfMetric();

        // When
        HandlingResult handlingResult = requestFixture()
                .method("POST")
                .body(mapper.writeValueAsString(selfMetric), "application/json")
                .handle(instance);

        // Then
        verify(selfMetricDao).save(any());
        assertThat(handlingResult.getStatus().getCode()).isEqualTo(201);
    }
}