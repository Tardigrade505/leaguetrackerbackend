package com.rest.api.leaguetracker.restapileaguetracker.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.rest.api.leaguetracker.restapileaguetracker.controllers.SeasonController;
import com.rest.api.leaguetracker.restapileaguetracker.objects.Season;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public
class SeasonResourceAssembler implements ResourceAssembler<Season, Resource<Season>> {

    @Override
    public Resource<Season> toResource(Season season) {

        return new Resource<>(season,
                linkTo(methodOn(SeasonController.class).one(season.getId())).withSelfRel(),
                linkTo(methodOn(SeasonController.class).all()).withRel("seasons"));
    }
}