package com.truelayer.web;

import com.truelayer.domain.Pokemon;
import com.truelayer.service.PokemonService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("/pokemon")
@Slf4j
public class PokemonController {

    private final PokemonService pokemonService;

    @Autowired
    public PokemonController(final PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @ApiOperation(value = "Get the Pokemon Shakespearean description")
    @ApiResponses(value =
        {
            @ApiResponse(code = 200, message = "The request was successful and pokemon information are returned in the response", response = Map.class),
            @ApiResponse(code = 404, message = "The Pokemon you searched doesn't exist"),
            @ApiResponse(code = 500, message = "Internal error during retrieval of Pokemon description. Please try again later.")
        })
    @GetMapping("/{pokemonName}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Pokemon> getPokemonShakespeareanDescription(
        @PathVariable @ApiParam(value = "The pokemon name", name = "pokemonName", required = true) final String pokemonName) {

        log.info("Request for Pokemon {} shakespearean description received", pokemonName);
        return pokemonService.getPokemonShakespeareanDescription(pokemonName)
            .map(ResponseEntity::ok)
            .orElseGet(() -> notFound().build());
    }
}
