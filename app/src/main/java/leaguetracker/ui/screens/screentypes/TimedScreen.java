package leaguetracker.ui.screens.screentypes;

/**
 * A screen that displays its display text for a period of time before displaying the
 * next screen
 */
public abstract class TimedScreen extends Screen {
    /**
     * The amount of time in milliseconds that the screen should appear before changing
     */
    private int waitTime;

    /**
     * The next screen to transition to once the wait time is up
     */
    private Screen nextScreen;

    public TimedScreen(final String displayText, final int waitTime, final Screen nextScreen) {
        super(displayText);
        this.waitTime = waitTime;
        this.nextScreen = nextScreen;
    }

    /**
     * Prints any display text, then waits, then returns the next screen
     * @return the next screenb
     */
    public Screen render() {
        printDisplayText();

        // Wait before changing screens
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return nextScreen;
    }
}
