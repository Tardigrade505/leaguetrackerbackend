package leaguetracker.backend.objects;

import leaguetracker.database.DataModelNames;
import leaguetracker.database.FileHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * An ADT that represents a league achievement
 */
public class Achievement {
    /*
    Index values of achievements file data
     */
    private static final int NAME_INDEX = 0;
    private static final int POINTS_VALUE_INDEX = 1;
    private static final int DOLLAR_VALUE_INDEX = 2;
    private static final int RARITY_INDEX = 3;

    /**
     * The name of the achievement
     */
    private String name;

    /**
     * The number of points the achievement is worth
     */
    private int pointsValue;

    /**
     * The amount of money in dollars to add to deck (no money prizes being given away) that the achievement is worth
     */
    private int dollarValue;

    /**
     * The full path to the image that represents this achievement
     */
    private String imagePath;

    /**
     * The rarity of the achievement
     */
    private Rarity rarity;

    public Achievement(String name, int pointsValue, int dollarValue, String imagePath, Rarity rarity) {
        this.name = name;
        this.pointsValue = pointsValue;
        this.dollarValue = dollarValue;
        this.imagePath = imagePath;
        this.rarity = rarity;
    }

    /**
     * A method that randomly generates a number of achievements that it reads from an achievement file
     * @param numberOfAchievements - the number of achievements to generate
     * @param pathToAchievementsFile - the full path to the achievements file
     * @return a list of achievements
     */
    public static List<Achievement> generateAchievements(final int numberOfAchievements, final String pathToAchievementsFile) {
        List<Achievement> achievementPool = importAchievements(pathToAchievementsFile);
        List<Achievement> achievements = new ArrayList<>();

        // Group the achievement pool by rarity
        List<Achievement> commonPool = new ArrayList<>();
        List<Achievement> uncommonPool = new ArrayList<>();
        List<Achievement> rarePool = new ArrayList<>();
        List<Achievement> mythicRarePool = new ArrayList<>();
        for (Achievement achievement : achievementPool) {
            switch (achievement.getRarity()) {
                case COMMON: commonPool.add(achievement); break;
                case UNCOMMON: uncommonPool.add(achievement); break;
                case RARE: rarePool.add(achievement); break;
                case MYTHIC_RARE: mythicRarePool.add(achievement); break;
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
     * @param pathToAchievementsFile
     * @return a full list of all the achievements
     */
    private static List<Achievement> importAchievements(String pathToAchievementsFile) {
        List<Achievement> importedAchievements = new ArrayList<>();

        List<String> rawAchievements = new FileHandler().readFile(pathToAchievementsFile);
        String achievementsImagesBasePath = pathToAchievementsFile.replace(DataModelNames.ACHIEVEMENTS_FILE, ""); // TODO: update when achievements.txt is not hard-coded
        achievementsImagesBasePath = achievementsImagesBasePath + DataModelNames.ACHIEVEMENTS_IMAGES_DIR + "/";

        for (String rawAchievement : rawAchievements) {
            String[] achievementSplit = rawAchievement.split(",");
            final String imagePath = achievementsImagesBasePath + achievementSplit[NAME_INDEX] + ".png";
            importedAchievements.add(new Achievement(achievementSplit[NAME_INDEX], Integer.parseInt(achievementSplit[POINTS_VALUE_INDEX]),
                    Integer.parseInt(achievementSplit[DOLLAR_VALUE_INDEX]), imagePath, Rarity.parseRarity(achievementSplit[RARITY_INDEX])));
        }
        return importedAchievements;
    }

    /**
     * Chooses a random common achievement from the achievement pool
     * @param pathToAchievementsFile - the full path to the achievement file
     * @return a random common achievement
     */
    public static Achievement generateBonusAchievement(final List<Achievement> alreadySelectedAchievements, final String pathToAchievementsFile) {
        List<Achievement> achievementPool = importAchievements(pathToAchievementsFile);
        List<Achievement> commonAchievementPool = new ArrayList<>();

        for (Achievement achievement : achievementPool) {
            if (Rarity.COMMON.equals(achievement.getRarity())) {
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

    public int getPointsValue() {
        return pointsValue;
    }

    public int getDollarValue() {
        return dollarValue;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getName() {
        return name;
    }

    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Achievement that = (Achievement) o;
        return pointsValue == that.pointsValue &&
                dollarValue == that.dollarValue &&
                Objects.equals(name, that.name) &&
                rarity == that.rarity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pointsValue, dollarValue, rarity);
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "name='" + name + '\'' +
                ", pointsValue=" + pointsValue +
                ", dollarValue=" + dollarValue +
                ", imagePath='" + imagePath + '\'' +
                ", rarity=" + rarity +
                '}';
    }
}
