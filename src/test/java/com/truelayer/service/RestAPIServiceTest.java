package com.truelayer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truelayer.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;

@ExtendWith(MockitoExtension.class)
class RestAPIServiceTest {

    private static final String POKEDEX_URL = "https://foo/";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RestAPIService restAPIService;

    @Test
    void correctlyPerformARestAPICall() throws IOException {
        TestHelper testHelper = new TestHelper();
        String pokemonName = "Charmeleon";

        ResponseEntity<String> responseEntity = testHelper.generateResponseEntity("pokedex-rest-response");
        JsonNode rootNode = testHelper.generateResponseNodes("pokedex-response.json");

        when(restTemplate.exchange(POKEDEX_URL + pokemonName, GET, new HttpEntity<>(pokemonName, getHttpHeaders()), String.class))
            .thenReturn(responseEntity);
        when(objectMapper.readTree(Objects.requireNonNull(responseEntity.getBody()))).thenReturn(rootNode);

        JsonNode response = restAPIService.restCall(POKEDEX_URL, pokemonName);
        assertNotNull(response);
    }

    @Test
    void throwsExceptionIfResponseIsEmpty() throws IOException {
        when(objectMapper.readTree(anyString())).thenThrow(JsonProcessingException.class);
        assertThatThrownBy(() -> objectMapper.readTree(anyString()))
            .isInstanceOf(JsonProcessingException.class);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user-agent", null);
        return headers;
    }
}
