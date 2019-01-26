package com.rest.api.leaguetracker.restapileaguetracker.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An API that exposes functionality for reading/writing to the database
 */
public class FileHandler {
    /**
     * Creates a new directory at the specified path.
     * Does not create subdirectories along the way.
     * @param path - the path of the directory (includes directory name)
     * @return a boolean representing whether or not the directory was successfully created
     */
    public boolean createNewDir(final String path) {
        return new File(path).mkdir();
    }

    /**
     * Creates a new file at the specified path.
     * Does not create subdirectories along the way.
     * @param path - the path of the file (includes directory name)
     * @return a boolean representing whether or not the file was successfully created
     */
    public boolean createNewFile(final String path) {
        try {
            return new File(path).createNewFile();
        } catch (IOException e) {
            // TODO: add tracing and trace this error
            return false;
        }
    }

    /**
     * Reads the contents of a file specified by path, and returns them
     * @param path - the path to the file
     * @return a list of strings, where each element in the list is a line in the file
     * returns empty list if the file does not exist
     */
    public List<String> readFile(final String path) {
        if (fileExists(path)) {
            try {
                return Files.readAllLines(Paths.get(path), Charset.defaultCharset());
            } catch (IOException e) {
                // TODO: trace error
                e.printStackTrace();
                return null;
            }
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Writes a value to a file. Overwriting anything already in the file. Creates the file if it does not exist
     * @param path - the path of the file
     * @param value - the value to be written
     * @return a boolean representing whether or not the write was successful
     */
    public boolean writeToFile(final String path, final String value) {
        List<String> linesToWrite = Arrays.asList(value.split("\n"));
        try {
            Files.write(Paths.get(path), linesToWrite, StandardCharsets.UTF_8);
            return true;
        }catch (IOException e) {
            // TODO: trace the exception
            return false;
        }
    }

    /**
     * Writes a value to a file. Overwriting anything already in the file. Creates the file if it does not exist
     * @param path - the path of the file
     * @param value - a list of strings where each element is a line in the file to be written
     * @return a boolean representing whether or not the write was successful
     */
    public boolean writeToFile(final String path, final List<String> value) {
        try {
            Files.write(Paths.get(path), value, StandardCharsets.UTF_8);
            return true;
        }catch (IOException e) {
            // TODO: trace the exception
            return false;
        }
    }

    /**
     * Appends a value to a file on a new line at the bottom of that file
     * @param path - the path to the file
     * @param value - the value to be appended
     * @return a boolean representing whether or not the append was successful
     */
    public boolean appendToFile(final String path, final String value) {
        List<String> linesToAppend = Arrays.asList(value.split("\n"));
        try {
            Files.write(Paths.get(path), linesToAppend, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            return true;
        }catch (IOException e) {
            // TODO: trace the exception
            return false;
        }
    }

    /**
     * Deletes the directory at the path
     * @param path - the path of the directory to be deleted
     * @return a boolean representing whether or not the directory was deleted. (will return true if
     * the directory was not found)
     */
    public boolean deleteDir(final String path) {
        return deleteDir(new File(path));
    }

    /**
     * Deletes the directory at the path
     * @param directory - a java File object that is a directory to be deleted
     * @return a boolean representing whether or not the directory was deleted. (will return true if
     * the directory was not found)
     */
    public boolean deleteDir(final File directory) {
        // Convert the relative path given to a full path
        return deleteDirHelper(new File(directory.getAbsolutePath()));
    }

    /**
     * Recusively deletes all directories and files starting with the one specified
     * @param directory - the top-level directory to delete
     * @return a boolean representing whether or not the directory was deleted. (will return true if
     * the directory was not found)
     */
    private boolean deleteDirHelper(final File directory) {
        File[] files = directory.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    if (!deleteDirHelper(f)) {
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
        return directory.delete();
    }

    /**
     * Deletes the file at the path
     * @param path - the path of the file to be deleted
     * @return a boolean representing whether or not the file was deleted. (will return true if the file was not found)
     */
    public boolean deleteFile(final String path) {
        return new File(path).delete();
    }

    /**
     * Helper method that checks if a file at the path exists
     * @param path - the path of the file
     * @return a boolean that represents whether or not the file exists
     */
    public boolean fileExists(final String path) {
        return new File(path).exists();
    }

    /**
     * Helper method that checks if a directory at the path exists
     * @param path - the path of the directory
     * @return a boolean that represents whether or not the file exists
     */
    public boolean directoryExists(final String path) {
        return new File(path).isDirectory();
    }
}
