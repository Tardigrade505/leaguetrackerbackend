package com.rest.api.leaguetracker.restapileaguetracker.objects;

import com.rest.api.leaguetracker.restapileaguetracker.util.Constants;
import com.rest.api.leaguetracker.restapileaguetracker.util.FileHandler;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Data
@Table(name = "ACHIEVEMENT")
public class Achievement {
    /*
    Index values of achievements file data
     */
    private static final int NAME_INDEX = 0;
    private static final int POINTS_VALUE_INDEX = 1;
    private static final int DOLLAR_VALUE_INDEX = 2;
    private static final int RARITY_INDEX = 3;

    private @Id @GeneratedValue Long id;
    private String name;
    private int pointsValue;
    private int dollarValue;
    private String imagePath;
    private String rarity;

    public Achievement(String name, int pointsValue, int dollarValue, String imagePath, String rarity) {
        this.name = name;
        this.pointsValue = pointsValue;
        this.dollarValue = dollarValue;
        this.imagePath = imagePath;
        this.rarity = rarity;
    }

    public Achievement() {} // Default constructor

    /**
     * A method that randomly generates a number of achievements that it reads from an achievement file
     * @param numberOfAchievements - the number of achievements to generate
     * @param fileName - the name of the achievements file
     * @return a list of achievements
     */
    public List<Achievement> generateAchievements(final int numberOfAchievements, final String fileName) {
        List<Achievement> achievementPool = importAchievements(fileName);
        List<Achievement> achievements = new ArrayList<>();

        // Group the achievement pool by rarity
        List<Achievement> commonPool = new ArrayList<>();
        List<Achievement> uncommonPool = new ArrayList<>();
        List<Achievement> rarePool = new ArrayList<>();
        List<Achievement> mythicRarePool = new ArrayList<>();
        for (Achievement achievement : achievementPool) {
            switch (achievement.getRarity()) {
                case Constants.COMMON: commonPool.add(achievement); break;
                case Constants.UNCOMMON: uncommonPool.add(achievement); break;
                case Constants.RARE: rarePool.add(achievement); break;
                case Constants.MYTHIC_RARE: mythicRarePool.add(achievement); break;
                default: break; // TODO: log error message
            }
        }

        // Assign rarity bounds for random selection of rarity
        final double commonRange = Rarity.COMMON.getChanceOfChoosing() * 100;
        final double uncommonRange = commonRange + Rarity.UNCOMMON.getChanceOfChoosing() * 100;
        final double rareRange = uncommonRange + Rarity.RARE.getChanceOfChoosing() * 100;
        final double mythicRareRange = rareRange + Rarity.MYTHIC_RARE.getChanceOfChoosing() * 100;

        int retries = 0;
        while (achievements.size() < numberOfAchievements && ++retries < 100) {
            // Select the rarity
            final int randomRange = ThreadLocalRandom.current().nextInt(0,  99);

            // Based on the rarity, select the achievement
            Achievement randomlySelectedAchievement = null;
            if (randomRange < commonRange && commonPool.size() > 0) { // Common rarity
                randomlySelectedAchievement = (commonPool.size() > 1) ?
                        commonPool.get(ThreadLocalRandom.current().nextInt(0,  commonPool.size())) : commonPool.get(0);
            } else if (randomRange >= commonRange && randomRange < uncommonRange && uncommonPool.size() > 0) { // Uncommon rarity
                randomlySelectedAchievement = (uncommonPool.size() > 1) ?
                        uncommonPool.get(ThreadLocalRandom.current().nextInt(0,  uncommonPool.size())) : uncommonPool.get(0);
            } else if (randomRange >= uncommonRange && randomRange < rareRange && rarePool.size() > 0) { // Rare rarity
                randomlySelectedAchievement = (rarePool.size() > 1) ?
                        rarePool.get(ThreadLocalRandom.current().nextInt(0,  rarePool.size())) : rarePool.get(0);
            } else if (randomRange >= rareRange && randomRange < mythicRareRange && mythicRarePool.size() > 0) { // Mythic rarity
                randomlySelectedAchievement = (mythicRarePool.size() > 1) ?
                        mythicRarePool.get(ThreadLocalRandom.current().nextInt(0,  mythicRarePool.size())) : mythicRarePool.get(0);
            }

            // If that achievement has not already been selected, add it
            if (null != randomlySelectedAchievement && !achievements.contains(randomlySelectedAchievement)) {
                achievements.add(randomlySelectedAchievement);
            }
        }

        // Exited while loop due to a failure
        if (retries >= 100) {
            System.out.println("Failed to generate achievements.");
        }
        return achievements;
    }

    /**
     * Reads the achievements file and parses its contents into a list of achievements
     * @param fileName
     * @return a full list of all the achievements
     */
    public List<Achievement> importAchievements(String fileName) {
        List<Achievement> importedAchievements = new ArrayList<>();

        InputStream in = getClass().getResourceAsStream("/" + fileName);
        List<String> rawAchievements = null;
        try {
            rawAchievements = new FileHandler().readFileInJar(in);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("Raw achievemnts = " + rawAchievements);
        String achievementsImagesBasePath = fileName.replace(Constants.ACHIEVEMENTS_FILE, ""); // TODO: update when achievements.txt is not hard-coded
        achievementsImagesBasePath = achievementsImagesBasePath + Constants.ACHIEVEMENTS_IMAGES_DIR + "/";

        for (String rawAchievement : rawAchievements) {
            String[] achievementSplit = rawAchievement.split(",");
            final String imagePath = achievementsImagesBasePath + achievementSplit[NAME_INDEX] + ".png";
            importedAchievements.add(new Achievement(achievementSplit[NAME_INDEX], Integer.parseInt(achievementSplit[POINTS_VALUE_INDEX]),
                    Integer.parseInt(achievementSplit[DOLLAR_VALUE_INDEX]), imagePath, (achievementSplit[RARITY_INDEX])));
        }
        return importedAchievements;
    }

    /**
     * Chooses a random common achievement from the achievement pool
     * @param fileName - the full path to the achievement file
     * @return a random common achievement
     */
    public Achievement generateBonusAchievement(final List<Achievement> alreadySelectedAchievements, final String fileName) {
        List<Achievement> achievementPool = importAchievements(fileName);
        List<Achievement> commonAchievementPool = new ArrayList<>();

        for (Achievement achievement : achievementPool) {
            if (Constants.COMMON.equals(achievement.getRarity())) {
                commonAchievementPool.add(achievement);
            }
        }

        Achievement bonusAchievement = null;
        int retries = 0;
        while (null == bonusAchievement && ++retries < 100) {
            final int randomIndex = ThreadLocalRandom.current().nextInt(0, commonAchievementPool.size());
            final Achievement randomlySelectedAchievement = commonAchievementPool.get(randomIndex);
            if (!alreadySelectedAchievements.contains(randomlySelectedAchievement)) {
                bonusAchievement = randomlySelectedAchievement;
            }
        }

        // Exited due to error
        if (retries >= 100) {
            System.out.println("Failed to choose a bonus achievement.");
        }
        return bonusAchievement;
    }
}
