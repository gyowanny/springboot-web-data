package com.example.springbootwebdata.controller;

import com.example.springbootwebdata.model.HealthMetric;
import com.example.springbootwebdata.model.payload.HealthMetricPayload;
import com.example.springbootwebdata.repository.HealthMetricsRepository;
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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HealthMetricsControllerTest {
    private static final String URI_PATH = "/healthmetrics";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private HealthMetricsRepository healthMetricsRepository;
    private MockMvc mockMvc;
    private HealthMetricsController instance;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        instance = new HealthMetricsController(healthMetricsRepository);
        mockMvc = MockMvcBuilders
                .standaloneSetup(instance)
                .build();
    }

    @Test
    public void postRequestShouldCreateANewHealthMetricFromAValidPayload() throws Exception {
        // Given
        HealthMetricPayload payload = new HealthMetricPayload("metricName", "description");
        HealthMetric metric = payload.toHealthMetric();
        when(healthMetricsRepository.save(any(HealthMetric.class))).thenReturn(metric);

        // When
        mockMvc.perform(post(URI_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(payload)))
        .andExpect(status().is2xxSuccessful());

        // Then
        verify(healthMetricsRepository).save(eq(metric));
    }
}