package com.truelayer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static java.io.File.separator;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.springframework.http.HttpStatus.OK;

public class TestHelper {

    public JsonNode generateResponseNodes(String fileName) throws IOException {
        String filePathPokedex = Paths.get("src", "test", "resources").toString() + separator + fileName;
        return new ObjectMapper().readTree(readFileToString(new File(filePathPokedex), UTF_8));
    }

    public ResponseEntity<String> generateResponseEntity(String fileName) throws IOException {
        String filePathPokedex = Paths.get("src", "test", "resources").toString() + separator + fileName;
        return new ResponseEntity<>(readFileToString(new File(filePathPokedex), UTF_8), OK);
    }
}
