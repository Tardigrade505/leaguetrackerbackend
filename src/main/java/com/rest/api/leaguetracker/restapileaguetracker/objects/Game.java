package com.rest.api.leaguetracker.restapileaguetracker.objects;

import com.rest.api.leaguetracker.restapileaguetracker.util.BackendUtils;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@Table(name = "GAME")
public class Game {

    private @Id @GeneratedValue Long id;

    /**
     * The name of the season that this game is in
     */
    private int seasonId;

    /**
     * A list of lists of players at tables
     */
    @OneToMany(targetEntity = PlayerTable.class, mappedBy = "players", cascade = {CascadeType.ALL})
    private List<PlayerTable> tables;

    /**
     * A list of achievements for the game
     */
    @OneToMany(targetEntity = Achievement.class, mappedBy = "name", cascade = {CascadeType.ALL})
    private List<Achievement> achievements;

    /**
     * The bonus achievement for the player in last place
     */
//    private Achievement bonusAchievement;

    public Game(int seasonId, List<PlayerTable> tables, List<Achievement> achievements, Achievement bonusAchievement) {
        this.seasonId = seasonId;
        this.tables = tables;
        this.achievements = achievements;
//        this.bonusAchievement = bonusAchievement;
    }

    /**
     * Generates a new game object
     * @param seasonId - the name of the season
     * @param playersInGame - the players in the game
     * @return
     */
    public static Game generateNewGame(final int seasonId, final List<String> playersInGame) {
        String pathToAchievementsFile = BackendUtils.getProjectBasePath() + "src/main/resources/achievements.txt";
        List<PlayerTable> tables = determineTables(playersInGame);
        List<Achievement> achievements = Achievement.generateAchievements(3, pathToAchievementsFile); // TODO: make achievements file name not hard-coded
        Achievement bonusAchievement = Achievement.generateBonusAchievement(achievements, pathToAchievementsFile);
        achievements.add(bonusAchievement);
        return new Game(seasonId, tables, achievements, bonusAchievement);
    }

    /**
     * Given a list of players returns a list of lists of players at tables
     * @param players - the list of players
     * @return list of lists of players at tables
     */
    private static List<PlayerTable> determineTables(List<String> players) {
        List<PlayerTable> tables = new ArrayList<>();

        List<Integer> tableGroupings = findTableGroupings(players.size());
        Collections.shuffle(players);

        for (Integer numberOfPlayersAtTable : tableGroupings) {
            PlayerTable currentTable = new PlayerTable(players.subList(0, numberOfPlayersAtTable));
            players = players.subList(numberOfPlayersAtTable, players.size()); // Remove players already assigned to a table
            tables.add(currentTable);
        }
        return tables;
    }

    /**
     * Given a number of players returns a list of Integers representing the breakdown of players
     * per table, prioritizing making tables of 4 and having no tables with fewer than 3 players
     * @param numberOfPlayers - the number og players
     * @return a list of integers representing number of players at each table
     */
    private static List<Integer> findTableGroupings(int numberOfPlayers) {
        return findTableGroupingsHelper(numberOfPlayers, new ArrayList<>());
    }

    /**
     * Helper method for findTableGroupings that does the recursive work
     * @param numberOfPlayers - the number of players
     * @param groupings - the list of table groupings
     * @return
     */
    private static List<Integer> findTableGroupingsHelper(int numberOfPlayers, List<Integer> groupings) {
        // Base case
        if (0 == numberOfPlayers) {
            return groupings;
        }

        // Exception case
        if (5 == numberOfPlayers) {
            groupings.add(5);
            return groupings;
        }

        // Other exception case
        if (numberOfPlayers < 4) {
            groupings.add(numberOfPlayers);
            return groupings;
        }

        int modValue = numberOfPlayers % 4;

        // Case where it evenly divides by 4
        if (0 == modValue) {
            for (int i = 0; i < numberOfPlayers/4; i++) {
                groupings.add(4);
            }
            return groupings;
        }

        // Case where it divides with a remainder of 3
        if (3 == modValue) {
            for (int i = 0; i < numberOfPlayers/4; i++) {
                groupings.add(4);
            }
            groupings.add(3);
            return groupings;
        }

        // Else
        groupings.add(3);
        return findTableGroupingsHelper(numberOfPlayers - 3, groupings);
    }

   Game() {}
}
