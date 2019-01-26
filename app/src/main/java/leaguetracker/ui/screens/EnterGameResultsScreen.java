package leaguetracker.ui.screens;

import leaguetracker.backend.objects.GameResults;
import leaguetracker.backend.objects.Achievement;
import leaguetracker.backend.objects.Game;
import leaguetracker.ui.screens.screentypes.Screen;
import leaguetracker.ui.screens.screentypes.TextFieldScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A screen for entering the results of the most recently played game
 */
public class EnterGameResultsScreen extends TextFieldScreen {
    /**
     * An object representing the game to record the results of
     */
    private Game game;

    public EnterGameResultsScreen(final Game game) {
        super("Enter the results of the game!", null);
        this.game = game;

        // Set up text fields for winners and second place of all tables
        List<String> textFields = new ArrayList<>();
        int tableNumber = 0;
        for (List<String> table : game.getTables()) {
            textFields.add("Who won table" + ++tableNumber + "? (" + table + ")");
            textFields.add("Who took second place at table" + tableNumber + "? (" + table + ")");
        }

        // Set up text fields for winners of achievements
        for (Achievement achievement : game.getAchievements()) {
            textFields.add("Who won the achievement " + achievement.getName() + "? (type none if no one did)");
        }
        textFields.add("Who won the bonus achievement " + game.getBonusAchievement().getName() + "? (type none if no one did)");

        this.textFields = textFields;
    }

    @Override
    protected void onCompleteRender(HashMap<String, String> fieldsWithResponses) {

    }

    @Override
    public Screen render() {
        printDisplayText();
        List<String> userResponses = printTextFieldsAndGatherResponsesAsArray(); // TODO: allow passing of methods into this method to create custom validators

        // Convert the userResponses to gameResults object
        final int numberOfTables = game.getTables().size();

        List<String> winners = new ArrayList<>();
        List<String> seconders = new ArrayList<>();
        List<String> participants = new ArrayList<>();
        HashMap<Achievement, String> achievementsAndWinners = new HashMap<>();

        // Find the winners and seconders
        for (int i = 0; i < numberOfTables; i++) {
            winners.add(userResponses.get(i*2));
            seconders.add(userResponses.get(i*2+1));
        }

        // Find the participants
        for (List<String> table : game.getTables()) {
            participants.addAll(table);
        }

        // Find the winners of the achievements
        int startingIndex = 2*numberOfTables;
        for (Achievement achievement : game.getAchievements()) {
            achievementsAndWinners.put(achievement, userResponses.get(startingIndex++));
        }

        // Find the winner of the bonus achievement
        achievementsAndWinners.put(game.getBonusAchievement(), userResponses.get(userResponses.size()-1));


        // Record the results
        backendInterface.recordGameResults(game.getSeasonName(), new GameResults(winners, seconders, participants, achievementsAndWinners));
        return new LeaderBoardScreen(game.getSeasonName());
    }
}
