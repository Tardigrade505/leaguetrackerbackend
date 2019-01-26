package com.rest.api.leaguetracker.restapileaguetracker.assemblers;

import com.rest.api.leaguetracker.restapileaguetracker.controllers.GameController;
import com.rest.api.leaguetracker.restapileaguetracker.objects.Game;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class GameResourceAssembler implements ResourceAssembler<Game, Resource<Game>> {

    @Override
    public Resource<Game> toResource(Game game) {

        return new Resource<>(game,
                linkTo(methodOn(GameController.class).one(game.getId())).withSelfRel(),
                linkTo(methodOn(GameController.class).getNewGame(game.getSeasonId(), null)).withRel("games"));
    }
}