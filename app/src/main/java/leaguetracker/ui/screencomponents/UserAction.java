package leaguetracker.ui.screencomponents;

import leaguetracker.ui.screens.screentypes.Screen;

public class UserAction {
    /**
     * The title of the action that will be displayed on the screen
     */
    private String actionTitle;

    /**
     * The next screen that will be rendered if you take this action
     */
    private Screen nextScreen;

    public UserAction(String actionTitle, Screen nextScreen) {
        this.actionTitle = actionTitle;
        this.nextScreen = nextScreen;
    }

    public String getActionTitle() {
        return actionTitle;
    }

    public Screen getNextScreen() {
        return nextScreen;
    }


}
