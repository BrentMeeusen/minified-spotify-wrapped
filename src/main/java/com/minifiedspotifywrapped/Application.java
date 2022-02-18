package com.minifiedspotifywrapped;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

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
     * @param args The arguments
     */
    public static void main(String[] args) {

		// Initialise a scanner that takes user input and print the opening
	    System.out.println("Minified Spotify Wrapped\r\n" +
		                   "========================\r\n");
		showCommands();

	    Scanner user = new Scanner(System.in);

	    // Ask for command and check its validity
	    String command = user.next().toLowerCase(Locale.ROOT);
	    while(!(command.equals("exit") || command.equals("quit"))) {

			// Select command
		    switch(command) {

			    case "help":
					showCommands();
					break;

			    case "":
					break;

			    default:
					System.out.println("Unknown command. Please type \"help\" to see a list of commands.");

		    }

			// Ask for next command
			command = user.next();

	    }




//        // If path is not filled in: return
//        if(args.length == 0) {
//            System.err.println("Please provide the path to the Spotify Data folder. " +
//                "You can request the data here: https://www.spotify.com/account/privacy/");
//            return;
//        }
//
//        // Get the Streaming History files
//        ArrayList<File> history = getStreamingHistoryFiles(args[0]);
//        if(history == null) {
//            return;
//        } else if(history.size() == 0) {
//            System.err.println("There are no files in the directory that match the format. " +
//                "Please make sure that the directory is correct.");
//			return;
//        }
//
//		// Get the flags
//	    int amount = getAmountFromFlags(args);
//	    if(amount == -99) { return; }
//
//	    int year = getYearFromFlags(args);
//	    if(year == -99) { return; }
//
//        // Parse JSON files to Stream instances and generate report
//        Stream.generate(history);
//        System.out.println(Stream.generateReport(amount, year));

    }


	/**
	 * Prints the valid commands.
	 */
	private static void showCommands() {
		System.out.println("Commands:\r\n" +
			"help - Show a list of commands.\r\n" +
			"variables - Show the variables that are currently initialised.\r\n" +
			"path - Specify the path to the folder in which the StreamingHistoryX.json files are located.\r\n" +
			"amount - Set the number of tracks and artists to show.\r\n" +
			"full - Show the full report, including all tracks and artists.\r\n" +
			"year - Set the year for which the data should be looked up.\r\n");
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
