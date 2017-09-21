package com.example.springbootwebdata.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatusControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private StatusController instance;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(instance)
//                .addFilters(new CORSFilter())
                .build();
    }

    @Test
    public void statusShouldReturnOK() throws Exception {
        mockMvc.perform(get("/private/status"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().string("OK"));
    }

}