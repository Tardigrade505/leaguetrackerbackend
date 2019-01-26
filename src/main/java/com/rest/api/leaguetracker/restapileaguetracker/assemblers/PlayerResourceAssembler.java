package com.rest.api.leaguetracker.restapileaguetracker.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.rest.api.leaguetracker.restapileaguetracker.controllers.PlayerController;
import com.rest.api.leaguetracker.restapileaguetracker.objects.Player;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public
class PlayerResourceAssembler implements ResourceAssembler<Player, Resource<Player>> {

    @Override
    public Resource<Player> toResource(Player player) {

        return new Resource<>(player,
                linkTo(methodOn(PlayerController.class).one(player.getId())).withSelfRel(),
                linkTo(methodOn(PlayerController.class).all()).withRel("players"));
    }
}