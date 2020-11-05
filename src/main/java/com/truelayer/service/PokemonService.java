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

    @Autowired
    private final RestAPIService restAPIService;

    public Optional<Pokemon> getPokemonShakespeareanDescription(final String pokemonName) {
        String description = getDescription(pokemonName);

        if (description == null) {
            return empty();
        } else {
            return Optional.of(new Pokemon(pokemonName, description));
        }
    }

    private String getDescription(String pokemonName) {
        String pokedexDescription = getPokedexDescription(pokemonName);
        if (pokedexDescription != null) {
            return getShakespeareanDescription(pokemonName, pokedexDescription);
        }
        return null;
    }

    private String getPokedexDescription(String pokemonName) {
        log.info("Querying pokemon description for {} from pokedex", pokemonName);
        JsonNode rootNode = restAPIService.restCall(pokedexConfig.getEndpoint(), pokemonName, GET);
        JsonNode responseNode = rootNode.findValue(pokedexConfig.getNodeToBeFound());
        return PokedexResponseParser.pokedexAPIJsonParsing(pokemonName, responseNode);
    }

    private String getShakespeareanDescription(String pokemonName, String pokedexDescription) {
        log.info("Parsing {} description in Shakespearean", pokemonName);
        JsonNode rootNode = restAPIService.restCall(shakespeareConfig.getEndpoint(), pokedexDescription, POST);
        JsonNode responseNode = rootNode.at(shakespeareConfig.getNodeToBeFound());
        return responseNode.toString()
//            .replaceAll("\\\\n", "")
//            .replaceAll("\"", "")
            .replaceAll("[\\\\|\"]","");
    }

}
