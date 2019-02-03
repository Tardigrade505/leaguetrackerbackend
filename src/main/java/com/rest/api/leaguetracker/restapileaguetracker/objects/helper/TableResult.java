package com.rest.api.leaguetracker.restapileaguetracker.objects.helper;

import java.util.HashMap;
import java.util.List;

public class TableResult {
    private List<String> participants;
    private String winner;
    private List<String> seconders;
    private HashMap<String, String> achievementWinners;

    public TableResult(List<String> participants, String winner, List<String> seconders, HashMap<String, String> achievementWinners) {
        this.participants = participants;
        this.winner = winner;
        this.seconders = seconders;
        this.achievementWinners = achievementWinners;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public String getWinner() {
        return winner;
    }

    public List<String> getSeconders() {
        return seconders;
    }

    public HashMap<String, String> getAchievementWinners() {
        return achievementWinners;
    }
}
