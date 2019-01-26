package leaguetracker.database;

import leaguetracker.database.util.DatabaseUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Test the functionality of the SeasonDatabase
 */
public class PlayerDatabaseTest extends DatabaseTest {
    private static final String TEST_BASE_PATH = DatabaseUtils.getProjectBasePath() + "app/src/test/testDir/";
    private static final String SEASON_NAME = "PlayerDatabaseTest";

    @Before
    public void createTestDirAndTestSeason() {
        Assert.assertTrue("Failed to create test dir. Tests cannot run.", new File(TEST_BASE_PATH).mkdir());

        SeasonDatabase seasonDatabase = SeasonDatabase.getInstance();
        seasonDatabase.setBasePath(TEST_BASE_PATH);
        Assert.assertTrue("Failed to create Test season. Tests cannot run.",
                seasonDatabase.createSeason(SEASON_NAME, new ArrayList<String>(), "cheevo.txt"));
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
     * Tests creating a player and that the associated files get created
     */
    @Test
    public void testCreatePlayer() {
        PlayerDatabase playerDatabase = PlayerDatabase.getInstance();
        playerDatabase.setBasePath(TEST_BASE_PATH);

        final String playerName = "testCreatePlayer";
        Assert.assertTrue("Bad boolean returned when creating a player.", playerDatabase.createPlayer(SEASON_NAME, playerName));
        Assert.assertTrue("Failed to create the playerPoints.txt file.", new FileHandler().fileExists(TEST_BASE_PATH + SEASON_NAME + "/" + DataModelNames.PLAYER_POINTS));
        List<String> players = new FileHandler().readFile(TEST_BASE_PATH + SEASON_NAME + "/" + DataModelNames.PLAYER_LIST);
        Assert.assertEquals("Unexpected number of player names written to playerList.txt.", 1, players.size());
        Assert.assertEquals("Player name in playerList.txt does not match expected", playerName, players.get(0));
    }

}
