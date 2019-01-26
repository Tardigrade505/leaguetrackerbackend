package leaguetracker.backend.handler;

import leaguetracker.backend.controller.AchievementController;
import leaguetracker.backend.controller.PlayerController;
import leaguetracker.backend.controller.SeasonController;
import leaguetracker.backend.objects.GameResults;

import java.util.List;

/**
 * This class defines and exposes all functionality of the League Tracker backend to the UI
 */
public class LeagueTrackerAPI {
    /**
     * A controller that handles requests that have to do with first class citizen directories
     */
    private SeasonController seasonController;

    /**
     * A controller that handles requests that have to do with first class citizen players
     */
    private PlayerController playerController;

    /**
     * A controller that handles requests that have to do with first class citizen achievements
     */
    private AchievementController achievementController;

    public LeagueTrackerAPI() {
        this.seasonController = new SeasonController();
        this.playerController = new PlayerController();
        this.achievementController = new AchievementController();
    }

    /**
     * Gets a list of all the season directories
     * @return a list of strings containing all the names of the seasons
     */
    public List<String> getAllSeasons() {
        return seasonController.getAllSeasonNames();
    }

    /**
     * Get a list of all the players in the season
     * @param seasonName - the name of the season
     * @return a list of all the players in the season
     */
    public List<String> getAllPlayers(final String seasonName) {
        return playerController.getAllPlayerNames(seasonName);
    }

    /**
     * Creates a new season
     * @param seasonName
     * @param players - list of players in the season
     * @param achievementFile - full path to the achievement file
     */
    public void createNewSeason(final String seasonName, final List<String> players, final String achievementFile) {
        seasonController.createNewSeason(seasonName, players, achievementFile);
    }

    /**
     * Records the results of the given game in the databases
     * @param seasonName - the name of the season
     * @param gameResults - the results of the game
     * @return a boolean representing whether or not the recording was successful
     */
    public boolean recordGameResults(final String seasonName, final GameResults gameResults) {
        final boolean playerRecordSuccess = playerController.recordPlayerResults(seasonName, gameResults);
        final boolean achievementRecordSuccess = achievementController.recordAchievementResults(seasonName, gameResults.getAchievementWinners());
        return playerRecordSuccess && achievementRecordSuccess;
    }

    public String getStandings(final String seasonName) {
        return playerController.getPlayerStandings(seasonName);
    }

}
