package com.example.springbootwebdata.controller;

import com.example.springbootwebdata.model.HealthMetric;
import com.example.springbootwebdata.repository.HealthMetricsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HealthMetricsCRUDControllerTest {
    private static final String PAYLOAD = "{\"name\":\"water.cup\",\"value\":\"1\",\"timestamp\":\"2017-11-01T18:25:43.511Z\"}";

    @Mock
    private HealthMetricsRepository healthMetricsRepository;

    private HealthMetricsCRUDController instance;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        instance = new HealthMetricsCRUDController(healthMetricsRepository);
        //MockMvc must be initialized manually when you inject mock dependencies otherwise
        //annotate the test class with @AutoConfigureMockMvc and @Autowired for mockMvc field class
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

        mockMvc.perform(
                put("/healthmetrics/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD))
                .andExpect(status().is2xxSuccessful());

        verify(healthMetricsRepository).save(eq(metric));
    }

    @Test
    public void putRequestShouldReturn404WhenMetricIdDoesNotExist() throws Exception {
        HealthMetric metric = objectMapper.readValue(PAYLOAD, HealthMetric.class);
        metric.setId(1l);
        when(healthMetricsRepository.save(any(HealthMetric.class))).thenReturn(metric);
        when(healthMetricsRepository.exists(1l)).thenReturn(false);

        mockMvc.perform(
                put("/healthmetrics/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD))
                .andExpect(status().is4xxClientError());

        verify(healthMetricsRepository).exists(eq(1l));
        verify(healthMetricsRepository, times(0)).save(eq(metric));
    }
}