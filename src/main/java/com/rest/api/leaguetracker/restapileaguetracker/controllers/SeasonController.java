package com.rest.api.leaguetracker.restapileaguetracker.controllers;

import com.rest.api.leaguetracker.restapileaguetracker.assemblers.SeasonResourceAssembler;
import com.rest.api.leaguetracker.restapileaguetracker.exceptions.SeasonNotFoundException;
import com.rest.api.leaguetracker.restapileaguetracker.objects.Player;
import com.rest.api.leaguetracker.restapileaguetracker.objects.Season;
import com.rest.api.leaguetracker.restapileaguetracker.repositories.SeasonRepository;
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

@CrossOrigin
@RestController
public
class SeasonController {

    private final SeasonRepository repository;
    private final SeasonResourceAssembler assembler;

    SeasonController(SeasonRepository repository, SeasonResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // Aggregate root
    @GetMapping("/seasons")
    public Resources<Resource<Season>> all() {
        List<Resource<Season>> seasons = repository.findAll().stream()
                .map(assembler::toResource).collect(Collectors.toList());

        return new Resources<>(seasons,
                linkTo(methodOn(SeasonController.class).all()).withSelfRel());
    }

    @PostMapping("/seasons")
    public ResponseEntity<Resource<Season>> newSeason(@RequestBody Season newSeason) throws URISyntaxException {
        Resource<Season> resource = assembler.toResource(repository.save(newSeason));

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    @PostMapping("/seasons/{id}/players")
    public ResponseEntity<Resource<Season>> newPlayer(@RequestBody Player newPlayer, @PathVariable Long id) throws URISyntaxException {
        Resource<Season> resource =  one(id);
        Season season = resource.getContent();
        season.addPlayer(newPlayer);
        return replaceSeason(season, id);
    }

    @GetMapping("/seasons/{id}/players")
    public Resources<Player> allPlayers(@PathVariable Long id) {
        Resource<Season> resource =  one(id);
        Season season = resource.getContent();
        List<Player> players = season.getPlayers();

        return new Resources<>(players,
                linkTo(methodOn(SeasonController.class).allPlayers(id)).withSelfRel());
    }

    // Single item

    @GetMapping("/seasons/{id}")
    public Resource<Season> one(@PathVariable Long id) {

        Season season =  repository.findById(id)
                .orElseThrow(() -> new SeasonNotFoundException(id));

        return assembler.toResource(season);
    }

    @PutMapping("/seasons/{id}")
    public ResponseEntity<Resource<Season>> replaceSeason(@RequestBody Season newSeason, @PathVariable Long id) throws URISyntaxException {

        Season updatedSeason =  repository.findById(id)
                .map(season -> {
                    season.setName(newSeason.getName());
                    season.setPlayers(newSeason.getPlayers());
                    season.setAchievements(newSeason.getAchievements());
                    return repository.save(season);
                })
                .orElseGet(() -> {
                    newSeason.setId(id);
                    return repository.save(newSeason);
                });

        Resource<Season> resource = assembler.toResource(updatedSeason);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    @DeleteMapping("/seasons/{id}")
    public ResponseEntity<?> deleteSeason(@PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
