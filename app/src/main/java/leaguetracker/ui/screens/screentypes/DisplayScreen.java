package leaguetracker.ui.screens.screentypes;

/**
 * A screen that displays some text only
 */
public abstract class DisplayScreen extends Screen {
    /**
     * The next screen to render
     */
    protected Screen nextScreen;

    public DisplayScreen(final String displayText, final Screen nextScreen) {
        super(displayText);
        this.nextScreen = nextScreen;
    }

    public Screen render() {
        printDisplayText();
        return nextScreen;
    }
}
