package com.rest.api.leaguetracker.restapileaguetracker.advice;

import com.rest.api.leaguetracker.restapileaguetracker.exceptions.AchievementNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class AchievementNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(AchievementNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String achievementNotFoundHandler(AchievementNotFoundException ex) {
        return ex.getMessage();
    }
}