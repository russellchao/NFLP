package com.nflp.nflp_backend.service;

import com.nflp.nflp_backend.dto.SentimentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class NLPClientServiceTest {

    @Autowired
    private NLPClientService nlpClientService;

    @Test
    public void testHealthCheck() {
        boolean healthy = nlpClientService.isNLPServiceHealthy();
        assertThat(healthy).isTrue();
    }

    @Test
    public void testSentimentAnalysis() {
        SentimentResponse response = nlpClientService.analyzeSentiment(
                "Josh Allen threw an amazing touchdown!"
        );

        System.out.println(response);

        assertThat(response).isNotNull();
        assertThat(response.getLabel()).isEqualTo("POSITIVE");
        assertThat(response.getScore()).isGreaterThan(BigDecimal.valueOf(0));
    }
}
