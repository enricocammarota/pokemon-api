package com.truelayer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

import static java.io.File.separator;
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
        String pokemonName = "Charmeleon";

        ResponseEntity<String> responseEntity = generateResponseEntity("pokedex-rest-response");
        JsonNode rootNode = generateResponseNodes("pokedex-response.json");

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

    private ResponseEntity<String> generateResponseEntity(String fileName) throws IOException {
        String filePathPodex = Paths.get("src", "test", "resources").toString() + separator + fileName;
        return new ResponseEntity<>(FileUtils.readFileToString(new File(filePathPodex), "UTF-8"),
            HttpStatus.OK);
    }

    private JsonNode generateResponseNodes(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String filePathPokedex = Paths.get("src", "test", "resources").toString() + separator + fileName;
        String response = FileUtils.readFileToString(new File(filePathPokedex), "UTF-8");
        return mapper.readTree(response);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user-agent", null);
        return headers;
    }
}
