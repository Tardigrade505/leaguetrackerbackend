package leaguetracker.database;

import java.util.List;

/**
 * A database that manages the SCRUD operations on Player objects
 */
public class PlayerDatabase extends Database {
    /**
     * Instance of itself for singleton design pattern
     */
    private static PlayerDatabase instance;

    /**
     * Return an instance of the PlayerDatabase
     * Used for singleton design pattern
     * @return an instance of PlayerDatabase
     */
    public static PlayerDatabase getInstance() {
        if (null == instance) {
            instance = new PlayerDatabase();
        }
        return instance;
    }

    private PlayerDatabase() {
        super();
    }

    /**
     * Create a new player in the given season
     * @param seasonName - the name of the season
     * @return a boolean representing whether or not the player was created
     */
    public boolean createPlayer(final String seasonName, final String playerName) {
        // Create the playerPoints.txt file if it does not exist
        final String playerPointsPath = basePath + seasonName + "/" + DataModelNames.PLAYER_POINTS;
        if (!fileHandler.fileExists(playerPointsPath)) {
            if (!fileHandler.createNewFile(playerPointsPath)) return false;
        }

        // Append a new player entry to the playerPoints.txt file
        final String newPlayerEntryRow = playerName + "," + "0,0,0,0,0,0";
        if (!fileHandler.appendToFile(playerPointsPath, newPlayerEntryRow)) return false;


        // Append the player name to the playerList.txt file
        if (!fileHandler.appendToFile(basePath + seasonName + "/" + DataModelNames.PLAYER_LIST, playerName)) return false;
        return true;
    }

    /**
     * Returns a list of all the players in the season
     * @param seasonName - the name of the season
     * @return a list of all the players in the season
     */
    public List<String> readAllPlayers(final String seasonName) {
        return fileHandler.readFile(basePath + seasonName + "/" + DataModelNames.PLAYER_LIST);
    }

    public boolean updatePlayerRow(final String seasonName, List<String> row) {
        return addToRow(row.get(0), row, basePath + seasonName + "/" + DataModelNames.PLAYER_POINTS);
    }

    public List<String> selectAllPlayers(final String seasonName) {
        return fileHandler.readFile(basePath + seasonName + "/" + DataModelNames.PLAYER_POINTS);
    }
}
