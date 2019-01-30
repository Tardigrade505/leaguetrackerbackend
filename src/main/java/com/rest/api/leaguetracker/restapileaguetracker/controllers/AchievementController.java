package com.rest.api.leaguetracker.restapileaguetracker.controllers;

import com.rest.api.leaguetracker.restapileaguetracker.assemblers.AchievementResourceAssembler;
import com.rest.api.leaguetracker.restapileaguetracker.exceptions.AchievementNotFoundException;
import com.rest.api.leaguetracker.restapileaguetracker.objects.Achievement;
import com.rest.api.leaguetracker.restapileaguetracker.repositories.AchievementRepository;
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
class AchievementController {

    private final AchievementRepository repository;
    private final AchievementResourceAssembler assembler;

    AchievementController(AchievementRepository repository, AchievementResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // Aggregate root
    @GetMapping("/achievements")
    public Resources<Resource<Achievement>> all() {
        List<Resource<Achievement>> achievements = repository.findAll().stream()
                .map(assembler::toResource).collect(Collectors.toList());

        return new Resources<>(achievements,
                linkTo(methodOn(AchievementController.class).all()).withSelfRel());
    }

    @PostMapping("/achievements")
    public ResponseEntity<Resource<Achievement>> newAchievement(@RequestBody Achievement achievement) throws URISyntaxException {
        Resource<Achievement> resource = assembler.toResource(repository.save(achievement));

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    // Single item

    @GetMapping("/achievements/{id}")
    public Resource<Achievement> one(@PathVariable Long id) {

        Achievement achievement =  repository.findById(id)
                .orElseThrow(() -> new AchievementNotFoundException(id));

        return assembler.toResource(achievement);
    }

    @PutMapping("/achievements/{id}")
    public ResponseEntity<Resource<Achievement>> replaceAchievement(@RequestBody Achievement newAchievement, @PathVariable Long id) throws URISyntaxException {

        Achievement updatedAchievement =  repository.findById(id)
                .map(achievement -> {
                    achievement.setName(newAchievement.getName());
                    return repository.save(achievement);
                })
                .orElseGet(() -> {
                    newAchievement.setId(id);
                    return repository.save(newAchievement);
                });

        Resource<Achievement> resource = assembler.toResource(updatedAchievement);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);

    }

    @DeleteMapping("/achievements/{id}")
    public ResponseEntity<?> deleteAchievement(@PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}