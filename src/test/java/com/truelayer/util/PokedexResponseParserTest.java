package com.truelayer.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.truelayer.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.truelayer.util.PokedexResponseParser.pokedexAPIJsonParsing;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class PokedexResponseParserTest {

    @Test
    void correctlyParseResponse() throws IOException {
        TestHelper testHelper = new TestHelper();
        String pokemonName = "Charmeleon";
        String expectedDescription = "\"When it swings its burning tail, it elevates the temperature to unbearably high levels.\"";

        JsonNode node = testHelper.generateResponseNodes("single-pokedex-description.json");

        assertEquals(expectedDescription, pokedexAPIJsonParsing(pokemonName, node));
    }

    @Test
    void returnsNullIfOnlyNonEnDescriptionsArePresent() throws IOException {
        TestHelper testHelper = new TestHelper();
        String pokemonName = "Charmeleon";

        JsonNode node = testHelper.generateResponseNodes("single-pokedex-non-en-description.json");

        assertNull(pokedexAPIJsonParsing(pokemonName, node));
    }

    @Test
    void returnsNullIfNoNodeIsRetrieved() throws IOException {
        String pokemonName = "Charmeleon";

        assertNull(pokedexAPIJsonParsing(pokemonName, null));
    }
}
