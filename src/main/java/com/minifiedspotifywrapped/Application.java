package com.minifiedspotifywrapped;

import java.io.File;
import java.util.ArrayList;

public class Application {

    /**
     * The method that will run.
     *
     * First argument (required): path to downloaded Spotify data.
     * Optional arguments:
     *   -f     Full output
     *   -n [n] Number of tracks and artists to show. Default: 10
     *
     * @param args The arguments
     */
    public static void main(String[] args) {

        // If path is not filled in: return
        if(args.length == 0) {
            System.err.println("Please provide the path to the Spotify Data folder. " +
                "You can request the data here: https://www.spotify.com/account/privacy/");
            return;
        }

        // Get the Streaming History files
        ArrayList<File> history = getStreamingHistoryFiles(args[0]);
        if(history == null) {
            return;
        } else if(history.size() == 0) {
            System.err.println("There are no files in the directory that match the format. " +
                "Please make sure that the directory is correct.");
        }

        // Parse JSON files to instances
        Stream.generate(history);
        Stream.generateReport();

    }


    /**
     * Gets the Streaming History files.
     *
     * @param path The directory.
     * @return
     */
    private static ArrayList<File> getStreamingHistoryFiles(String path) {

        // Initialise ArrayList, get path to directory
        ArrayList<File> history = new ArrayList<>();
        File directory = new File(path);

        // If it's not a directory, return false
        if(!directory.isDirectory()) {
            System.err.println("The given path is not a directory.");
            return null;
        }

        // Get history files
        int i = 0;
        while(true) {
            File file = new File(directory.getAbsolutePath() + "\\StreamingHistory" + i++ + ".json");
            if(file.isFile()) {
                history.add(file);
            } else {
                break;
            }
        }

        // Return files found
        return history;

    }

}
