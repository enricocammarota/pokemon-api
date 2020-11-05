package com.truelayer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pokedexapiurl")
@Data
public class PokedexConfig {

    private String endpoint;

    private String nodeToBeFound;
}
