package leaguetracker.database;

import leaguetracker.database.util.DatabaseUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests the functionality of the database API
 */
public class FileHandlerTest {
    private static final String TEST_BASE_PATH = DatabaseUtils.getProjectBasePath() + "app/src/test/testDir/";

    @Before
    public void createTestDir() {
        Assert.assertTrue("Failed to create test dir. Tests cannot run.", new File(TEST_BASE_PATH).mkdir());
    }

    @After
    public void cleanupTestDir() {
        try {
            Assert.assertTrue("Failed to clean up test dir. Tests cannot run.", deleteDirectory(new File(TEST_BASE_PATH)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests creating a new directory
     */
    @Test
    public void testCreateNewDir() {
        FileHandler api = new FileHandler();
        final String directoryPath = TEST_BASE_PATH + "testCreateNewDir";

        Assert.assertTrue("Create new dir returned a bad boolean.", api.createNewDir(directoryPath));
        Assert.assertTrue("Failed to create a new directory.", checkFileExists(directoryPath));
        Assert.assertTrue("Directory exists, but is not a directory, just a file.", checkIsDir(directoryPath));
    }

    /**
     * Tests creating a new file
     */
    @Test
    public void testCreateNewFile() {
        FileHandler api = new FileHandler();
        final String filePath = TEST_BASE_PATH + "testCreateNewFile";

        Assert.assertTrue("Create new file returned a bad boolean", api.createNewFile(filePath));
        Assert.assertTrue("Failed to create a new file.", checkFileExists(filePath));
    }

    /**
     * Test writing multiple lines to a file
     */
    @Test
    public void testWritingToAFile() {
        FileHandler api = new FileHandler();
        final String filePath = TEST_BASE_PATH + "testWritingToAFile";

        // Create a file to write to
        Assert.assertTrue("Failed to create a file to write to.", api.createNewFile(filePath));

        // Write to that file
        final String firstLine = "Wrote to file";
        final String secondLine = "Second line";
        Assert.assertTrue("Writing to the file returned a bad boolean.", api.writeToFile(filePath, firstLine + "\n" + secondLine));

        // Test that what was written to the file is as expected
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add(firstLine);
        expectedResult.add(secondLine);
        Assert.assertEquals("Written lines were not as expected.", expectedResult, readFromFile(filePath));

    }

    /**
     * Test writing a single line to a file
     */
    @Test
    public void testWritingToAFileSingleLine() {
        FileHandler api = new FileHandler();
        final String filePath = TEST_BASE_PATH + "testWritingToAFileSingleLine";

        // Create a file to write to
        Assert.assertTrue("Failed to create a file to write to.", api.createNewFile(filePath));

        // Write to that file
        final String firstLine = "Wrote to file";
        Assert.assertTrue("Writing to the file returned a bad boolean.", api.writeToFile(filePath, firstLine ));

        // Test that what was written to the file is as expected
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add(firstLine);
        Assert.assertEquals("Written lines were not as expected.", expectedResult, readFromFile(filePath));

    }

    /**
     * Test overwriting a file with new contents
     */
    @Test
    public void testOverwritingFileWithNewContents() {
        FileHandler api = new FileHandler();
        final String filePath = TEST_BASE_PATH + "testOverwritingFileWithNewContents";

        // Create a file with contents to be overwritten
        Assert.assertTrue("Failed to create a file to write to.", api.createNewFile(filePath)); // Create a file
        final String toWrite = "Wrote to file\nAnother one!";
        Assert.assertTrue("Writing to the file returned a bad boolean.",
                api.writeToFile(filePath, toWrite)); // Write to that file

        // Overwrite that file with new contents
        final String overwriteString = "OVERWRITTEN";
        Assert.assertTrue("Failed to write to a file with existing contents.",
                api.writeToFile(filePath, overwriteString)); // Overwrite that file

        // Test that what was overwritten to the file is as expected
        Assert.assertEquals("Overwritten lines were not as expected.",
                Arrays.asList(overwriteString.split("\n")), readFromFile(filePath));
    }

    /**
     * Tests creating a file, writing to it, and reading from it
     */
    @Test
    public void testReadFile() {
        FileHandler api = new FileHandler();
        final String filePath = TEST_BASE_PATH + "testReadFile";

        // Create a file for reading
        Assert.assertTrue("Failed to create a file for reading.", api.createNewFile(filePath));

        // Write something to the file
        final String toWriteToFile = "This is line 1\nThis is line2";
        Assert.assertTrue("Failed to write to the file.", api.writeToFile(filePath, toWriteToFile));

        // Read that file and compare its contents to what is expected
        Assert.assertEquals("Read file returned a bad boolean.", Arrays.asList(toWriteToFile.split("\n")), api.readFile(filePath));
    }

    /**
     * Test appending lines to a file
     */
    @Test
    public void testAppendToFile() {
        FileHandler api = new FileHandler();
        final String filePath = TEST_BASE_PATH + "testAppendToFile";

        // Create a file
        Assert.assertTrue("Failed to create a new file.", api.createNewFile(filePath));

        // Write to that file
        final String toWrite = "Hey, its a line!\nCool beans!";
        Assert.assertTrue("Failed to write to the file.", api.writeToFile(filePath, toWrite));

        // Append to that file
        final String toAppend = "Woah, invasion!\nWatch out!";
        Assert.assertTrue("Appending to the file returned a bad boolean.", api.appendToFile(filePath, toAppend));

        // Validate that the append was successful
        List<String> split = Arrays.asList(toWrite.split("\n"));
        ArrayList<String> expectedReadLines = new ArrayList<>(split); // Need to wrap this List in ArrayList since List is immutable
        expectedReadLines.addAll(Arrays.asList(toAppend.split("\n")));
        Assert.assertEquals("Appended to file does not match expected.", expectedReadLines, api.readFile(filePath));
    }

    /**
     * Test deleting a file
     */
    @Test
    public void testDeleteFile() {
        FileHandler api = new FileHandler();
        final String filePath = TEST_BASE_PATH + "testDeleteFile";

        // Create a file
        Assert.assertTrue("Failed to create a file.", api.createNewFile(filePath));

        // Delete that file
        Assert.assertTrue("Delete file returned a bad boolean.", api.deleteFile(filePath));

        // Check if the file still exists
        Assert.assertFalse("Failed to delete file. File still exists.", checkFileExists(filePath));
    }

    /**
     * Test deleting a file that is actually a directory
     */
    @Test
    public void testDeleteFileThatIsDirectoryWithContents() {
        FileHandler api = new FileHandler();
        final String filePath = TEST_BASE_PATH + "testDeleteFileThatIsDirectory";

        // Create a directory
        Assert.assertTrue("Failed to create a directory.", api.createNewDir(filePath));

        // Create a file in that directory
        Assert.assertTrue("Failed to create a file within the directory.", api.createNewFile(filePath + "/subFile.txt"));

        // Attempt to delete that directory using delete file
        Assert.assertFalse("File was deleted even though it was a directory", api.deleteFile(filePath));

        // Check if the directory still exists and is still a directory
        Assert.assertTrue("Deleted the directory when it should not have been deleted.", checkIsDir(filePath));
    }

    /**
     * Test deleting a directory
     */
    @Test
    public void testDeleteDirectory() {
        FileHandler api = new FileHandler();
        final String filePath = TEST_BASE_PATH + "testDeleteDirectory";

        // Create a directory
        Assert.assertTrue("Failed to create a directory.", api.createNewDir(filePath));

        // Create a file in that directory
        Assert.assertTrue("Failed to create a file within the directory.", api.createNewFile(filePath + "/subFile.txt"));

        // Delete that directory
        Assert.assertTrue("Delete directory returned a bad boolean.", api.deleteDir(filePath));

        // Validate that the directory and its contents were deleted
        Assert.assertFalse("Failed to delete file.", checkFileExists(filePath + "/subFile.txt"));
        Assert.assertFalse("Failed to delete directory.", checkIsDir(filePath));
    }

    /**
     * Test deleting a directory passing in a file instead of string path
     */
    @Test
    public void testDeleteDirectoryUsingFileObject() {
        FileHandler api = new FileHandler();
        final String filePath = TEST_BASE_PATH + "testDeleteDirectoryUsingFileObject";

        // Create a directory
        Assert.assertTrue("Failed to create a directory.", api.createNewDir(filePath));

        // Create a file in that directory
        Assert.assertTrue("Failed to create a file within the directory.", api.createNewFile(filePath + "/subFile.txt"));

        // Delete that directory
        Assert.assertTrue("Delete directory returned a bad boolean.", api.deleteDir(new File(filePath)));

        // Validate that the directory and its contents were deleted
        Assert.assertFalse("Failed to delete file.", checkFileExists(filePath + "/subFile.txt"));
        Assert.assertFalse("Failed to delete directory.", checkIsDir(filePath));
    }

    @Test
    public void testInitializeDatabaseAPIAndDiscoverBasePath() {
        FileHandler api = new FileHandler();

    }

    /**
     * Deletes the folder and all its contents
     * @param folder - the folder to be deleted
     * @return a boolean representing whether or not the folder and all its contents were deleted
     */
    private boolean deleteDirectory(File folder) {
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

    /**
     * Checks whether or not a file with the given path exists
     * @param path - the path of the file
     * @return a boolean representing whether or not the file exists
     */
    private boolean checkFileExists(final String path) {
        return new File(path).exists();
    }

    /**
     * Checks if a directory exists and is a directory
     * @param path - the path of the directory
     * @return a boolean representing whether or not the directory is a directory
     */
    private boolean checkIsDir(final String path) {
        return new File(path).isDirectory();
    }

    /**
     * Reads the contents line by line in a file
     * @param path - the path to the file to read
     * @return a list of strings, where each element in the list is a line in the read file
     */
    private List<String> readFromFile(final String path) {
        try {
            return Files.readAllLines(Paths.get(path), Charset.defaultCharset());
        } catch (IOException e) {
            Assert.fail("Failed to read from file:  " + path + " with error: " + e.getMessage());
            return null; // So compiler is happy
        }
    }
}
