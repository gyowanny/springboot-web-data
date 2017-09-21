package com.example.springbootwebdata.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class HealthMetricsCRUDControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HealthMetricsCRUDController instance;

    @Test
    public void postRequestShouldSaveAValidMetricAndReturn200() throws Exception {
        mockMvc.perform(
                    post("/healthmetrics")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"water.cup\",\"value\":\"1\",\"timestamp\":\"2017-11-01T18:25:43.511Z\"}"))
                .andExpect(status().is2xxSuccessful());
    }
}