package leaguetracker.backend.controller;

import leaguetracker.backend.LeagueConstants;
import leaguetracker.backend.objects.Achievement;
import leaguetracker.backend.objects.GameResults;
import leaguetracker.database.PlayerDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PlayerController {
    /**
     * An object that handles interacting with player data
     */
    private PlayerDatabase playerDatabase;

    public PlayerController() {
        this.playerDatabase = PlayerDatabase.getInstance();
    }

    public List<String> getAllPlayerNames(final String seasonName) {
        return playerDatabase.readAllPlayers(seasonName);
    }

    /**
     * Records the player results in the database for a given game
     * @param seasonName - the name of the season
     * @param gameResults - the game results to be recorded
     * @return a boolean representing whether or not the results were recorded
     */
    public boolean recordPlayerResults(final String seasonName, final GameResults gameResults) {
        // Compile all the points for each player
        List<String> currentRow;
        for (String playerName : gameResults.getParticipants()) {
            currentRow = new ArrayList<>();

            int pointsFromFirst = (gameResults.getWinners().contains(playerName)) ? LeagueConstants.POINTS_PER_WIN : 0;
            int pointsFromSecond = (gameResults.getSeconders().contains(playerName)) ? LeagueConstants.POINTS_FOR_SECOND : 0;

            // Gather achievement points
            int pointsFromAchievements = 0;
            int dollarsFromAchievements = 0;
            HashMap<Achievement, String> achievementsAndWinners = gameResults.getAchievementWinners();
            if (achievementsAndWinners.containsValue(playerName)) {
                for (Achievement achievement : achievementsAndWinners.keySet()) {
                    if (playerName.equals(achievementsAndWinners.get(achievement))) {
                        pointsFromAchievements += achievement.getPointsValue();
                        dollarsFromAchievements += achievement.getDollarValue();
                    }
                }
            }
            currentRow.add(playerName); // Row key (player name)
            currentRow.add(String.valueOf(pointsFromFirst+pointsFromSecond+pointsFromAchievements+LeagueConstants.POINTS_FOR_PARTICIPATION)); // (total points)
            currentRow.add(String.valueOf(pointsFromFirst)); // (Points from first)
            currentRow.add(String.valueOf(pointsFromSecond)); // (Points from second)
            currentRow.add(String.valueOf(pointsFromAchievements)); // (Points from achievements)
            currentRow.add(String.valueOf(LeagueConstants.POINTS_FOR_PARTICIPATION)); // (Points from participation)
            currentRow.add(String.valueOf(dollarsFromAchievements)); // (Dollars from achievements)

            // Write the row
            if (!playerDatabase.updatePlayerRow(seasonName, currentRow)) return false;
        }
        return true;
    }

    /**
     * Queries the database for the current standings of the season
     * @param seasonName - the name of the season
     * @return the standings
     */
    public String getPlayerStandings(final String seasonName) {
        // Get all the player info from the database
        List<String> allPlayersWithPoints = playerDatabase.selectAllPlayers(seasonName);

        // Grab the name and total points only
        List<PlayerStandingEntry> playerAndPoints = new ArrayList<>();
        for (String row : allPlayersWithPoints) {
            String[] rowSplit = row.split(",");
            playerAndPoints.add(new PlayerStandingEntry(rowSplit[0], Integer.parseInt(rowSplit[1])));
        }

        // Sort the entries of the map by most points to least
        Collections.sort(playerAndPoints);

        // Convert the list to string with newlines
        StringBuilder playerStandings = new StringBuilder();
        for (PlayerStandingEntry playerStandingEntry : playerAndPoints) {
            playerStandings.append(playerStandingEntry.getPlayerName()).append(", ").append(playerStandingEntry.getTotalPoints()).append(" points\n");
        }
        return playerStandings.toString();
    }


    /**
     * A wrapper class that combines player name and points into a
     * leader board entry.
     */
    class PlayerStandingEntry implements Comparable<PlayerStandingEntry> {
        private String playerName;
        private int totalPoints;

        PlayerStandingEntry(String playerName, int totalPoints) {
            this.playerName = playerName;
            this.totalPoints = totalPoints;
        }

        String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        int getTotalPoints() {
            return totalPoints;
        }

        public void setTotalPoints(int totalPoints) {
            this.totalPoints = totalPoints;
        }

        @Override
        public int compareTo(PlayerStandingEntry o) {
            return Integer.compare(totalPoints, o.getTotalPoints()) * -1;
        }
    }
}

