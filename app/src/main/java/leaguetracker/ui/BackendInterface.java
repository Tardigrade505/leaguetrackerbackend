package leaguetracker.ui;

import leaguetracker.backend.handler.LeagueTrackerAPI;
import leaguetracker.backend.objects.GameResults;
import leaguetracker.ui.screencomponents.UserAction;
import leaguetracker.ui.screens.SeasonMainMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * A handler class that translates requested UI functionality into backend API calls
 */
public class BackendInterface {
    /**
     * An instance of the backend API
     */
    private LeagueTrackerAPI leagueTrackerAPI;

    public BackendInterface() {
        this.leagueTrackerAPI = new LeagueTrackerAPI();
    }

    /**
     * Queries the backend API to get a list of the season, and creates
     * a list of user actions from that list
     * @return a list of user actions
     */
    public List<UserAction> createUserActionsFromExistingSeasons() {
        List<UserAction> userActions = new ArrayList<>();

        // Get a list of all the seasons
        List<String> allSeasons = leagueTrackerAPI.getAllSeasons();

        // Add each season to the list of user actions with an attached season main menu screen
        for (String season : allSeasons) {
            userActions.add(new UserAction(season, new SeasonMainMenu(season)));
        }
        return userActions;
    }

    /**
     * Get a list of all the players in the season
     * @param seasonName - the name of the season
     * @return a list of all the players in the season
     */
    public List<String> getPlayerList(final String seasonName) {
        return leagueTrackerAPI.getAllPlayers(seasonName);
    }

    /**
     * Creates a new season
     * @param seasonName - the name of the season
     * @param players - a list of players in the season
     * @param achievementFile - the path to the achievement file
     */
    public void createNewSeason(final String seasonName, final List<String> players, final String achievementFile) {
        leagueTrackerAPI.createNewSeason(seasonName, players, achievementFile);
    }

    /**
     * Records the results of a game
     * @param seasonName - the name of the season
     * @param gameResults - results of the game to be recorded
     * @return a boolean representing whether or not the results were recorded
     */
    public boolean recordGameResults(final String seasonName, final GameResults gameResults) {
        return leagueTrackerAPI.recordGameResults(seasonName, gameResults);
    }

    /**
     * Queries the backend database for the current standings for the given season
     * @param seasonName - the name of the season
     * @return the standings
     */
    public String getStandings(final String seasonName) {
        return leagueTrackerAPI.getStandings(seasonName);
    }


}
