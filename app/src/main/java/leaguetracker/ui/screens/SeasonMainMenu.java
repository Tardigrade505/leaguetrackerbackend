package leaguetracker.ui.screens;

import leaguetracker.ui.screencomponents.UserAction;
import leaguetracker.ui.screens.screentypes.Screen;
import leaguetracker.ui.screens.screentypes.UserActionScreen;

import java.util.ArrayList;

/**
 * A main menu screen for a particular season with a variety of options for the season
 */
public class SeasonMainMenu extends UserActionScreen {

    public SeasonMainMenu(final String seasonName) {
        super(seasonName + " Main Menu", null);
        ArrayList<UserAction> userActions = new ArrayList<>();
        userActions.add(new UserAction("Play a new game!", new EnterMissingPeopleScreen(seasonName)));
        userActions.add(new UserAction("View Leaderboard", new LeaderBoardPreviewScreen(seasonName)));
        userActions.add(new UserAction("Exit", null));
        this.userActions = userActions;

    }

    @Override
    public Screen render() {
        printDisplayText();
        printUserActions();
        final int userInputNumber = waitForUserNumberInput();
        return userActions.get(userInputNumber-1).getNextScreen();
    }
}
