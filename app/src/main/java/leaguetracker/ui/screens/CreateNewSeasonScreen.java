package leaguetracker.ui.screens;

import leaguetracker.ui.screens.screentypes.Screen;
import leaguetracker.ui.screens.screentypes.TextFieldScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A screen for creating a new season. Users input information about the new season
 * and that information is saved and a new season is created
 */
public class CreateNewSeasonScreen extends TextFieldScreen {
    private static final String SEASON_NAME_FIELD = "Season Name";
    private static final String PLAYERS_FIELD = "Players (comma separated list of names)";
    private static final String ACHIEVEMENTS_FIELD = "Achievements file location (type default for default cheevos)";

    public CreateNewSeasonScreen() {
        super("Enter new season information:", null);
        List<String> fields = new ArrayList<>();
        fields.add(SEASON_NAME_FIELD);
        fields.add(PLAYERS_FIELD);
        fields.add(ACHIEVEMENTS_FIELD);
        this.textFields = fields;

    }

    @Override
    protected void onCompleteRender(HashMap<String, String> fieldsWithResponses) {
        // Clean up the player list string by removing some spaces by commas
        String playerListString = fieldsWithResponses.get(PLAYERS_FIELD);
        playerListString = playerListString.replace(", ", ",");

        List<String> playerList = Arrays.asList(playerListString.split(","));
        backendInterface.createNewSeason(fieldsWithResponses.get(SEASON_NAME_FIELD), playerList,
                fieldsWithResponses.get(ACHIEVEMENTS_FIELD));
    }

    @Override
    public Screen render() {
        printDisplayText();
        HashMap<String, String> fieldsWithResponses = printTextFieldsAndGatherResponses();
        onCompleteRender(fieldsWithResponses);
        return new SeasonMainMenu(fieldsWithResponses.get("Season Name"));
    }
}
