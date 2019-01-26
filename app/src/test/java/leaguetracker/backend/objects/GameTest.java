package leaguetracker.backend.objects;

import leaguetracker.backend.BackendTest;
import leaguetracker.backend.util.BackendUtils;
import leaguetracker.database.SeasonDatabase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameTest extends BackendTest {
    private static final String TEST_BASE_PATH = BackendUtils.getProjectBasePath() + "app/src/test/testDir/";
    private static final String SEASON_NAME = "GameTest";

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

//    @Test
//    public void testGenerateNewGame() {
//        System.out.println(Game.generateNewGame());
//    }

    /**
     * Test the function for determining the table groupings for a given number of people
     */
    @Test
    public void testFindTableGroupings() {
        Assert.assertEquals(new ArrayList<>(), findTableGroupings(0));
        Assert.assertEquals(Collections.singletonList(1), findTableGroupings(1));
        Assert.assertEquals(Collections.singletonList(2), findTableGroupings(2));
        Assert.assertEquals(Collections.singletonList(3), findTableGroupings(3));
        Assert.assertEquals(Collections.singletonList(4), findTableGroupings(4));
        Assert.assertEquals(Collections.singletonList(5), findTableGroupings(5));
        Assert.assertEquals(Arrays.asList(3,3), findTableGroupings(6));
        Assert.assertEquals(Arrays.asList(4,3), findTableGroupings(7));
        Assert.assertEquals(Arrays.asList(4,4), findTableGroupings(8));
        Assert.assertEquals(Arrays.asList(3,3,3), findTableGroupings(9));
        Assert.assertEquals(Arrays.asList(3,4,3), findTableGroupings(10));
        Assert.assertEquals(Arrays.asList(4,4,3), findTableGroupings(11));
        Assert.assertEquals(Arrays.asList(4,4,4), findTableGroupings(12));
        Assert.assertEquals(Arrays.asList(3,3,4,3), findTableGroupings(13));
        Assert.assertEquals(Arrays.asList(3,4,4,3), findTableGroupings(14));
        Assert.assertEquals(Arrays.asList(4,4,4,3), findTableGroupings(15));
        Assert.assertEquals(Arrays.asList(4,4,4,4), findTableGroupings(16));
        Assert.assertEquals(Arrays.asList(3,3,4,4,3), findTableGroupings(17));

    }

    private List<Integer> findTableGroupings(int numberOfPlayers) {
        return findTableGroupingsHelper(numberOfPlayers, new ArrayList<>());
    }

    private List<Integer> findTableGroupingsHelper(int numberOfPlayers, List<Integer> groupings) {
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
}
