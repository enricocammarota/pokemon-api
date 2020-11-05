package com.truelayer.service;

import com.truelayer.config.PokedexConfig;
import com.truelayer.domain.Pokemon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceTest {

    private static final String POKEDEX_URL = "https://pokeapi.co/api/v2/pokemon-species/";

    @Mock
    private PokedexConfig pokedexConfig;

    @Mock
    private RestTemplate restTemplate;

    private PokemonService pokemonService;

    @Test
    void correctlyRetrievePokemonShakespeareanDescription() {
        String pokemonName = "Charmander";
        String expectedPokemonDescription = "";

        when(pokedexConfig.getEndpoint()).thenReturn(POKEDEX_URL);
        when(restTemplate.getForObject(POKEDEX_URL, String.class)).thenReturn("");

        Optional<Pokemon> pokemon = pokemonService.getPokemonShakespeareanDescription(pokemonName);
        assertEquals(expectedPokemonDescription, pokemon.get().getDescription());
    }
}
