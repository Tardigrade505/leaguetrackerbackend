package leaguetracker.ui.screens;

import leaguetracker.ui.screencomponents.UserAction;
import leaguetracker.ui.screens.screentypes.Screen;
import leaguetracker.ui.screens.screentypes.UserActionScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * A screen that displays the leader board to the user and asks if they want
 * to play another game or end the league session
 */
public class LeaderBoardScreen extends UserActionScreen {
    public LeaderBoardScreen(final String seasonName) {
        super("LEADERBOARD\n\n", null);
        this.displayText = this.displayText + backendInterface.getStandings(seasonName);
        List<UserAction> userActions = new ArrayList<>();
        userActions.add(new UserAction("Play another game in this session?", new EnterMissingPeopleScreen(seasonName)));
        userActions.add(new UserAction("End session.", new SeasonMainMenu(seasonName)));
        this.userActions = userActions;
    }

    @Override
    public Screen render() {
        printDisplayText();
        printUserActions();
        final int userInput = waitForUserNumberInput();
        return userActions.get(userInput-1).getNextScreen();
    }
}
