package com.rest.api.leaguetracker.restapileaguetracker.controllers;

import com.rest.api.leaguetracker.restapileaguetracker.LeagueConstants;
import com.rest.api.leaguetracker.restapileaguetracker.assemblers.GameResourceAssembler;
import com.rest.api.leaguetracker.restapileaguetracker.exceptions.GameNotFoundException;
import com.rest.api.leaguetracker.restapileaguetracker.objects.Achievement;
import com.rest.api.leaguetracker.restapileaguetracker.objects.Game;
import com.rest.api.leaguetracker.restapileaguetracker.objects.Player;
import com.rest.api.leaguetracker.restapileaguetracker.objects.helper.GameResults;
import com.rest.api.leaguetracker.restapileaguetracker.objects.helper.TableResult;
import com.rest.api.leaguetracker.restapileaguetracker.repositories.GameRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@CrossOrigin
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

    @PostMapping("/games/results")
    public ResponseEntity<Resource<Game>> postResults(@RequestBody GameResults gameResults) throws URISyntaxException {
        System.out.println("Storing results");
        System.out.println("Season ID = " + gameResults.getSeasonId());
        System.out.println("JSON received = " + gameResults);

        for (TableResult result : gameResults.getTableResults()) {
            // Update the winners
            // Get the player and update their points value
            Player currentPlayer = findPlayerByName(result.getWinner(), gameResults.getSeasonId());
            System.out.println("Got current player");
            System.out.println("current player = " + currentPlayer);

            // Update and replace the player with the updated points value
            currentPlayer.setTotalPoints(currentPlayer.getTotalPoints() + LeagueConstants.POINTS_FOR_WIN);
            this.playerController.replacePlayer(currentPlayer, currentPlayer.getId());

            // Update the seconders
            for (String player : result.getSeconders()) {
                // Get the player and update their points value
                currentPlayer = findPlayerByName(player, gameResults.getSeasonId());

                // Update and replace the playerController with the updated points value
                currentPlayer.setTotalPoints(currentPlayer.getTotalPoints() + LeagueConstants.POINTS_FOR_SECOND);
                this.playerController.replacePlayer(currentPlayer, currentPlayer.getId());
            }

            // Update the achievement winners
            List<Achievement> achievements = new Achievement().importAchievements("achievements.txt");
            for (String achievement : result.getAchievementWinners().keySet()) {
                if (!"none".equals(result.getAchievementWinners().get(achievement))) {

                    // Get the player and update their points value
                    currentPlayer = findPlayerByName(result.getAchievementWinners().get(achievement), gameResults.getSeasonId());

                    // Update and replace the player with the updated points value
                    for (Achievement achievementObject : achievements) {
                        if (achievement.equals(achievementObject.getName())) {
                            currentPlayer.setTotalPoints(currentPlayer.getTotalPoints() + achievementObject.getPointsValue());
                            currentPlayer.setBonusMoney(currentPlayer.getBonusMoney() + achievementObject.getDollarValue());
                            this.playerController.replacePlayer(currentPlayer, currentPlayer.getId());
                        }
                    }
                }
            }

            // Update the participants
            for (String participantName : result.getParticipants()) {
                currentPlayer = findPlayerByName(participantName, gameResults.getSeasonId());
                currentPlayer.setTotalPoints(currentPlayer.getTotalPoints() + LeagueConstants.POINTS_FOR_PARTICIPATION);
                playerController.replacePlayer(currentPlayer, currentPlayer.getId());
            }
        }


        // TODO: fix
        return null;
//        Resource<Game> resource = assembler.toResource(repository.save(Game.generateNewGame(results.getSeasonId(), Collections.emptyList())));
//        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    // Aggregate root
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
