package uk.co.gyotools.selfmetrics.ft.selfmetrics.controller;

import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.SelfMetric;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.payload.SelfMetricPayload;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.repository.SelfMetricsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SelfMetricsControllerTest {
    private static final String URI_PATH = "/healthmetrics";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private SelfMetricsRepository healthMetricsRepository;
    private MockMvc mockMvc;
    private SelfMetricsController instance;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        instance = new SelfMetricsController(healthMetricsRepository);
        mockMvc = MockMvcBuilders
                .standaloneSetup(instance)
                .build();
    }

    @Test
    public void postRequestShouldCreateANewHealthMetricFromAValidPayload() throws Exception {
        // Given
        SelfMetricPayload payload = new SelfMetricPayload("metricName", "description");
        SelfMetric metric = payload.toHealthMetric();
        when(healthMetricsRepository.save(any(SelfMetric.class))).thenReturn(metric);

        // When
        mockMvc.perform(post(URI_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(payload)))
        .andExpect(status().is2xxSuccessful());

        // Then
        verify(healthMetricsRepository).save(eq(metric));
    }

    @Test
    public void postRequestShouldReturn403WhenANameAlreadyInDBIsProvided() throws Exception {
        // Given
        SelfMetricPayload payload = new SelfMetricPayload("metricName", "description");
        SelfMetric metric = payload.toHealthMetric();
        when(healthMetricsRepository.existsByName(eq(metric.getName()))).thenReturn(true);

        // When
        mockMvc.perform(post(URI_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Metric name already exists"));

        // Then
        verify(healthMetricsRepository).existsByName(metric.getName());
        verify(healthMetricsRepository, times(0)).save(eq(metric));
    }

    @Test
    public void putRequestShouldUpdateAnExistingHealthMetric() throws Exception {
        // Given
        final Long existingMetricId = 1L;
        SelfMetricPayload payload = new SelfMetricPayload("metricName", "description");
        SelfMetric metric = payload.toHealthMetric();
        metric.setId(existingMetricId);
        when(healthMetricsRepository.findOne(existingMetricId)).thenReturn(metric);
        when(healthMetricsRepository.save(any(SelfMetric.class))).thenReturn(metric);

        // When
        mockMvc.perform(put(URI_PATH + "/{id}", existingMetricId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().is2xxSuccessful());

        // Then
        verify(healthMetricsRepository).findOne(eq(existingMetricId));
        verify(healthMetricsRepository).save(eq(metric));
    }

    @Test
    public void putRequestShouldReturn4xxErrorWhenHealthMetricDoesNotExist() throws Exception {
        // Given
        final Long existingMetricId = 1L;
        SelfMetricPayload payload = new SelfMetricPayload("metricName", "description");
        when(healthMetricsRepository.findOne(existingMetricId)).thenReturn(null);

        // When
        mockMvc.perform(put(URI_PATH + "/{id}", existingMetricId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().is4xxClientError());

        // Then
        verify(healthMetricsRepository).findOne(eq(existingMetricId));
        verify(healthMetricsRepository, times(0)).save(any(SelfMetric.class));
    }

    @Test
    public void getRequestByIdShouldReturnAHealthMetric() throws Exception {
        // Given
        final Long existingMetricId = 1L;
        SelfMetric metric = new SelfMetricPayload("metricName", "description").toHealthMetric();
        metric.setId(existingMetricId);

        when(healthMetricsRepository.findOne(eq(existingMetricId))).thenReturn(metric);

        // When
        mockMvc.perform(get(URI_PATH + "/{id}", existingMetricId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(metric)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        // Then
        verify(healthMetricsRepository).findOne(eq(existingMetricId));
    }

    @Test
    public void getRequestByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        // Given
        final Long existingMetricId = 1L;

        when(healthMetricsRepository.findOne(eq(existingMetricId))).thenReturn(null);

        // When
        mockMvc.perform(get(URI_PATH + "/{id}", existingMetricId))
                .andExpect(status().is4xxClientError());

        // Then
        verify(healthMetricsRepository).findOne(eq(existingMetricId));
    }

    @Test
    public void getRequestShouldReturnAllTheExistingHealthMetrics() throws Exception {
        // Given
        SelfMetric metric = new SelfMetricPayload("metricName", "description").toHealthMetric();
        metric.setId(1L);
        List<SelfMetric> metricList = asList(metric);

        when(healthMetricsRepository.findAll()).thenReturn(metricList);

        // When
        mockMvc.perform(get(URI_PATH))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(metricList)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        // Then
        verify(healthMetricsRepository).findAll();
    }
}