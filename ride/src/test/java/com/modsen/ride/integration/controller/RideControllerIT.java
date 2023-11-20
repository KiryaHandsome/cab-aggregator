package com.modsen.ride.integration.controller;

import com.modsen.ride.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class RideControllerIT extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void simpleTest() {
        assertThat(mockMvc).isNotNull();
    }
}
