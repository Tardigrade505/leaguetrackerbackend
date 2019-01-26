package leaguetracker.backend.util;

public  class BackendUtils {
    /**
     *
     * @return The full path to the project
     */
    public static String getProjectBasePath() {
        return System.getProperty("user.dir").split("leaguetracker")[0] + "leaguetracker/";
    }
}
