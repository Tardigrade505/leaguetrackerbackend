package leaguetracker.ui.screens;

import leaguetracker.ui.screens.screentypes.TimedScreen;

/**
 * A timed screen that welcomes the user to the app
 */
public class WelcomeScreen extends TimedScreen {
    public WelcomeScreen() {
        super("Welcome to GgEDH Brawlin'!", 1000, new SeasonPickerScreen());
    }
}
