package com.rest.api.leaguetracker.restapileaguetracker.controllers;

import com.rest.api.leaguetracker.restapileaguetracker.assemblers.PlayerResourceAssembler;
import com.rest.api.leaguetracker.restapileaguetracker.exceptions.PlayerNotFoundException;
import com.rest.api.leaguetracker.restapileaguetracker.objects.Player;
import com.rest.api.leaguetracker.restapileaguetracker.objects.Season;
import com.rest.api.leaguetracker.restapileaguetracker.repositories.PlayerRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public
class PlayerController {

    private final PlayerRepository playerRepository;
    private final PlayerResourceAssembler assembler;

    public PlayerController(PlayerRepository playerRepository,
                     PlayerResourceAssembler assembler) {

        this.playerRepository = playerRepository;
        this.assembler = assembler;
    }

    @GetMapping("/players")
    public Resources<Resource<Player>> all() {

        List<Resource<Player>> players = playerRepository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(players,
                linkTo(methodOn(PlayerController.class).all()).withSelfRel());
    }

    @GetMapping("/players/{id}")
    public Resource<Player> one(@PathVariable Long id) {
        return assembler.toResource(
                playerRepository.findById(id)
                        .orElseThrow(() -> new PlayerNotFoundException(id)));
    }

    @PostMapping("/players")
    public ResponseEntity<Resource<Player>> newPlayer(@RequestBody Player player) {
        Player newPlayer = playerRepository.save(player);

        return ResponseEntity
                .created(linkTo(methodOn(PlayerController.class).one(newPlayer.getId())).toUri())
                .body(assembler.toResource(newPlayer));
    }

    @PutMapping("/players/{id}")
    public ResponseEntity<Resource<Player>> replacePlayer(@RequestBody Player newPlayer, @PathVariable Long id) throws URISyntaxException {

        Player updatedPlayer =  playerRepository.findById(id)
                .map(player -> {
                    player.setTotalPoints(newPlayer.getTotalPoints());
                    player.setName(newPlayer.getName());
                    return playerRepository.save(player);
                })
                .orElseGet(() -> {
                    newPlayer.setId(id);
                    return playerRepository.save(newPlayer);
                });

        Resource<Player> resource = assembler.toResource(updatedPlayer);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }
}