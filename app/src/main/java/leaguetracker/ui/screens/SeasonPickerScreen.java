package leaguetracker.ui.screens;

import leaguetracker.ui.screencomponents.UserAction;
import leaguetracker.ui.screens.CreateNewSeasonScreen;
import leaguetracker.ui.screens.screentypes.Screen;
import leaguetracker.ui.screens.screentypes.UserActionScreen;

/**
 * A screen that allows the user to navigate to past seasons, current seasons, and create new seasons
 */
public class SeasonPickerScreen extends UserActionScreen {
    public SeasonPickerScreen() {
        super("Choose a current season, or create a new one!", null);
    }

    @Override
    public Screen render() {
        printDisplayText();

        // Find all of the existing seasons on this machine
        userActions = backendInterface.createUserActionsFromExistingSeasons();
        userActions.add(new UserAction("Create a new season", new CreateNewSeasonScreen()));
        printUserActions();

        // Wait for user input
        final int userInput = waitForUserNumberInput();
        return userActions.get(userInput-1).getNextScreen();
    }
}
