package com.truelayer.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truelayer.PokemonServiceApplication;
import com.truelayer.config.PokedexConfig;
import com.truelayer.domain.Pokemon;
import com.truelayer.service.PokemonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = PokemonController.class)
@ContextConfiguration(classes = {PokemonServiceApplication.class})
public class PokemonControllerTest {

    private static final String GET_POKEMON_SHAKESPEAREAN_DESCRIPTION_URL = "/pokemon";
    private static final String EXPECTED_DESCRIPTION = "qweqweqewewqeqwweqweq";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PokemonService pokemonService;

    @MockBean
    private PokedexConfig pokedexConfig;

    @Test
    void correctlyRetrievePokemonShakespeareanDescription() throws Exception {
        String pokemonName = "charizard";

        when(pokedexConfig.getEndpoint()).thenReturn("https://pokeapi.co/api/v2/pokemon-species/");
        when(pokemonService.getPokemonShakespeareanDescription(pokemonName)).thenReturn(Optional.of(new Pokemon("charizard", "strong and red dragon")));
        mockMvc.perform(get(GET_POKEMON_SHAKESPEAREAN_DESCRIPTION_URL + "/" + pokemonName))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(EXPECTED_DESCRIPTION)));
    }

    @Test
    void returnsNotFoundIfThePokemonDoesNotExists() throws Exception {
        String pokemonName = "foo";

        when(pokedexConfig.getEndpoint()).thenReturn("https://pokeapi.co/api/v2/pokemon-species/");
        when(pokemonService.getPokemonShakespeareanDescription(pokemonName)).thenReturn(Optional.empty());
        mockMvc.perform(get(GET_POKEMON_SHAKESPEAREAN_DESCRIPTION_URL + "/" + pokemonName))
            .andExpect(status().isNotFound());
    }

}
