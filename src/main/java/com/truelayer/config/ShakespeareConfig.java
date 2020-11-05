package com.truelayer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "shakespeareapiurl")
@Data
public class ShakespeareConfig {

    private String endpoint;

    private String nodeToBeFound;
}
