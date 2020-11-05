package com.truelayer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@Slf4j
public class RestAPIService {

    JsonNode restCall(String endpoint, String field, HttpMethod method) {
        log.info("Performing rest call to {}", endpoint);
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        ResponseEntity<String> response = restTemplate.exchange(
            endpoint + field,
            method,
            new HttpEntity<>(field, getHttpHeaders()),
            String.class);

        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(Objects.requireNonNull(response.getBody()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.info("Finished rest call to {}", endpoint);
        return rootNode;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user-agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        return headers;
    }
}
