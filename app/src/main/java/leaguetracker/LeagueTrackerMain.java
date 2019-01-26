package leaguetracker;

import leaguetracker.ui.screens.screentypes.Screen;
import leaguetracker.ui.screens.WelcomeScreen;

/**
 * The main class of the league tracker app UI. Renders screens until it recieves a null screen
 */
public class LeagueTrackerMain {

    public static void main(String[] args) {

        Screen currentScreen = new WelcomeScreen(); // Start with a welcome screen
        while (null != currentScreen) {
            currentScreen = currentScreen.render();
        }
    }
}
