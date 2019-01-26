package leaguetracker.ui.screens.screentypes;

import leaguetracker.ui.BackendInterface;

/**
 * The super class of all screens
 */
public abstract class Screen {
    /**
     * The text that appears at the top of a screen, like a title
     */
    protected String displayText;

    /**
     * A controller for calling the backend API to perform backend tasks
     */
    protected BackendInterface backendInterface = new BackendInterface();

    /**
     * Creates a new screen
     *
     * @param displayText - the display text
     */
    public Screen(final String displayText) {
        this.displayText = displayText;
    }

    /**
     * A method that all Screens must implement that prints out the desired appearance of the screen
     * @return the next screen to render
     */
    public abstract Screen render();

    /**
     * A common way to print out all display text
     */
    protected void printDisplayText() {
        // If there is display text, show it like:
        /*

        Welcome to this screen!


         */
        if (null != displayText) {
            System.out.println("\n" + displayText + "\n");
        }
    }
}

