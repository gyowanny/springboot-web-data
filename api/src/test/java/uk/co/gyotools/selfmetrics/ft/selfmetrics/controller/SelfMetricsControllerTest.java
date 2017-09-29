package uk.co.gyotools.selfmetrics.ft.selfmetrics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.AbstractUnitTest;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.SelfMetric;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.model.payload.SelfMetricPayload;
import uk.co.gyotools.selfmetrics.ft.selfmetrics.repository.SelfMetricsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class SelfMetricsControllerTest extends AbstractUnitTest {
    private static final String URI_PATH = "/selfmetrics";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private SelfMetricsRepository selfMetricsRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    public void postRequestShouldCreateANewHealthMetricFromAValidPayload() throws Exception {
        // Given
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
        SelfMetric metric = payload.toHealthMetric();

        selfMetricsRepository.save(metric);

        // When
        mockMvc.perform(post(URI_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Metric name already exists"));
    }

    @Test
    @Transactional
    public void putRequestShouldUpdateAnExistingHealthMetric() throws Exception {
        // Given
        SelfMetricPayload payload = new SelfMetricPayload("metricName", "description");
        SelfMetric metric = payload.toHealthMetric();

        metric = selfMetricsRepository.save(metric);
        final Long existingMetricId = metric.getId();

        final String newName = "New name";
        metric.setName(newName);

        payload = new SelfMetricPayload(metric);

        // When
        mockMvc.perform(put(URI_PATH + "/{id}", existingMetricId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().is2xxSuccessful());

        // Then
        SelfMetric actualSelfMetric = selfMetricsRepository.findOne(existingMetricId);
        assertNotNull(actualSelfMetric);
        assertThat(actualSelfMetric.getName()).isEqualTo(newName);
    }

    @Test
    @Transactional
    public void putRequestShouldReturn4xxErrorWhenHealthMetricDoesNotExist() throws Exception {
        // Given
        final Long existingMetricId = 1L;
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

        metric = selfMetricsRepository.save(metric);

        // When
        mockMvc.perform(get(URI_PATH + "/{id}", metric.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(metric)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @Transactional
    public void getRequestByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        // When
        mockMvc.perform(get(URI_PATH + "/{id}", 1L))
                .andExpect(status().is4xxClientError());

    }

    @Test
    @Transactional
    public void getRequestShouldReturnAllTheExistingHealthMetrics() throws Exception {
        // Given
        SelfMetric metric = new SelfMetricPayload("metricName", "description").toHealthMetric();
        metric = selfMetricsRepository.save(metric);

        // When
        mockMvc.perform(get(URI_PATH))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(asList(metric))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

    }
}