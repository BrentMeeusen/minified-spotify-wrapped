package com.minifiedspotifywrapped;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Application {

	private static void print(String[] arr) {
		for(String s : arr) {
			System.out.print(s + ", ");
		}
		System.out.println("done.");
	}

    /**
     * The method that is called when the program is opened.
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
			return;
        }

		// Get the flags
	    int amount = getAmountFromFlags(args);
	    if(amount == -99) { return; }

	    int year = getYearFromFlags(args);
	    if(year == -99) { return; }

        // Parse JSON files to Stream instances and generate report
        Stream.generate(history);
        System.out.println(Stream.generateReport(amount, year));

    }


    /**
     * Gets the Streaming History files.
     *
     * @param path The directory.
     * @return an ArrayList of files
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
		File file = new File(directory.getAbsolutePath() + "\\StreamingHistory" + i++ + ".json");
        while(file.isFile()) {
			history.add(file);
            file = new File(directory.getAbsolutePath() + "\\StreamingHistory" + i++ + ".json");
        }

        // Return files found
        return history;

    }


	/**
	 * Gets the amount of artists and tracks to list from the flags.
	 *
	 * @param flags The flags to search in
	 * @return The amount
	 */
	private static int getAmountFromFlags(String[] flags) {

		int amount = 10;
		for(int i = 0; i < flags.length; i++) {
			if(flags[i].equals("-f")) {
				amount = -1;
			} else if(flags[i].equals("-n")) {
				try {
					amount = Integer.parseInt(flags[++i]);
				}
				catch(Exception e) {
					System.err.println("Please insert an integer as limit.");
					return -99;
				}
			}
		}
		return amount;

	}


	/**
	 * Gets the amount of artists and tracks to list from the flags.
	 *
	 * @param flags The flags to search in
	 * @return The amount
	 */
	private static int getYearFromFlags(String[] flags) {

		int year = Calendar.getInstance().get(Calendar.YEAR);
		for(int i = 0; i < flags.length; i++) {
			if(flags[i].equals("-y")) {
				try {
					year = Integer.parseInt(flags[++i]);
				}
				catch(Exception e) {
					System.err.println("Please insert an integer as year.");
					return -99;
				}
			}
		}
		return year;

	}

}
