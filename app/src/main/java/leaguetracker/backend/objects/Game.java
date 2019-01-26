package leaguetracker.backend.objects;

import leaguetracker.backend.util.BackendUtils;
import leaguetracker.database.DataModelNames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    /**
     * The name of the season that this game is in
     */
    private String seasonName;

    /**
     * A list of lists of players at tables
     */
    private List<List<String>> tables;

    /**
     * A list of achievements for the game
     */
    private List<Achievement> achievements;

    /**
     * The bonus achievement for the player in last place
     */
    private Achievement bonusAchievement;

    public Game(String seasonName, List<List<String>> tables, List<Achievement> achievements, Achievement bonusAchievement) {
        this.seasonName = seasonName;
        this.tables = tables;
        this.achievements = achievements;
        this.bonusAchievement = bonusAchievement;
    }

    /**
     * Generates a new game object
     * @param seasonName - the name of the season
     * @param playersInGame - the players in the game
     * @return
     */
    public static Game generateNewGame(final String seasonName, final List<String> playersInGame) {
        String pathToAchievementsFile = BackendUtils.getProjectBasePath() + "app/src/main/resources/" + DataModelNames.ACHIEVEMENTS_FILE;
        List<List<String>> tables = determineTables(playersInGame);
        List<Achievement> achievements = Achievement.generateAchievements(3, pathToAchievementsFile); // TODO: make achievements file name not hard-coded
        Achievement bonusAchievement = Achievement.generateBonusAchievement(achievements, pathToAchievementsFile);
        return new Game(seasonName, tables, achievements, bonusAchievement);
    }

    /**
     * Given a list of players returns a list of lists of players at tables
     * @param players - the list of players
     * @return list of lists of players at tables
     */
    private static List<List<String>> determineTables(List<String> players) {
        List<List<String>> tables = new ArrayList<>();

        List<Integer> tableGroupings = findTableGroupings(players.size());
        Collections.shuffle(players);

        for (Integer numberOfPlayersAtTable : tableGroupings) {
            List<String> currentTable = players.subList(0, numberOfPlayersAtTable);
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

    public String getSeasonName() {
        return seasonName;
    }

    public List<List<String>> getTables() {
        return tables;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public Achievement getBonusAchievement() {
        return bonusAchievement;
    }
}
