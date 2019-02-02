package com.rest.api.leaguetracker.restapileaguetracker.objects.helper;

import java.util.HashMap;
import java.util.List;

public class GameResults {
    private int seasonId;
//    private List<String> winners;
//    private List<String> seconders;
//    private HashMap<String, String> achievementWinners;
//    private List<String> participants;

    private List<TableResult> tableResults;


    public GameResults(int seasonId, List<TableResult> tableResults) {
        this.seasonId = seasonId;
//        this.winners = winners;
//        this.seconders = seconders;
//        this.achievementWinners = achievementWinners;
//        this.participants = participants;
        this.tableResults = tableResults;
    }

    public List<TableResult> getTableResults() {
        return tableResults;
    }

    public int getSeasonId() {
        return seasonId;
    }

    //    public int getSeasonId() {
//        return seasonId;
//    }
//
//    public List<String> getWinners() {
//        return winners;
//    }
//
//    public List<String> getSeconders() {
//        return seconders;
//    }
//
//    public HashMap<String, String> getAchievementWinners() {
//        return achievementWinners;
//    }
//
//    public List<String> getParticipants() {
//        return participants;
//    }
//
//    @Override
//    public String toString() {
//        return "GameResults{" +
//                "seasonId=" + seasonId +
//                ", winners=" + winners +
//                ", seconders=" + seconders +
//                ", achievementWinners=" + achievementWinners +
//                ", participants=" + participants +
//                '}';
//    }
}
