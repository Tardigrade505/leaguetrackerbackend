package leaguetracker.ui.screens;

import leaguetracker.backend.objects.Game;
import leaguetracker.ui.screens.screentypes.Screen;
import leaguetracker.ui.screens.screentypes.TextFieldScreen;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * A screen that prompts the user to enter the names of any league members who
 * are missing from this game
 */
public class EnterMissingPeopleScreen extends TextFieldScreen {
    /**
     * The name of the season associated with this screen
     */
    private String seasonName;

    /**
     * Creates a new instance of an EnterMissingPeopleScreen
     * @param seasonName - the name of the associated season
     */
    public EnterMissingPeopleScreen(final String seasonName) {
        super("Enter the names of any missing people for this game", null);
        this.seasonName = seasonName;
    }

    @Override
    protected void onCompleteRender(HashMap<String, String> fieldsWithResponses) {

    }

    @Override
    public Screen render() {
        // Get a list of all players in the league
        List<String> leaguePlayers = backendInterface.getPlayerList(seasonName);

        // Build the textField
        final String textFieldTitle = "Any of these players missing from this game?\n\n"
                + convertListOfPlayersToOnePlayerPerLine(leaguePlayers)
                + "If so, enter their names below in as a comma-separated list to remove them from this game. Otherwise type none\n";
        this.textFields = Collections.singletonList(textFieldTitle);

        // Begin the rendering
        printDisplayText();
        HashMap<String, String> userResponses = printTextFieldsAndGatherResponses();

        // If player names were entered to be removed, remove them from the leaguePlayers for this game
        String namesToRemoveString = userResponses.get(textFieldTitle);
        if (!"none".equals(namesToRemoveString)) {
            // Convert user-entered list of names to arrayList
            // Clean up the player list string by removing some spaces by commas
            namesToRemoveString = namesToRemoveString.replace(", ", ",");
            List<String> namesToRemove = Arrays.asList(namesToRemoveString.split(","));
            leaguePlayers.removeAll(namesToRemove);
        }

        // Generate a new game screen
        return new GameScreen(Game.generateNewGame(seasonName, leaguePlayers));

    }

    /**
     * Helper method that takes a list of player names and returns those names
     * one per line
     * @param leaguePlayers - the list of league player names
     * @return a string containing the player names, one per line
     */
    private String convertListOfPlayersToOnePlayerPerLine(List<String> leaguePlayers) {
        StringBuilder onePlayerPerLine = new StringBuilder();

        for (String leaguePlayerName : leaguePlayers) {
            onePlayerPerLine.append(leaguePlayerName).append("\n");
        }
        return onePlayerPerLine.toString();
    }
}
