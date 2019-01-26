package com.rest.api.leaguetracker.restapileaguetracker.repositories;

import com.rest.api.leaguetracker.restapileaguetracker.objects.Season;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonRepository extends JpaRepository<Season, Long> {

}