package leaguetracker.ui.screens;

import leaguetracker.ui.screens.screentypes.Screen;

public class ScreenFactory {
    public Screen getScreen(final int screenType) {
        switch (screenType) {
            case ScreenTypes.WELCOME_SCREEN:
                return new WelcomeScreen();
            case ScreenTypes.SEASON_PICKER:
                return new SeasonPickerScreen();
            default: return null;
        }
    }
}
