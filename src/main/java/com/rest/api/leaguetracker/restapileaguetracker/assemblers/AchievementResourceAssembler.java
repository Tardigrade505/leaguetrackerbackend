package com.rest.api.leaguetracker.restapileaguetracker.assemblers;

import com.rest.api.leaguetracker.restapileaguetracker.controllers.AchievementController;
import com.rest.api.leaguetracker.restapileaguetracker.objects.Achievement;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class AchievementResourceAssembler implements ResourceAssembler<Achievement, Resource<Achievement>> {

    @Override
    public Resource<Achievement> toResource(Achievement achievement) {

        return new Resource<>(achievement,
                linkTo(methodOn(AchievementController.class).one(achievement.getId())).withSelfRel(),
                linkTo(methodOn(AchievementController.class).all()).withRel("achievements"));
    }
}