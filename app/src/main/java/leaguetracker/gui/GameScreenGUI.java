package leaguetracker.gui;

import leaguetracker.backend.objects.Game;

import javax.swing.*;

/**
 * A runnable class that initiates the GUI for the Game Screen
 */
public class GameScreenGUI implements Runnable {
    /**
     * An object representing the current game
     */
    private Game game;

    public GameScreenGUI(Game game) {
        this.game = game;
    }

    /**
     * Sets up the behavior of the main frame of the GUI
     */
    @Override
    public void run() {
        JFrame frame = new MainFrame("Game", game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        frame.setVisible(true);
    }
}
