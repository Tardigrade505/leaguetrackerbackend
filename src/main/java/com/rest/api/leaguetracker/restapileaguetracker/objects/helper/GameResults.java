package com.rest.api.leaguetracker.restapileaguetracker.objects.helper;

import java.util.HashMap;
import java.util.List;

public class GameResults {
    private int seasonId;
    private List<String> winners;
    private List<String> seconders;
    private HashMap<String, String> achievementWinners;
    private List<String> participants;

    public GameResults(int seasonId, List<String> winners, List<String> seconders, HashMap<String, String> achievementWinners,
                       List<String> participants) {
        this.seasonId = seasonId;
        this.winners = winners;
        this.seconders = seconders;
        this.achievementWinners = achievementWinners;
        this.participants = participants;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public List<String> getWinners() {
        return winners;
    }

    public List<String> getSeconders() {
        return seconders;
    }

    public HashMap<String, String> getAchievementWinners() {
        return achievementWinners;
    }

    public List<String> getParticipants() {
        return participants;
    }

    @Override
    public String toString() {
        return "GameResults{" +
                "seasonId=" + seasonId +
                ", winners=" + winners +
                ", seconders=" + seconders +
                ", achievementWinners=" + achievementWinners +
                ", participants=" + participants +
                '}';
    }
}
