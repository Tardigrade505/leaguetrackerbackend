package leaguetracker.ui.screens;

import leaguetracker.ui.screens.screentypes.DisplayScreen;
import leaguetracker.ui.screens.screentypes.Screen;

/**
 * A screen that shows the user the leader board
 */
public class LeaderBoardPreviewScreen extends DisplayScreen {
    private String seasonName;
    public LeaderBoardPreviewScreen(final String seasonName) {
        super("LEADERBOARD\n\n", null);
        this.displayText = this.displayText + backendInterface.getStandings(seasonName);
        this.seasonName = seasonName;
    }

    @Override
    public Screen render() {
        printDisplayText();
        return new SeasonMainMenu(seasonName);
    }
}
