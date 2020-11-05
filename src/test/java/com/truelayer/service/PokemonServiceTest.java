package com.truelayer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.truelayer.TestHelper;
import com.truelayer.config.PokedexConfig;
import com.truelayer.config.ShakespeareConfig;
import com.truelayer.domain.Pokemon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;

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
        TestHelper testHelper = new TestHelper();
        String pokemonName = "Charmander";
        String description = "\"Obviously prefers hot places. When it rains, steam is said to spout from the tip of its tail.\"";
        String expectedPokemonDescription = "Obviously prefers hot places. At which hour 't rains,  steam is did doth sayeth to spout from the tip of its tail.";

        JsonNode pokedexResponseNodes = testHelper.generateResponseNodes("pokedex-response.json");
        JsonNode shakespeareResponseNodes = testHelper.generateResponseNodes("shakespeare-response.json");

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
    void returnsNoDescriptionIfPokemonDoesNotExist() {
        String pokemonName = "foo";
        String expectedPokemonDescription = "";

        when(pokedexConfig.getEndpoint()).thenReturn(POKEDEX_URL);
        when(restAPIService.restCall(POKEDEX_URL, pokemonName)).thenReturn(null);

        Optional<Pokemon> pokemon = pokemonService.getPokemonShakespeareanDescription(pokemonName);
        pokemon.ifPresent(p -> {
            assertEquals(expectedPokemonDescription, p.getDescription());
        });
    }

    @Test
    void returnsNoDescriptionIfShakespeareParsingFails() throws IOException {
        TestHelper testHelper = new TestHelper();
        String pokemonName = "Charmander";
        String description = "\"Obviously prefers hot places. When it rains, steam is said to spout from the tip of its tail.\"";
        String expectedPokemonDescription = "";

        JsonNode pokedexResponseNodes = testHelper.generateResponseNodes("pokedex-response.json");

        when(pokedexConfig.getEndpoint()).thenReturn(POKEDEX_URL);
        when(restAPIService.restCall(POKEDEX_URL, pokemonName)).thenReturn(pokedexResponseNodes);
        when(pokedexConfig.getNodeToBeFound()).thenReturn(POKEDEX_NODE);
        when(shakespeareConfig.getEndpoint()).thenReturn(SHAKESPEARE_URL);
        when(restAPIService.restCall(SHAKESPEARE_URL, description)).thenReturn(null);

        Optional<Pokemon> pokemon = pokemonService.getPokemonShakespeareanDescription(pokemonName);
        pokemon.ifPresent(p -> {
            assertEquals(expectedPokemonDescription, p.getDescription());
        });
    }
}
