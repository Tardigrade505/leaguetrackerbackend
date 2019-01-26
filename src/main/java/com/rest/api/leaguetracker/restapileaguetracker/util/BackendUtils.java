package com.rest.api.leaguetracker.restapileaguetracker.util;

public  class BackendUtils {
    /**
     *
     * @return The full path to the project
     */
    public static String getProjectBasePath() {
        return System.getProperty("user.dir").split("restapileaguetracker")[0] + "restapileaguetracker/";
    }
}
