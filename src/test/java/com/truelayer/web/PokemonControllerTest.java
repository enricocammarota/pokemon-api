package com.truelayer.web;

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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = PokemonController.class)
@ContextConfiguration(classes = {PokemonServiceApplication.class})
class PokemonControllerTest {

    private static final String GET_POKEMON_SHAKESPEAREAN_DESCRIPTION_URL = "/pokemon";
    private static final String POKEDEX_API_URL = "http://foo";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokemonService pokemonService;

    @MockBean
    private PokedexConfig pokedexConfig;

    @Test
    void correctlyRetrievePokemonShakespeareanDescription() throws Exception {
        String pokemonName = "charizard";
        String description = "strong and red dragon";
        String expectedResponse = "{\"name\":\"charizard\",\"description\":\"strong and red dragon\"}";

        when(pokedexConfig.getEndpoint()).thenReturn(POKEDEX_API_URL);
        when(pokemonService.getPokemonShakespeareanDescription(pokemonName)).thenReturn(Optional.of(new Pokemon(pokemonName, description)));

        mockMvc.perform(get(GET_POKEMON_SHAKESPEAREAN_DESCRIPTION_URL + "/" + pokemonName))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponse));
    }

    @Test
    void returnsNotFoundIfThePokemonDoesNotExists() throws Exception {
        String pokemonName = "foo";

        when(pokedexConfig.getEndpoint()).thenReturn(POKEDEX_API_URL);
        when(pokemonService.getPokemonShakespeareanDescription(pokemonName)).thenReturn(Optional.empty());

        mockMvc.perform(get(GET_POKEMON_SHAKESPEAREAN_DESCRIPTION_URL + "/" + pokemonName))
            .andExpect(status().isNotFound());
    }

}
