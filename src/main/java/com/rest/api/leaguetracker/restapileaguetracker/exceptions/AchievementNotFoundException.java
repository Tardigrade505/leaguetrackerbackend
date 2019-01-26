package com.rest.api.leaguetracker.restapileaguetracker.exceptions;

public class AchievementNotFoundException extends RuntimeException {

    public AchievementNotFoundException(Long id) {
        super("Could not find achievement " + id);
    }
}