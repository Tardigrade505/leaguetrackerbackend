package leaguetracker.gui;

import leaguetracker.backend.objects.Achievement;
import leaguetracker.backend.objects.Game;
import leaguetracker.ui.BackendInterface;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A JPanel that holds the achievements for this particular game
 */
public class AchievementsPanel extends JPanel {
    private final float titleFontSize = 30.0f;
    private final double cardImageSizePercentage = 0.60d;

    public AchievementsPanel(JFrame superFrame, Game game) {
        //        setBorder(BorderFactory.createTitledBorder("Achievement border"));

        // Gather required information from the backend
        final BackendInterface backendInterface = new BackendInterface();
        final String standings = backendInterface.getStandings(game.getSeasonName()); // Grab the standings info
        final String[] standingsRows = standings.split("\n");
        final String playerInFirst = standingsRows[0].split(",")[0];
        final String pointsOfPlayerInFirst = standingsRows[0].split(",")[1]; // Use this to determine if this is the first game in the session
        final String playerInLast = standingsRows[standingsRows.length-1].split(",")[0];
        boolean isFirstSession = " 0 points".equals(pointsOfPlayerInFirst);

        //////// Create GUI screen components ////////
        // Achievements title
        JLabel achievementTitle;
        if (isFirstSession) { // Handle case where there aren't any standings yet (first game of season)
            achievementTitle = new JLabel("Achievements");
            achievementTitle.setFont(achievementTitle.getFont().deriveFont(titleFontSize+10));
        } else {
            achievementTitle = new JLabel("Achievements for NOT " + playerInFirst);
            achievementTitle.setFont(achievementTitle.getFont().deriveFont(titleFontSize));
        }

        // Achievements card images
        List<JLabel> achievementImageLabels = new ArrayList<>();
        for (Achievement achievement : game.getAchievements()) {
            JLabel imageLabel = new JLabel(scaleImageIcon(new ImageIcon(achievement.getImagePath()), cardImageSizePercentage));
            achievementImageLabels.add(imageLabel);
        }

        // Bonus Achievement title and card image
        JLabel bonusAchievementTitle = new JLabel("\n\n\nBonus Achievement for " + playerInLast);
        bonusAchievementTitle.setFont(bonusAchievementTitle.getFont().deriveFont(titleFontSize));
        JLabel bonusAchievementLabel = new JLabel(scaleImageIcon(new ImageIcon(game.getBonusAchievement().getImagePath()), cardImageSizePercentage));

        // Record results button
        JButton recordResultsButton = new JButton("Finish and Record");


        //////// Add behavior to components ////////
        // Close GUI when record results button is clicked
        recordResultsButton.addActionListener(e -> superFrame.dispose());

        //////// Place components of GUI screen ////////
        // Layout
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        // First Row (Achievement title)
        gc.anchor = GridBagConstraints.CENTER;
        gc.weightx = 0.5;
        gc.weighty = 0.5;

        gc.gridx = 1;
        gc.gridy = 0;
        add(achievementTitle, gc);

        // Second row (Achievement card images)
        gc.anchor = GridBagConstraints.CENTER;
        gc.weightx = 0.5;
        gc.weighty = 0.5;

        gc.gridx = 0;
        gc.gridy = 1;

        for (JLabel achievementImage : achievementImageLabels) {
            add(achievementImage, gc);
            gc.gridx++;
        }

        // Third row (bonus achievement title)
        if (!isFirstSession) { // Only render if this is NOT the first session
            // Next row
            gc.gridy++;
            gc.gridx = 1;
            add(bonusAchievementTitle, gc);

            // Fourth row (bonus achievement card image)
            gc.gridy++;
            add(bonusAchievementLabel, gc);
        }

        // Last row (Record results button)
        gc.gridx = 1;
        gc.gridy++;
        add(recordResultsButton, gc);

    }

    /**
     * Given an image icon and a percentage, scales the imageIcon by that percentage
     * @param imageIcon - the image icon
     * @param percentage - the percentage to scale by
     * @return A scaled image icon
     */
    private ImageIcon scaleImageIcon(ImageIcon imageIcon, double percentage) {
        int newWidth = (int) Math.round(imageIcon.getIconWidth() * percentage);
        int newHeight = (int) Math.round(imageIcon.getIconHeight() * percentage);

        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(newWidth, newHeight,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }
}
