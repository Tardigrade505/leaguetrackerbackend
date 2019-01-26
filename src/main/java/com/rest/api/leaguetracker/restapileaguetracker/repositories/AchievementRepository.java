package com.rest.api.leaguetracker.restapileaguetracker.repositories;

import com.rest.api.leaguetracker.restapileaguetracker.objects.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {

}