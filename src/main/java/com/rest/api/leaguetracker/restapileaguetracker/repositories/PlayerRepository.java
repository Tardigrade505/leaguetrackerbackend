package com.rest.api.leaguetracker.restapileaguetracker.repositories;

import com.rest.api.leaguetracker.restapileaguetracker.objects.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}