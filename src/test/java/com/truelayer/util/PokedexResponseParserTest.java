package com.truelayer.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static com.truelayer.util.PokedexResponseParser.pokedexAPIJsonParsing;
import static java.io.File.separator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class PokedexResponseParserTest {

    @Test
    void correctlyParseResponse() throws IOException {
        String pokemonName = "Charmeleon";
        String expectedDescription = "\"When it swings its burning tail, it elevates the temperature to unbearably high levels.\"";

        JsonNode node = generateResponseNodes("single-pokedex-description.json");

        assertEquals(expectedDescription, pokedexAPIJsonParsing(pokemonName, node));
    }

    @Test
    void returnsNullIfOnlyNonEnDescriptionsArePresent() throws IOException {
        String pokemonName = "Charmeleon";

        JsonNode node = generateResponseNodes("single-pokedex-non-en-description.json");

        assertNull(pokedexAPIJsonParsing(pokemonName, node));
    }

    private JsonNode generateResponseNodes(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String filePathPokedex = Paths.get("src", "test", "resources").toString() + separator + fileName;
        String response = FileUtils.readFileToString(new File(filePathPokedex), "UTF-8");
        return mapper.readTree(response);
    }
}
