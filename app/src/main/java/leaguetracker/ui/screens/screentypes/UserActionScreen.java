package leaguetracker.ui.screens.screentypes;

import leaguetracker.ui.screencomponents.UserAction;

import java.util.List;
import java.util.Scanner;

/**
 * A screen that contains both display text and a list of user actions that
 * appear as numbered options. Each user action is connected to a new screen
 */
public abstract class UserActionScreen extends Screen {
    /**
     * A list of potential user actions that will take the user to different screens
     */
    protected List<UserAction> userActions;

    public UserActionScreen(final String displayText, final List<UserAction> userActions) {
        super(displayText);
        this.userActions = userActions;
    }

    public abstract Screen render();

    /**
     * Prints out all user actions on this screen
     */
    protected void printUserActions() {
        /*

        1. Do this action
        2. Do this other action

         */
        StringBuilder userActionsString = new StringBuilder();
        userActionsString.append("\n"); // Initial newline

        int actionNumber = 0;
        for (UserAction userAction : userActions) {
            if (!userAction.getActionTitle().isEmpty()) {
                userActionsString.append(++actionNumber).append(".").append(" ").append(userAction.getActionTitle()).append("\n");
            }
        }
        System.out.println(userActionsString.toString());
    }

    /**
     * Sets up a Scanner to wait for user's integer input
     * @return the user's inputted integer
     */
    protected int waitForUserNumberInput() {
        Scanner sc = new Scanner(System.in);
        int userInputNumber;
        while (true) {
            if (isValidInput(userInputNumber = sc.nextInt())) break;
        }
        return userInputNumber;

    }

    /**
     * Validates the user input number to ensure that it is a valid input (must be an available option and not 0)
     * @param userInputNumber - the user's input number to validate
     * @return a boolean representing whether or not the user's input was valid
     */
    private boolean isValidInput(int userInputNumber) {
        return userInputNumber > 0 && userInputNumber < userActions.size() + 1;
    }
}
