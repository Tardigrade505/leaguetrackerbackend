package leaguetracker.backend.controller;

import leaguetracker.backend.util.BackendUtils;
import leaguetracker.database.DataModelNames;
import leaguetracker.database.SeasonDatabase;

import java.util.List;

/**
 * Handles business logic related to fulfilling API requests related to seasons
 */
public class SeasonController {
    private static final String DEFAULT_ACHIEVEMENT_LOCATION = BackendUtils.getProjectBasePath() + "app/src/main/resources/" + DataModelNames.ACHIEVEMENTS_FILE;

    /**
     * An object handles interacting with the DataBaseAPI
     */
    private SeasonDatabase seasonDatabase;

    public SeasonController() {
        this.seasonDatabase = SeasonDatabase.getInstance();
    }

    /**
     * Gets all season names
     * @return a list of all the names of the seasons
     */
    public List<String> getAllSeasonNames() {
        return seasonDatabase.selectAllSeasons();
    }

    /**
     * Creates a new season with a name, player list, and a reference to an achievemnts file
     * @param seasonName - the name of the season
     * @param players - the list of players in the season
     * @param achievementFile - the full path to the achievements file
     * @return
     */
    public boolean createNewSeason(final String seasonName, final List<String> players, final String achievementFile) {
        if ("default".equals(achievementFile)) {
            return seasonDatabase.createSeason(seasonName, players, DEFAULT_ACHIEVEMENT_LOCATION);
        }
        return seasonDatabase.createSeason(seasonName, players, achievementFile);
    }
}
