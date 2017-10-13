package uk.co.gyotools.selfmetrics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.co.gyotools.selfmetrics.selfmetrics.AbstractUnitTest;
import uk.co.gyotools.selfmetrics.model.SelfMetric;
import uk.co.gyotools.selfmetrics.model.SelfMetricEntry;
import uk.co.gyotools.selfmetrics.model.payload.SelfMetricEntryPayload;
import uk.co.gyotools.selfmetrics.dao.SelfMetricEntryDao;
import uk.co.gyotools.selfmetrics.dao.SelfMetricDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class SelfMetricsEntryControllerTest extends AbstractUnitTest {
    private static final String URI_PATH = "/selfmetrics/entry";
    private static final String PAYLOAD = "{\"metricId\":\"1\",\"value\":\"1\",\"timestamp\":\"2017-11-01T18:25:43.511Z\"}";

    @MockBean
    private SelfMetricEntryDao selfMetricEntryDao;
    @MockBean
    private SelfMetricDao selfMetricDao;
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Date now;
    private SelfMetricEntryPayload payload;
    private SelfMetricEntry metricEntry;

    @Before
    public void setUp() throws Exception {
        now = new Date();
        metricEntry = createSelfMetricEntry(null, "metricName", "1", now);
    }

    @Test
    public void postRequestShouldSaveAValidMetricAndReturn200() throws Exception {
        // Given
        SelfMetric metric = new SelfMetric();
        metric.setName("metricName");
        metric.setId("1");
        when(selfMetricEntryDao.save(any())).thenReturn("1");
        when(selfMetricDao.findById(any())).thenReturn(metric);

        payload = new SelfMetricEntryPayload("1", "1", now);

        // When
        mockMvc.perform(
                    post(URI_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void putRequestShouldUpdateAnExistingMetricAndReturn200() throws Exception {
        // Given
        SelfMetricEntry metricEntry = createSelfMetricEntry("1", "entry", "1", new Date());
        when(selfMetricEntryDao.findById(any())).thenReturn(metricEntry);

        final String newName = "New Name";
        metricEntry.setName(newName);
        payload = new SelfMetricEntryPayload("1", metricEntry);

        // When
        mockMvc.perform(
                put(URI_PATH + "/{id}", metricEntry.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().is2xxSuccessful());

        // Then
        verify(selfMetricEntryDao).findById(any());
        verify(selfMetricEntryDao).save(any());
    }

    @Test
    public void putRequestShouldReturn400WhenMetricIdDoesNotExist() throws Exception {
        // When
        mockMvc.perform(
                put(URI_PATH + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getRequestShouldReturnAnExistingHealthMetric() throws Exception {
        // Given
        metricEntry = createSelfMetricEntry("1", "entry", "1", new Date());
        when(selfMetricEntryDao.findById(any())).thenReturn(metricEntry);

        // When
        mockMvc.perform(
                get(URI_PATH + "/{id}", metricEntry.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(metricEntry)));
    }

    @Test
    public void getRequestShouldReturnNotFoundForInvalidId() throws Exception {
        // When
        mockMvc.perform(
                get(URI_PATH + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void getMetricsByNameAndDateIntervalShouldReturnAListOfMetrics() throws Exception {
        // Given
        String metricName = "metricName";
        List<SelfMetricEntry> metricsList = asList(
                createSelfMetricEntry("1", metricName, "1", new Date()),
                createSelfMetricEntry("2", metricName, "1", new Date()),
                createSelfMetricEntry("3", metricName, "1", new Date())
        );
        when(selfMetricEntryDao.findByNameAndTimestampBetween(any(), any(), any())).thenReturn(metricsList);
        final LocalDateTime from = LocalDateTime.of(2017, Month.APRIL, 1, 1, 0, 0, 0);
        final LocalDateTime to = LocalDateTime.of(2018, Month.APRIL, 1, 3, 0, 0, 0);

        // When
        mockMvc.perform(get(URI_PATH + "/{name}/{from}/{to}", metricName, toISOFormat(from), toISOFormat(to)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(metricsList)));

        //Then
        verify(selfMetricEntryDao).findByNameAndTimestampBetween(any(), any(), any());
    }

    private String toISOFormat(LocalDateTime from) {
        return DateTimeFormatter.ISO_DATE_TIME.format(from);
    }

    private SelfMetricEntry createSelfMetricEntry(String id, String name, String value, Date timestamp) {
        SelfMetricEntry metricEntry = new SelfMetricEntry();
        metricEntry.setId(id);
        metricEntry.setName(name);
        metricEntry.setValue(value);
        metricEntry.setTimestamp(timestamp);
        return metricEntry;
    }
}