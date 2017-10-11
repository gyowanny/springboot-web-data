package uk.co.gyotools.selfmetrics.ft.selfmetrics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.AbstractUnitTest;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.SelfMetric;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.payload.SelfMetricPayload;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.dao.SelfMetricDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class SelfMetricsControllerTest extends AbstractUnitTest {
    private static final String URI_PATH = "/selfmetrics";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private SelfMetricDao selfMetricDao;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    public void postRequestShouldCreateANewHealthMetricFromAValidPayload() throws Exception {
        // Given
        when(selfMetricDao.save(any())).thenReturn("1");
        SelfMetricPayload payload = new SelfMetricPayload("metricName", "description");
        SelfMetric metric = payload.toHealthMetric();

        // When
        mockMvc.perform(post(URI_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(payload)))
        .andExpect(status().is2xxSuccessful());

    }

    @Test
    @Transactional
    public void postRequestShouldReturn403WhenANameAlreadyInDBIsProvided() throws Exception {
        // Given
        SelfMetricPayload payload = new SelfMetricPayload("metricName", "description");
        when(selfMetricDao.existsByName(any())).thenReturn(true);

        // When
        mockMvc.perform(post(URI_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Metric name already exists"));

        // Then
        verify(selfMetricDao).existsByName(any());
    }

    @Test
    @Transactional
    public void putRequestShouldUpdateAnExistingHealthMetric() throws Exception {
        // Given
        SelfMetricPayload payload = new SelfMetricPayload("metricName", "description");
        SelfMetric metric = payload.toHealthMetric();
        metric.setId("1");
        payload = new SelfMetricPayload(metric);
        when(selfMetricDao.findById(any())).thenReturn(metric);
        when(selfMetricDao.save(any())).thenReturn("1");

        // When
        mockMvc.perform(put(URI_PATH + "/{id}", metric.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().is2xxSuccessful());

        // Then
        verify(selfMetricDao).findById(any());
        verify(selfMetricDao).save(any());
    }

    @Test
    @Transactional
    public void putRequestShouldReturn4xxErrorWhenHealthMetricDoesNotExist() throws Exception {
        // Given
        final String existingMetricId = "1";
        SelfMetricPayload payload = new SelfMetricPayload("metricName", "description");

        // When
        mockMvc.perform(put(URI_PATH + "/{id}", existingMetricId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().is4xxClientError());

    }

    @Test
    @Transactional
    public void getRequestByIdShouldReturnAHealthMetric() throws Exception {
        // Given
        SelfMetricPayload payload = new SelfMetricPayload("metricName", "description");
        SelfMetric metric = payload.toHealthMetric();
        when(selfMetricDao.findById(any())).thenReturn(metric);

        // When
        mockMvc.perform(get(URI_PATH + "/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(metric)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        // Then
        verify(selfMetricDao).findById(eq("1"));
    }

    @Test
    @Transactional
    public void getRequestByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        // When
        mockMvc.perform(get(URI_PATH + "/{id}", "1"))
                .andExpect(status().is4xxClientError());

        // Then
        verify(selfMetricDao).findById(eq("1"));
    }

    @Test
    @Transactional
    public void getRequestShouldReturnAllTheExistingHealthMetrics() throws Exception {
        // Given
        SelfMetric metric = new SelfMetricPayload("metricName", "description").toHealthMetric();
        when(selfMetricDao.findAll()).thenReturn(asList(metric));

        // When
        mockMvc.perform(get(URI_PATH))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(asList(metric))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

    }
}