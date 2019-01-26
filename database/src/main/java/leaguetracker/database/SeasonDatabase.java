package leaguetracker.database;

import java.util.List;

/**
 * A database that manages the SCRUD operation on Season objects
 */
public class SeasonDatabase extends Database {
    /**
     * An instance of the playerDatabase for performing actions related to players
     */
    private PlayerDatabase playerDatabase;

    /**
     * Instance of itself for singleton design pattern
     */
    private static SeasonDatabase instance;

    /**
     * Return an instance of the SeasonDatabase
     * Used for singleton design pattern
     * @return an instance of SeasonDatabase
     */
    public static SeasonDatabase getInstance() {
        if (null == instance) {
            instance = new SeasonDatabase();
        }
        return instance;
    }

    private SeasonDatabase() {
        super();
        this.playerDatabase = PlayerDatabase.getInstance();
    }

    /**
     * Returns the selected season object
     * @param seasonName - the name of the season
     * @return the season
     */
    public String selectSeason(final String seasonName) {
        return null;
    }

    /**
     * Returns all the seasons
     * @return a list of all the seasons
     */
    public List<String> selectAllSeasons() {
        return fileHandler.readFile(basePath + DataModelNames.SEASON_LIST);
    }

    /**
     * Creates and stores a new season object in the database
     * @param name - the name of the season
     * @param playerList - a list of player names in the season
     * @param achievementsFile - the full path to a file containing the list of achievements
     * @return a boolean representing whether or not the season was created
     */
    public boolean createSeason(final String name, final List<String> playerList, final String achievementsFile) {
        // Create the top level season directory
        final boolean createdSeasonDir = fileHandler.createNewDir(basePath + name);

        // Append the new season to the seasons.txt file if it exists, otherwise create it
        boolean wroteToFile;
        if (fileHandler.fileExists(basePath + DataModelNames.SEASON_LIST)) {
            wroteToFile = fileHandler.appendToFile(basePath + DataModelNames.SEASON_LIST, name);
        } else {
            wroteToFile = fileHandler.writeToFile(basePath + DataModelNames.SEASON_LIST, name);
        }

        // Create the playerList.txt file for this season
        final boolean createdPlayerList = fileHandler.createNewFile(basePath + name + "/" + DataModelNames.PLAYER_LIST);

        // Add all the players in the player list to the season
        for (String playerName: playerList) {
            if (!playerDatabase.createPlayer(name, playerName)) return false; // If you fail to create a player, return false
        }
        return (createdSeasonDir && wroteToFile && createdPlayerList);


    }

    public String readSeason(final String seasonName) {
        return null;
    }

    public boolean updateSeason() {
        return false;
    }

    public boolean deleteSeason() {
        return false;
    }

    public void setPlayerDatabase(PlayerDatabase playerDatabase) {
        this.playerDatabase = playerDatabase;
    }
}
