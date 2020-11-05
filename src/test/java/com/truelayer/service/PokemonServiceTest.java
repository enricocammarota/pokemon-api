package com.truelayer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truelayer.config.PokedexConfig;
import com.truelayer.config.ShakespeareConfig;
import com.truelayer.domain.Pokemon;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import static java.io.File.separator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

    private static final String POKEDEX_URL = "https://foo/";
    private static final String POKEDEX_NODE = "flavor_text_entries";
    private static final String SHAKESPEARE_URL = "https://bar/";
    private static final String SHAKESPEARE_NODE = "/contents/translated";

    @Mock
    private ShakespeareConfig shakespeareConfig;

    @Mock
    private PokedexConfig pokedexConfig;

    @Mock
    private RestAPIService restAPIService;

    @InjectMocks
    private PokemonService pokemonService;

    @Test
    void correctlyRetrievePokemonShakespeareanDescription() throws IOException {

        String pokemonName = "Charmander";
        String description = "\"Obviously prefers hot places. When it rains, steam is said to spout from the tip of its tail.\"";
        String expectedPokemonDescription = "Obviously prefers hot places. At which hour 't rains,  steam is did doth sayeth to spout from the tip of its tail.";

        JsonNode pokedexResponseNodes = generateResponseNodes("pokedex-response.json");
        JsonNode shakespeareResponseNodes = generateResponseNodes("shakespeare-response.json");

        when(pokedexConfig.getEndpoint()).thenReturn(POKEDEX_URL);
        when(restAPIService.restCall(POKEDEX_URL, pokemonName)).thenReturn(pokedexResponseNodes);
        when(pokedexConfig.getNodeToBeFound()).thenReturn(POKEDEX_NODE);
        when(shakespeareConfig.getEndpoint()).thenReturn(SHAKESPEARE_URL);
        when(shakespeareConfig.getNodeToBeFound()).thenReturn(SHAKESPEARE_NODE);
        when(restAPIService.restCall(SHAKESPEARE_URL, description)).thenReturn(shakespeareResponseNodes);

        Optional<Pokemon> pokemon = pokemonService.getPokemonShakespeareanDescription(pokemonName);
        pokemon.ifPresent(p -> {
            assertEquals(expectedPokemonDescription, p.getDescription());
        });
    }

    @Test
    void correctlyReturnsNoDescriptionIfPokemonDoesNotExist() {
        String pokemonName = "foo";

        when(pokedexConfig.getEndpoint()).thenReturn(POKEDEX_URL);
        when(restAPIService.restCall(POKEDEX_URL, pokemonName)).thenReturn(null);

        Optional<Pokemon> pokemon = pokemonService.getPokemonShakespeareanDescription(pokemonName);
        pokemon.ifPresent(p -> {
            assertEquals("", p.getDescription());
        });
    }

    private JsonNode generateResponseNodes(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String filePathPodex = Paths.get("src", "test", "resources").toString() + separator + fileName;
        String response = FileUtils.readFileToString(new File(filePathPodex), "UTF-8");
        return mapper.readTree(response);
    }

    //HERE - OTHER TESTS
}
