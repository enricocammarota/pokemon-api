package com.truelayer.util;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static java.util.Locale.ENGLISH;

@Slf4j
public final class PokedexResponseParser {

    private static final String newLineRegEx = "\\\\n";

    private static final String formFeedCharacterRegEx = "\\\\f";

    private PokedexResponseParser() {

    }

    public static String pokedexAPIJsonParsing(String pokemonName, JsonNode locatedNode) {
        log.info("Started Pokedex response parsing for {}", pokemonName);

        List<String> descriptions = new ArrayList<>();
        Iterator<JsonNode> descriptionsNodes = locatedNode.elements();

        while (descriptionsNodes.hasNext()) {
            JsonNode description = descriptionsNodes.next();
            if (description.at("/language/name").asText().equals(ENGLISH.getLanguage())) {
                descriptions.add(description.get("flavor_text").toString()
                    .replaceAll(newLineRegEx, "")
                    .replaceAll(formFeedCharacterRegEx, ""));
            }
        }

        Random random = new Random();
        log.info("Finished Pokedex response parsing for {}!", pokemonName);
        return descriptions.get(random.nextInt(descriptions.size()));
    }
}
