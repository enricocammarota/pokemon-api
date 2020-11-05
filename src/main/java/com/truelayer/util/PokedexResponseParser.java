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

    private static final String NEW_LINE_PATTERN = "\\\\n";

    private static final String FORM_FEED_PATTERN = "\\\\f";

    private static final Random random = new Random();

    private PokedexResponseParser() {

    }

    public static String pokedexAPIJsonParsing(String pokemonName, JsonNode locatedNode) {
        log.info("Started Pokedex response parsing for {}", pokemonName);

        List<String> descriptions = new ArrayList<>();
        Iterator<JsonNode> descriptionsNodes = locatedNode.elements();

        descriptionsNodes.forEachRemaining(description -> {
            if (description.at("/language/name").asText().equals(ENGLISH.getLanguage())) {
                descriptions.add(description.get("flavor_text").toString()
                    .replaceAll(NEW_LINE_PATTERN, "")
                    .replaceAll(FORM_FEED_PATTERN, ""));
            }
        });

        log.info("Finished Pokedex response parsing for {}!", pokemonName);
        return descriptions.get(random.nextInt(descriptions.size()));
    }
}
