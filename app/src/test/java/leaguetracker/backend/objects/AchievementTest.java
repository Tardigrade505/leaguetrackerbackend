package leaguetracker.backend.objects;

import leaguetracker.backend.util.BackendUtils;
import leaguetracker.database.DataModelNames;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class AchievementTest {
    private final String pathToAchievementsFile = BackendUtils.getProjectBasePath() + "app/src/main/resources/" + DataModelNames.ACHIEVEMENTS_FILE;

    @Test
    public void testGenerateAchievements() {
        System.out.println("Achievements = " + Achievement.generateAchievements(3, pathToAchievementsFile));
        Assert.assertEquals("Unexpected number of achievements were generated.", 3,
                Achievement.generateAchievements(3, pathToAchievementsFile).size());
    }

    @Test
    public void testGenerateBonusAchievement() {
        Assert.assertEquals("Failed to return a common bonus achievement.", Rarity.COMMON,
                Achievement.generateBonusAchievement(new ArrayList<>(), pathToAchievementsFile).getRarity());
    }
}
