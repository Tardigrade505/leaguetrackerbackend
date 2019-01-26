package leaguetracker.ui.screens;

import leaguetracker.backend.objects.Game;
import leaguetracker.gui.GameScreenGUI;
import leaguetracker.ui.screens.screentypes.Screen;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A class that helps with manual testing of the UI
 */
public class ScreenTests {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ScreenTests screenTests = new ScreenTests();

        Scanner sc = new Scanner(System.in);
        final String testToRun = sc.nextLine();

        Method method = ScreenTests.class.getDeclaredMethod(testToRun);
        method.invoke(screenTests);
    }

    /**
     * Test flow 1:
     * 1. User sees action screen
     * 2. User sees season picker screen
     * 3. User chooses to create a new season
     * 4. User sees enter season info screen and fills in season info
     * 5. User sees main menu for that season
     * 6. User selects to play a new game from the main menu
     * 7. User views the game with players at tables, and achievements
     * 8. User records results from that game, the results are saved in the database
     * 9. User is asked to play another game or end the session
     * 10. User ends the session
     */
    private void flow1() {
        run(new WelcomeScreen());
    }

    /**
     * Test the GUI of the Game page
     */
    private void gameGUI() {
        List<String> players = new ArrayList<>();
        players.add("Elliot");
        players.add("Ryan");
        SwingUtilities.invokeLater(new GameScreenGUI(Game.generateNewGame("NEW SEASON", players)));
    }

    /**
     * Helper method that runs the test
     * @param screen - the first screen to run the test on
     */
    private void run(Screen screen) {
        Screen currentScreen = screen;

        while (null != currentScreen) {
            currentScreen = currentScreen.render();
        }
    }
}
