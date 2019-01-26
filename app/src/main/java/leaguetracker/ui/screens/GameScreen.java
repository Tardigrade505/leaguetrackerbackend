package leaguetracker.ui.screens;

import leaguetracker.gui.GameScreenGUI;
import leaguetracker.backend.objects.Game;
import leaguetracker.ui.screens.EnterGameResultsScreen;
import leaguetracker.ui.screens.screentypes.Screen;

import javax.swing.*;

/**
 * A screen that starts the GUI which displays the player tables and achievements for the
 * next game
 */
public class GameScreen extends Screen {
    /**
     * A reference to the game object that this screen represents
     */
    private Game game;

    /**
     * Creates a new screen
     *
     */
    public GameScreen(final Game game) {
        super("");
        this.game = game;
    }

    @Override
    public Screen render() {
        SwingUtilities.invokeLater(new GameScreenGUI(game));
        return new EnterGameResultsScreen(game);
    }
}
