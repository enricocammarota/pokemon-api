package com.truelayer.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.truelayer.config.PokedexConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Slf4j
public final class PokedexResponseParser {

    private PokedexResponseParser() {

    }

    public static String pokedexAPIJsonParsing(String pokemonName, JsonNode locatedNode) {
        log.info("Started Pokedex response parsing for {}", pokemonName);

        List<String> descriptions = new ArrayList<>();
        Iterator<JsonNode> descriptionsNodes = locatedNode.elements();

        while (descriptionsNodes.hasNext()) {
            JsonNode description = descriptionsNodes.next();
            if (description.at("/language/name").asText().equals("en")) {
                descriptions.add(description.get("flavor_text").toString()
                    .replaceAll("\\\\n", " ")
                    .replaceAll("\\\\f", " "));
            }
        }

        Random random = new Random();
        log.info("Finished Pokedex response parsing!");
        return descriptions.get(random.nextInt(descriptions.size()));
    }
}
