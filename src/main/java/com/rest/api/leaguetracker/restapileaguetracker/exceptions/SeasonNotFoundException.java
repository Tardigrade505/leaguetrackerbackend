package com.rest.api.leaguetracker.restapileaguetracker.exceptions;

public class SeasonNotFoundException extends RuntimeException {

    public SeasonNotFoundException(Long id) {
        super("Could not find season " + id);
    }
}