package leaguetracker.backend.controller;

import leaguetracker.backend.BackendTest;
import leaguetracker.backend.objects.Achievement;
import leaguetracker.backend.objects.GameResults;
import leaguetracker.backend.objects.Rarity;
import leaguetracker.backend.util.BackendUtils;
import leaguetracker.database.DataModelNames;
import leaguetracker.database.FileHandler;
import leaguetracker.database.PlayerDatabase;
import leaguetracker.database.SeasonDatabase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PlayerControllerTest extends BackendTest {
    private static final String TEST_BASE_PATH = BackendUtils.getProjectBasePath() + "app/src/test/testDir/";
    private static final String SEASON_NAME = "PlayerControllerTest";

    @Before
    public void createTestDirAndTestSeason() {
        Assert.assertTrue("Failed to create test dir. Tests cannot run.", new File(TEST_BASE_PATH).mkdir());

        SeasonDatabase seasonDatabase = SeasonDatabase.getInstance();
        seasonDatabase.setBasePath(TEST_BASE_PATH);
        Assert.assertTrue("Failed to create Test season. Tests cannot run.",
                seasonDatabase.createSeason(SEASON_NAME, new ArrayList<>(), "cheevo.txt"));
    }

    @After
    public void cleanupTestDir() {
        try {
            Assert.assertTrue("Failed to clean up test dir. Tests cannot run.", deleteDirectory(new File(TEST_BASE_PATH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRecordPlayerResults() {
        // Create a new player
        PlayerDatabase playerDatabase = PlayerDatabase.getInstance();
        playerDatabase.setBasePath(TEST_BASE_PATH);
        Assert.assertTrue("Failed to create player.", playerDatabase.createPlayer(SEASON_NAME, "testRecordPlayerResults"));

        // Create a game result to record
        HashMap<Achievement, String> achievementWinners = new HashMap<>();
        achievementWinners.put(new Achievement("test", 1, 1, "", Rarity.COMMON), "testRecordPlayerResults");
        GameResults gameResults = new GameResults(Collections.singletonList("testRecordPlayerResults"),
                Collections.singletonList("testRecordPlayerResults"), Collections.singletonList("testRecordPlayerResults"), achievementWinners);

        // Record the result
        PlayerController playerController = new PlayerController();
        Assert.assertTrue("Bad boolean from recordPlayerResults.", playerController.recordPlayerResults(SEASON_NAME, gameResults));

        // Read the playerPoints.txt file and ensure values were recorded appropriately
        List<String> pointsContents = new FileHandler().readFile(TEST_BASE_PATH + SEASON_NAME + "/" + DataModelNames.PLAYER_POINTS);
        Assert.assertEquals("Unexpected number of lines in playerPoints.txt.", 1, pointsContents.size());
        final String expectedResults = "testRecordPlayerResults,7,3,2,1,1,1";
        Assert.assertEquals("Recorded results were not as expected.", expectedResults, pointsContents.get(0));
    }
}
