package leaguetracker.database;

import leaguetracker.database.util.DatabaseUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test the functionality of the SeasonDatabase
 */
public class SeasonDatabaseTest extends DatabaseTest {
    private static final String TEST_BASE_PATH = DatabaseUtils.getProjectBasePath() + "app/src/test/testDir/";

    private FileHandler fileHandler = new FileHandler();

    @Before
    public void createTestDir() {
        Assert.assertTrue("Failed to create test dir. Tests cannot run.", new File(TEST_BASE_PATH).mkdir());
    }

    @After
    public void cleanupTestDir() {
        try {
            Assert.assertTrue("Failed to clean up test dir. Tests cannot run.", deleteDirectory(new File(TEST_BASE_PATH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests the database ability to select a row by a key
     */
    @Test
    public void testDatabaseSelectRow() {
        // Set up test by creating a file with some content in it
        FileHandler fileHandler = new FileHandler();
        final String testFilePath = TEST_BASE_PATH + "testDatabaseSelectRow";
        Assert.assertTrue("Failed to create new file for test.",fileHandler.createNewFile(testFilePath));
        List<String> toWrite = new ArrayList<>();
        toWrite.add("testKey1,column1,column2");
        final String selectRow = "testKey2,column1,column2";
        toWrite.add(selectRow);
        Assert.assertTrue("Failed to write to file.", fileHandler.writeToFile(testFilePath, toWrite));

        // Test select row
        SeasonDatabase database = SeasonDatabase.getInstance();
        List<String> expectedRow = Arrays.asList(selectRow.split(","));
        Assert.assertEquals("Expected row and selected row do not match.", expectedRow, database.selectRow("testKey2", testFilePath));
    }

    /**
     * Tests the database ability to replace a row by a key
     */
    @Test
    public void testDatabaseReplaceRow() {
        // Set up test by creating a file with some content in it
        FileHandler fileHandler = new FileHandler();
        final String testFilePath = TEST_BASE_PATH + "testDatabaseReplaceRow";
        Assert.assertTrue("Failed to create new file for test.",fileHandler.createNewFile(testFilePath));
        List<String> toWrite = new ArrayList<>();
        toWrite.add("testKey1,column1,column2");
        final String selectRow = "testKey2,column1,column2";
        toWrite.add(selectRow);
        Assert.assertTrue("Failed to write to file.", fileHandler.writeToFile(testFilePath, toWrite));

        // Test replace row
        SeasonDatabase database = SeasonDatabase.getInstance();
        final List<String> replaceRow = Arrays.asList("testKey2,replaced,replaced2".split(","));
        Assert.assertTrue("Bad boolean returned when replacing row.", database.replaceRow("testKey2", replaceRow, testFilePath));
        Assert.assertEquals("Expected row and replaced row do not match.", replaceRow, database.selectRow("testKey2", testFilePath));
    }

    /**
     * Test the creation of a season and all the associated files
     */
    @Test
    public void testCreateSeason() {
        // Set the base path of the 2 databases to use the test base path
        SeasonDatabase seasonDatabase = SeasonDatabase.getInstance();
        seasonDatabase.setBasePath(TEST_BASE_PATH);
        PlayerDatabase playerDatabase = PlayerDatabase.getInstance();
        playerDatabase.setBasePath(TEST_BASE_PATH);
        seasonDatabase.setPlayerDatabase(playerDatabase);

        // Create a new season with 2 players
        final String seasonName = "testCreateSeason";
        List<String> playerList = new ArrayList<>();
        playerList.add("player1");
        playerList.add("player2");
        seasonDatabase.createSeason(seasonName, playerList, "default");

        // Validate that all files associated with the season were created
        Assert.assertTrue("Failed to create top-level directory for new season.",
                fileHandler.directoryExists(TEST_BASE_PATH + seasonName));
        Assert.assertTrue("Failed to create seasons.txt file.",
                fileHandler.fileExists(TEST_BASE_PATH + DataModelNames.SEASON_LIST));
        List<String> seasons = fileHandler.readFile(TEST_BASE_PATH + DataModelNames.SEASON_LIST);
        Assert.assertEquals("Unexpected number of seasons found in seasons.txt.", 1, seasons.size());
        Assert.assertEquals("Season name in seasons.txt does not match.", seasonName, seasons.get(0));
        Assert.assertTrue("Failed to create playerList.txt file.",
                fileHandler.fileExists(TEST_BASE_PATH + seasonName + "/" + DataModelNames.PLAYER_LIST));
        List<String> players = fileHandler.readFile(TEST_BASE_PATH + seasonName + "/" + DataModelNames.PLAYER_LIST);
        Assert.assertEquals("Unexpected number of players found in playerList.txt.", 2, players.size());
        Assert.assertEquals("Player names in playerList.txt do not match expected.", playerList, players);
    }

}
