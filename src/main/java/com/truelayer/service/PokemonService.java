package com.truelayer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.truelayer.config.PokedexConfig;
import com.truelayer.config.ShakespeareConfig;
import com.truelayer.domain.Pokemon;
import com.truelayer.util.PokedexResponseParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Service
@Slf4j
@AllArgsConstructor
public class PokemonService {

    private final PokedexConfig pokedexConfig;

    private final ShakespeareConfig shakespeareConfig;

    private static final String REG_EX_PATTERN = "[\\\\|\"]";

    @Autowired
    private final RestAPIService restAPIService;

    public Optional<Pokemon> getPokemonShakespeareanDescription(final String pokemonName) {
        String description = getDescription(pokemonName);

        return description == null ? empty() : Optional.of(new Pokemon(pokemonName, description));
    }

    private String getDescription(String pokemonName) {
        String pokedexDescription = getPokedexDescription(pokemonName);

        return pokedexDescription != null ? getShakespeareanDescription(pokemonName, pokedexDescription) : null;
    }

    private String getPokedexDescription(String pokemonName) {
        log.info("Querying Pokemon description for {} from pokedex", pokemonName);

        JsonNode rootNode = restAPIService.restCall(pokedexConfig.getEndpoint(), pokemonName, GET);
        if (rootNode != null) {
            JsonNode responseNode = rootNode.findValue(pokedexConfig.getNodeToBeFound());
            return PokedexResponseParser.pokedexAPIJsonParsing(pokemonName, responseNode);
        } else {
            return  null;
        }
    }

    private String getShakespeareanDescription(String pokemonName, String pokedexDescription) {
        log.info("Querying Shakespearean description for {}", pokemonName);

        JsonNode rootNode = restAPIService.restCall(shakespeareConfig.getEndpoint(), pokedexDescription, POST);
        if (rootNode != null) {
            JsonNode responseNode = rootNode.at(shakespeareConfig.getNodeToBeFound());
            return responseNode.toString().replaceAll(REG_EX_PATTERN, "");
        } else {
            return null;
        }
    }

}
