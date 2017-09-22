package uk.co.gyotools.healthmetrics.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class StatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StatusController instance;

    @Test
    public void statusShouldReturnOK() throws Exception {
        mockMvc.perform(get("/private/status"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().string("OK"));
    }

}