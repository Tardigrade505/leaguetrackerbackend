package leaguetracker.database;

import leaguetracker.database.util.DatabaseUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public abstract class Database {
    /**
     * For managing files
     */
    protected FileHandler fileHandler;

    /**
     * The base path of the project
     */
    protected String basePath;

    protected Database() {
        this.fileHandler = new FileHandler();

        // Determine full path of location to store data
        this.basePath = DatabaseUtils.getProjectBasePath() + "data/";

        // Create the data directory if it does not already exist
        File dataFile = new File(basePath);
        if (!dataFile.isDirectory()) {
            // TODO: add error handling to deal with failure to create data file
            dataFile.mkdir();
        }
    }

    /**
     * Selects a row from a file based on a row key, which is the first element
     * of a comma-separated list in a line in the file
     * @param rowKey
     * @param filePath - the path to the file
     * @return a row in the file
     */
    protected List<String> selectRow(final String rowKey, final String filePath) {
        List<String> allRows = fileHandler.readFile(filePath);

        // Look through each row, for the row that has your key
        List<String> currentRow;
        for (String row : allRows) {
            currentRow = Arrays.asList(row.split(","));
            if (rowKey.equals(currentRow.get(0))) return currentRow;
        }
        // TODO: add error tracing when fail to find row
        System.out.println("Failed to find row with rowKey: " + rowKey);
        return null;
    }

    /**
     * Replaces a row with a new row with the same key
     * @param rowKey
     * @param newRow
     * @param filePath
     * @return a boolean representing whether or not the replace was successful
     */
    protected boolean replaceRow(final String rowKey, final List<String> newRow, final String filePath) {
        List<String> allRows = fileHandler.readFile(filePath);

        // Look through each row, for the row that has your key
        List<String> currentRow;
        for (int i = 0; i < allRows.size(); i++) {
            currentRow = Arrays.asList(allRows.get(i).split(","));
            if (rowKey.equals(currentRow.get(0))) {
                allRows.set(i, rowToString(newRow));
                break;
            }
        }

        // Write the updated rows to the file
        return fileHandler.writeToFile(filePath, allRows);
    }

    protected boolean addToRow(final String rowKey, final List<String> additiveRow, final String filePath) {
        // Add the row's values to the old row
        List<String> selectedRow = selectRow(rowKey, filePath);
        for (int i = 1; i < selectedRow.size(); i++) {
            int sum = Integer.parseInt(selectedRow.get(i)) + Integer.parseInt(additiveRow.get(i));
            selectedRow.set(i, String.valueOf(sum));
        }

        // Write the updated row
        return replaceRow(rowKey, selectedRow, filePath);
    }

    private String rowToString(List<String> row) {
        if (0 == row.size()) {
            return "";
        }
        if (1 == row.size()) {
            return row.get(0);
        }
        String rowString = row.toString();

        // Remove brackets []
        rowString = rowString.substring(1);
        rowString = rowString.substring(0, rowString.length()-1);

        // Remove spaces around commas
        return rowString.replace(", ", ",");
    }

    public void setBasePath(String testBasePath) {
        this.basePath = testBasePath;
    }
}
