package com.rest.api.leaguetracker.restapileaguetracker.repositories;

import com.rest.api.leaguetracker.restapileaguetracker.objects.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {

}