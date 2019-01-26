package leaguetracker.backend;

import java.io.File;

public abstract class BackendTest {
    /**
     * Deletes the folder and all its contents
     * @param folder - the folder to be deleted
     * @return a boolean representing whether or not the folder and all its contents were deleted
     */
    protected boolean deleteDirectory(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    if (!deleteDirectory(f)) {
                        return false;
                    }
                } else {
                    // If you failed to delete, return false
                    if (!f.delete()) {
                        return false;
                    }
                }
            }
        }
        // If you failed to delete the folder return false
        return folder.delete();
    }
}
