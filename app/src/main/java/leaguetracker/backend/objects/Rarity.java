package leaguetracker.backend.objects;

public enum Rarity {
    COMMON(0.5),
    UNCOMMON(0.25),
    RARE(0.16),
    MYTHIC_RARE(0.09);
    private final double chanceOfChoosing;

    /*
    String values of the rarities
     */
    private static final String RARITY_COMMON = "common";
    private static final String RARITY_UNCOMMON = "uncommon";
    private static final String RARITY_RARE = "rare";
    private static final String RARITY_MYTHIC_RARE = "mythicrare";

    Rarity(double chanceOfChoosing) {
        this.chanceOfChoosing = chanceOfChoosing;
    }

    public double getChanceOfChoosing() {
        return chanceOfChoosing;
    }

    /**
     * A helper method that maps a string value of a rarity to a rarity object
     * @param rarityString
     * @return a rarity object
     */
    public static Rarity parseRarity(final String rarityString) {
        if (RARITY_COMMON.equals(rarityString)) {
            return Rarity.COMMON;
        }
        if (RARITY_UNCOMMON.equals(rarityString)) {
            return Rarity.UNCOMMON;
        }
        if (RARITY_RARE.equals(rarityString)) {
            return Rarity.RARE;
        }
        if (RARITY_MYTHIC_RARE.equals(rarityString)) {
            return Rarity.MYTHIC_RARE;
        }
        // TODO: log the bad read rarity string
        return null;
    }
}
