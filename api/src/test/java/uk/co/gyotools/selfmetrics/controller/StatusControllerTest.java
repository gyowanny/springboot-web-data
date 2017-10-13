package uk.co.gyotools.selfmetrics.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.gyotools.selfmetrics.selfmetrics.AbstractUnitTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class StatusControllerTest extends AbstractUnitTest {

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