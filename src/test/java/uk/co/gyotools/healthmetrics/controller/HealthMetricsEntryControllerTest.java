package uk.co.gyotools.healthmetrics.controller;

import uk.co.gyotools.healthmetrics.model.HealthMetric;
import uk.co.gyotools.healthmetrics.model.HealthMetricEntry;
import uk.co.gyotools.healthmetrics.model.payload.HealthMetricEntryPayload;
import uk.co.gyotools.healthmetrics.repository.HealthMetricsEntryRepository;
import uk.co.gyotools.healthmetrics.repository.HealthMetricsRepository;
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

import java.time.*;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
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
public class HealthMetricsEntryControllerTest {
    private static final String URI_PATH = "/healthmetrics/entry";
    private static final String PAYLOAD = "{\"metricId\":\"1\",\"value\":\"1\",\"timestamp\":\"2017-11-01T18:25:43.511Z\"}";

    private final ISO8601DateFormat dateFormat = new ISO8601DateFormat();

    @Mock
    private HealthMetricsEntryRepository healthMetricsEntryRepository;
    @Mock
    private HealthMetricsRepository healthMetricsRepository;

    private HealthMetricsEntryController instance;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;
    private Date now;
    private HealthMetricEntryPayload payload;
    private HealthMetricEntry metricEntry;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        instance = new HealthMetricsEntryController(healthMetricsRepository, healthMetricsEntryRepository);
        //MockMvc must be initialized manually when you inject mock dependencies otherwise
        //annotate the test class with @AutoConfigureMockMvc and the mockMvc field class with @Autowired
        mockMvc = MockMvcBuilders
                .standaloneSetup(instance)
                .build();
        now = new Date();
        payload = new HealthMetricEntryPayload(1L, "1", now);
        metricEntry = createHealthMetric(1L, "metricName", "1", now);
    }

    @Test
    public void postRequestShouldSaveAValidMetricAndReturn200() throws Exception {
        // Given
        HealthMetric healthMetric = new HealthMetric();
        healthMetric.setId(1L);
        healthMetric.setName("metricName");

        metricEntry.setId(null);

        when(healthMetricsRepository.findOne(anyLong())).thenReturn(healthMetric);
        when(healthMetricsEntryRepository.save(any(HealthMetricEntry.class))).thenReturn(metricEntry);

        // When
        mockMvc.perform(
                    post(URI_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().is2xxSuccessful());

        // Then
        verify(healthMetricsRepository).findOne(eq(healthMetric.getId()));
        verify(healthMetricsEntryRepository).save(eq(metricEntry));
    }

    @Test
    public void putRequestShouldUpdateAnExistingMetricAndReturn200() throws Exception {
        // Given
        when(healthMetricsEntryRepository.findOne(anyLong())).thenReturn(metricEntry);
        when(healthMetricsEntryRepository.save(any(HealthMetricEntry.class))).thenReturn(metricEntry);

        // When
        mockMvc.perform(
                put(URI_PATH + "/{id}", metricEntry.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().is2xxSuccessful());

        // Then
        verify(healthMetricsEntryRepository).findOne(eq(metricEntry.getId()));
        verify(healthMetricsEntryRepository).save(eq(metricEntry));
    }

    @Test
    public void putRequestShouldReturn400WhenMetricIdDoesNotExist() throws Exception {
        // Given
        when(healthMetricsEntryRepository.save(any(HealthMetricEntry.class))).thenReturn(metricEntry);
        when(healthMetricsEntryRepository.findOne(metricEntry.getId())).thenReturn(null);

        // When
        mockMvc.perform(
                put(URI_PATH + "/{id}", metricEntry.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD))
                .andExpect(status().isBadRequest());
        // Then
        verify(healthMetricsEntryRepository).findOne(eq(metricEntry.getId()));
        verify(healthMetricsEntryRepository, times(0)).save(eq(metricEntry));
    }

    @Test
    public void getRequestShouldReturnAnExistingHealthMetric() throws Exception {
        // Given
        when(healthMetricsEntryRepository.findOne(anyLong())).thenReturn(metricEntry);

        // When
        mockMvc.perform(
                get(URI_PATH + "/{id}", metricEntry.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(metricEntry)));

        // Then
        verify(healthMetricsEntryRepository).findOne(eq(metricEntry.getId()));
    }

    @Test
    public void getRequestShouldReturnNotFoundForInvalidId() throws Exception {
        // Given
        when(healthMetricsEntryRepository.findOne(anyLong())).thenReturn(null);

        // When
        mockMvc.perform(
                get(URI_PATH + "/{id}", metricEntry.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Then
        verify(healthMetricsEntryRepository).findOne(eq(metricEntry.getId()));
    }

    @Test
    public void getMetricsByNameAndDateIntervalShouldReturnAListOfMetrics() throws Exception {
        // Given
        String metricName = "metricName";
        List<HealthMetricEntry> metricsList = asList(
                createHealthMetric(1l, metricName, "1", new Date()),
                createHealthMetric(2l, metricName, "1", new Date()),
                createHealthMetric(3l, metricName, "1", new Date())
        );
        final LocalDateTime from = LocalDateTime.of(2017, Month.APRIL, 1, 1, 0, 0, 0);
        final LocalDateTime to = LocalDateTime.of(2017, Month.APRIL, 1, 3, 0, 0, 0);
        when(healthMetricsEntryRepository.findByNameAndTimestampBetween(any(), any(), any())).thenReturn(metricsList);

        String fromStr = dateFormat.format(Date.from(from.toInstant(ZoneOffset.UTC)));
        String toStr = dateFormat.format(Date.from(to.toInstant(ZoneOffset.UTC)));

        // When
        mockMvc.perform(get(URI_PATH + "/{name}/{from}/{to}", metricName, fromStr, toStr))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(metricsList)));

        // Then
        verify(healthMetricsEntryRepository).findByNameAndTimestampBetween(eq(metricName), eq(from), eq(to));
    }

    private HealthMetricEntry createHealthMetric(Long id, String name, String value, Date timestamp) {
        HealthMetricEntry metric = new HealthMetricEntry();
        metric.setId(id);
        metric.setName(name);
        metric.setValue(value);
        metric.setTimestamp(timestamp);
        return metric;
    }
}