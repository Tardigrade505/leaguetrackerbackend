package com.rest.api.leaguetracker.restapileaguetracker.advice;

import com.rest.api.leaguetracker.restapileaguetracker.exceptions.SeasonNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class SeasonNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(SeasonNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String seasonNotFoundHandler(SeasonNotFoundException ex) {
        return ex.getMessage();
    }
}