package com.example.springbootwebdata.controller;

import com.example.springbootwebdata.model.HealthMetric;
import com.example.springbootwebdata.repository.HealthMetricsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HealthMetricsControllerTest {
    private static final String PAYLOAD = "{\"name\":\"water.cup\",\"value\":\"1\",\"timestamp\":\"2017-11-01T18:25:43.511Z\"}";

    private final ISO8601DateFormat dateFormat = new ISO8601DateFormat();

    @Mock
    private HealthMetricsRepository healthMetricsRepository;

    private HealthMetricsController instance;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        instance = new HealthMetricsController(healthMetricsRepository);
        //MockMvc must be initialized manually when you inject mock dependencies otherwise
        //annotate the test class with @AutoConfigureMockMvc and the mockMvc field class with @Autowired
        mockMvc = MockMvcBuilders
                .standaloneSetup(instance)
                .build();
    }

    @Test
    public void postRequestShouldSaveAValidMetricAndReturn200() throws Exception {
        HealthMetric metric = objectMapper.readValue(PAYLOAD, HealthMetric.class);
        when(healthMetricsRepository.save(any(HealthMetric.class))).thenReturn(metric);

        mockMvc.perform(
                    post("/healthmetrics")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(PAYLOAD))
                .andExpect(status().is2xxSuccessful());

        verify(healthMetricsRepository).save(eq(metric));
    }

    @Test
    public void putRequestShouldUpdateAnExistingMetricAndReturn200() throws Exception {
        HealthMetric metric = objectMapper.readValue(PAYLOAD, HealthMetric.class);
        metric.setId(1l);
        when(healthMetricsRepository.save(any(HealthMetric.class))).thenReturn(metric);
        when(healthMetricsRepository.exists(1l)).thenReturn(true);

        mockMvc.perform(
                put("/healthmetrics/{id}","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD))
                .andExpect(status().is2xxSuccessful());

        verify(healthMetricsRepository).exists(eq(1l));
        verify(healthMetricsRepository).save(eq(metric));
    }

    @Test
    public void putRequestShouldReturn400WhenMetricIdDoesNotExist() throws Exception {
        HealthMetric metric = objectMapper.readValue(PAYLOAD, HealthMetric.class);
        metric.setId(1l);
        when(healthMetricsRepository.save(any(HealthMetric.class))).thenReturn(metric);
        when(healthMetricsRepository.exists(1l)).thenReturn(false);

        mockMvc.perform(
                put("/healthmetrics/{id}","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD))
                .andExpect(status().isBadRequest());

        verify(healthMetricsRepository).exists(eq(1l));
        verify(healthMetricsRepository, times(0)).save(eq(metric));
    }

    @Test
    public void getRequestShouldReturnAnExistingHealthMetric() throws Exception {
        HealthMetric metric = objectMapper.readValue(PAYLOAD, HealthMetric.class);
        metric.setId(1l);
        when(healthMetricsRepository.findOne(1l)).thenReturn(metric);

        mockMvc.perform(
                get("/healthmetrics/{id}","1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(metric)));

        verify(healthMetricsRepository).findOne(eq(1l));
    }

    @Test
    public void getRequestShouldReturnNotFoundForInvalidId() throws Exception {
        when(healthMetricsRepository.findOne(1l)).thenReturn(null);

        mockMvc.perform(
                get("/healthmetrics/{id}","1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(healthMetricsRepository).findOne(eq(1l));
    }

    @Test
    public void getMetricsByNameAndDateIntervalShouldReturnAListOfMetrics() throws Exception {
        String metricName = "water.cup";
        List<HealthMetric> metricsList = asList(
                createHealthMetric(1l, metricName, "1", Date.from(Instant.now())),
                createHealthMetric(2l, metricName, "1", Date.from(Instant.now().plus(1, ChronoUnit.HOURS))),
                createHealthMetric(3l, metricName, "1", Date.from(Instant.now().plus(2, ChronoUnit.HOURS)))
        );
        Date from = Date.from(Instant.now());
        Date to = Date.from(Instant.now().plus(3, ChronoUnit.HOURS));
        when(healthMetricsRepository.findByNameAndTimestampBetween(any(), any(), any())).thenReturn(metricsList);

        mockMvc.perform(get("/healthmetrics/{name}/{from}/{to}", metricName, dateFormat.format(from), dateFormat.format(to)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(metricsList)));

        verify(healthMetricsRepository).findByNameAndTimestampBetween(eq(metricName), any(), any());
    }

    private HealthMetric createHealthMetric(Long id, String name, String value, Date timestamp) {
        HealthMetric metric = new HealthMetric();
        metric.setId(id);
        metric.setName(name);
        metric.setValue(value);
        metric.setTimestamp(timestamp);
        return metric;
    }
}