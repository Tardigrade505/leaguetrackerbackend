package leaguetracker.gui;

import leaguetracker.backend.objects.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Main GUI frame that holds all the other components
 */
public class MainFrame extends JFrame {

    public MainFrame(final String title, final Game game) {
        super(title);

        // Set layout manager
        setLayout(new BorderLayout());

        // Create Swing components
        TitleAndTablePanel titleAndTablePanel = new TitleAndTablePanel(game);
        AchievementsPanel achievementsPanel = new AchievementsPanel(this, game);
        JScrollPane scrollPane = new JScrollPane(achievementsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Add Swing components to content pane
        Container c = getContentPane();
        c.add(titleAndTablePanel, BorderLayout.NORTH);
        c.add(scrollPane, BorderLayout.CENTER);
    }
}
