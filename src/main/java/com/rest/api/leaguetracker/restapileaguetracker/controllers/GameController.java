package com.rest.api.leaguetracker.restapileaguetracker.controllers;

import com.rest.api.leaguetracker.restapileaguetracker.LeagueConstants;
import com.rest.api.leaguetracker.restapileaguetracker.assemblers.GameResourceAssembler;
import com.rest.api.leaguetracker.restapileaguetracker.exceptions.GameNotFoundException;
import com.rest.api.leaguetracker.restapileaguetracker.objects.Achievement;
import com.rest.api.leaguetracker.restapileaguetracker.objects.Game;
import com.rest.api.leaguetracker.restapileaguetracker.objects.Player;
import com.rest.api.leaguetracker.restapileaguetracker.objects.helper.GameResults;
import com.rest.api.leaguetracker.restapileaguetracker.repositories.GameRepository;
import com.rest.api.leaguetracker.restapileaguetracker.util.BackendUtils;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

@RestController
public class GameController {
    private final GameRepository repository;
    private final GameResourceAssembler assembler;
    private final PlayerController playerController;
    private final SeasonController seasonController;

    GameController(GameRepository repository, GameResourceAssembler assembler, PlayerController playerController, SeasonController seasonController) {
        this.repository = repository;
        this.assembler = assembler;
        this.playerController = playerController;
        this.seasonController = seasonController;
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/games/results")
    public ResponseEntity<Resource<Game>> postResults(@RequestBody GameResults results) throws URISyntaxException {
        System.out.println("Storing results");
        System.out.println("Season ID = " + results.getSeasonId());
        System.out.println("JSON received = " + results);
        Player currentPlayer;
        // Update the winners
        for (String player: results.getWinners()) {
            System.out.println("Going through winners");
            // Get the player and update their points value
            currentPlayer = findPlayerByName(player, results.getSeasonId());
            System.out.println("Got current player");
            System.out.println("current player = " + currentPlayer);

            // Update and replace the playerController with the updated points value
            assert currentPlayer != null;
            currentPlayer.setTotalPoints(currentPlayer.getTotalPoints() + LeagueConstants.POINTS_FOR_WIN);
            this.playerController.replacePlayer(currentPlayer, currentPlayer.getId());
        }

        // Update the seconders
        for (String player: results.getSeconders()) {
            System.out.println("Going through seconders");
            // Get the player and update their points value
            currentPlayer = findPlayerByName(player, results.getSeasonId());

            // Update and replace the playerController with the updated points value
            assert currentPlayer != null;
            currentPlayer.setTotalPoints(currentPlayer.getTotalPoints() + LeagueConstants.POINTS_FOR_SECOND);
            this.playerController.replacePlayer(currentPlayer, currentPlayer.getId());
        }

        // Update the achievement winners
        List<Achievement> achievements = Achievement.importAchievements(
                BackendUtils.getProjectBasePath() + "src/main/resources/achievements.txt");
        for (String achievement : results.getAchievementWinners().keySet()) {
            System.out.println("Going through cheevos");
            if (!"none".equals(results.getAchievementWinners().get(achievement))) {
                System.out.println("Winner wasnt none");
                // Get the player and update their points value
                currentPlayer = findPlayerByName(results.getAchievementWinners().get(achievement), results.getSeasonId());

                // Update and replace the player with the updated points value
                for (Achievement achievementObject : achievements) {
                    if (achievement.equals(achievementObject.getName())) {
                        System.out.println("Current player = " + currentPlayer);
                        System.out.println("AchievementObject = " + achievementObject);
                        currentPlayer.setTotalPoints(currentPlayer.getTotalPoints() + achievementObject.getPointsValue());
                        this.playerController.replacePlayer(currentPlayer, currentPlayer.getId());
                    }
                }
            }
        }

        // Update the participants
        for (String participantName : results.getParticipants()) {
            System.out.println("1");
            currentPlayer = findPlayerByName(participantName, results.getSeasonId());
            System.out.println("2");
            currentPlayer.setTotalPoints(currentPlayer.getTotalPoints() + LeagueConstants.POINTS_FOR_PARTICIPATION);
            System.out.println("3");
            playerController.replacePlayer(currentPlayer, currentPlayer.getId());
            System.out.println("4");
        }


        // TODO: fix
        return null;
//        Resource<Game> resource = assembler.toResource(repository.save(Game.generateNewGame(results.getSeasonId(), Collections.emptyList())));
//        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    // Aggregate root
    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/games")
    public Resource<Game> getNewGame(@RequestParam int seasonId, @RequestParam List<String> playerList) {
        Game game = Game.generateNewGame(seasonId, playerList);

        return  assembler.toResource(game);
    }

    // Single item

    @GetMapping("/games/{id}")
    public Resource<Game> one(@PathVariable Long id) {

        Game game =  repository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));

        return assembler.toResource(game);
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/games")
    public ResponseEntity<Resource<Game>> newGame(@RequestBody Game newGame) throws URISyntaxException {
        Resource<Game> resource = assembler.toResource(repository.save(newGame));

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    private Player findPlayerByName(final String playerName, final int seasonId) {
        System.out.println("Finding player by name: " + playerName + " and season Id = " + seasonId);
        Resources<Player> playersInSeason = seasonController.allPlayers((long) seasonId);

        for (Player player : playersInSeason) {
            System.out.println("Player = " + player);
            if (playerName.equals(player.getName())) {
                return player;
            }
        }
        System.out.println("Failed to find playerController with name: " + playerName);
        return null;
    }
}
