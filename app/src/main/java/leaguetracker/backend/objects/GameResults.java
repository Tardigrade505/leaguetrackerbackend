package leaguetracker.backend.objects;

import java.util.HashMap;
import java.util.List;

/**
 * An ADT that stores the results of a game
 */
public class GameResults {
    /**
     * A list of players who won their table
     */
    private List<String> winners;

    /**
     * A list of players who got second at their table
     */
    private List<String> seconders;

    /**
     * A list of players who participated in the game
     */
    private List<String> participants;

    /**
     * A map of achievements to their winners
     */
    private HashMap<Achievement, String> achievementWinners;

    public GameResults(List<String> winners, List<String> seconders, List<String> participants, HashMap<Achievement, String> achievementWinners) {
        this.winners = winners;
        this.seconders = seconders;
        this.participants = participants;
        this.achievementWinners = achievementWinners;
    }

    public List<String> getWinners() {
        return winners;
    }

    public List<String> getSeconders() {
        return seconders;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public HashMap<Achievement, String> getAchievementWinners() {
        return achievementWinners;
    }
}
