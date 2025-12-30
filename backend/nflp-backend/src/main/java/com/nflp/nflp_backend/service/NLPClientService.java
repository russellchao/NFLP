package com.nflp.nflp_backend.service;

import com.nflp.nflp_backend.dto.EntityResponse;
import com.nflp.nflp_backend.dto.SentimentResponse;
import com.nflp.nflp_backend.dto.ToxicityResponse;
import com.nflp.nflp_backend.exception.NLPServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class NLPClientService {

    private final RestTemplate restTemplate;
    private final String nlpServiceUrl;

    public NLPClientService(RestTemplate restTemplate, @Value("${nlp.service.url}") String nlpServiceUrl) {
        this.restTemplate = restTemplate;
        this.nlpServiceUrl = nlpServiceUrl;
    }

    /**
     * Call Python NLP service to analyze sentiment
     *
     * @param text Text to analyze
     * @return SentimentResponse with scores and label
     */
    public SentimentResponse analyzeSentiment(String text) {
        log.info("Calling NLP service for sentiment analysis");

        try {
            // Prepare request
            String url = nlpServiceUrl + "/api/sentiment";
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("text", text);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

            // Make HTTP POST call
            ResponseEntity<SentimentResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    SentimentResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("Sentiment analysis successful: {}", response.getBody().getLabel());
                return response.getBody();
            } else {
                throw new NLPServiceException("NLP service returned empty response");
            }

        } catch (RestClientException e) {
            log.error("Error calling NLP sentiment service: {}", e.getMessage());
            throw new NLPServiceException("Failed to analyze sentiment: " + e.getMessage(), e);
        }
    }

    /**
     * Call Python NLP service to extract entities (players, teams)
     *
     * @param text Text to analyze
     * @return EntityResponse with players and teams
     */
    public EntityResponse extractEntities(String text) {
        log.info("Calling NLP service for entity extraction");

        try {
            // Prepare request
            String url = nlpServiceUrl + "/api/entity";
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("text", text);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

            // Make HTTP POST call
            ResponseEntity<EntityResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    EntityResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                EntityResponse entities = response.getBody();
                log.info("Entity extraction successful: {} players, {} teams",
                        entities.getPlayers().size(),
                        entities.getTeams().size());
                return entities;
            } else {
                throw new NLPServiceException("NLP service returned empty response");
            }

        } catch (RestClientException e) {
            log.error("Error calling NLP entity service: {}", e.getMessage());
            throw new NLPServiceException("Failed to extract entities: " + e.getMessage(), e);
        }
    }

    /**
     * Call Python NLP service to detect toxicity
     *
     * @param text Text to analyze
     * @return ToxicityResponse with toxicity score and flagged words
     */
    public ToxicityResponse detectToxicity(String text) {
        log.info("Calling NLP service for toxicity detection");

        try {
            // Prepare request
            String url = nlpServiceUrl + "/api/toxicity";
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("text", text);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

            // Make HTTP POST call
            ResponseEntity<ToxicityResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    ToxicityResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ToxicityResponse toxicity = response.getBody();
                log.info("Toxicity detection successful: score={}, toxic={}",
                        toxicity.getToxicityScore(),
                        toxicity.getIsToxic());
                return toxicity;
            } else {
                throw new NLPServiceException("NLP service returned empty response");
            }

        } catch (RestClientException e) {
            log.error("Error calling NLP toxicity service: {}", e.getMessage());
            throw new NLPServiceException("Failed to detect toxicity: " + e.getMessage(), e);
        }
    }

    /**
     * Perform complete NLP analysis (sentiment + entities + toxicity)
     *
     * @param text Text to analyze
     * @return Map with all analysis results
     */
    public Map<String, Object> analyzeComplete(String text) {
        log.info("Performing complete NLP analysis");

        Map<String, Object> results = new HashMap<>();

        try {
            SentimentResponse sentiment = analyzeSentiment(text);
            EntityResponse entities = extractEntities(text);
            ToxicityResponse toxicity = detectToxicity(text);

            results.put("sentiment", sentiment);
            results.put("entities", entities);
            results.put("toxicity", toxicity);

            return results;

        } catch (NLPServiceException e) {
            log.error("Complete NLP analysis failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Check if NLP service is healthy
     *
     * @return true if service is reachable
     */
    public boolean isNLPServiceHealthy() {
        try {
            String url = nlpServiceUrl + "/health";
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (RestClientException e) {
            log.warn("NLP service health check failed: {}", e.getMessage());
            return false;
        }
    }
}