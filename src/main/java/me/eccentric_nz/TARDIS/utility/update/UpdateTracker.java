package me.eccentric_nz.TARDIS.utility.update;

public class UpdateTracker {

    private static boolean updateFound;
    private static String currentCommit;
    private static String latestCommit;

    public static void setUpdateFound(boolean found) {
        updateFound = found;
    }

    public static boolean isUpdateFound() {
        return updateFound;
    }

    public static void setCurrentCommit(String commit) {
        currentCommit = commit;
    }

    public static String getCurrentCommit() {
        return currentCommit;
    }

    public static void setLatestCommit(String commit) {
        latestCommit = commit;
    }

    public static String getLatestCommit() {
        return latestCommit;
    }
}
