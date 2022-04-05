package com.minifiedspotifywrapped;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

public class Application {

	// Setup variables to be changed by user
	private static File directory = null;
	private static int amount = 10, year = Calendar.getInstance().get(Calendar.YEAR);

	// Setup variables to be changed by the program when changes are made
	private static boolean isRead = false;          // Whether the files are read
	private static boolean isGenerated = false;     // Whether the output is computed for this year and amount
	private static boolean isSorted = false;        // Whether it's sorted accordingly already


	/**
	 * Keeps asking the user for a valid path.
	 *
	 * @param user the scanner through which the user input is given
	 * @param current the current directory
	 * @return the path
	 */
	private static File getDirectory(Scanner user, File current) {

		File directory, history;
		do {
			String n = user.nextLine();
			if(n.equals("") && current != null) return current;
			directory = new File(n);
			history = new File(directory.getAbsolutePath() + "\\StreamingHistory0.json");
		}
		while(!directory.isDirectory() || !history.isFile());

		isRead = false;
		return directory;

	}


	/**
	 * Keeps asking the user for a valid amount.
	 *
	 * @param user the scanner through which the user input is given
	 * @param current the current amount
	 * @return the amount
	 */
	private static int getAmount(Scanner user, int current) {

		int amount = -1;
		do {
			String n = user.nextLine();
			if(n.equals("")) return current;
			try {
				amount = Integer.parseInt(n);
			} catch(Exception ignored) {}
		}
		while(amount < 0);

		isGenerated = false;
		return amount;

	}


	/**
	 * Keeps asking the user for a valid year.
	 *
	 * @param user the scanner through which the user input is given
	 * @param current the current year
	 * @return the year
	 */
	private static int getYear(Scanner user, int current) {

		int year = -1;
		do {
			String n = user.nextLine();
			if(n.equals("")) return current;
			try {
				year = Integer.parseInt(n);
			} catch(Exception ignored) {}
		}
		while(year <= 2000);

		isGenerated = false;
		return year;

	}


	/**
	 * Sets the variables for the program.
	 *
	 * @param user the scanner used to get the user input
	 */
	private static void setVariables(Scanner user) {

		// Print opening and get path
		System.out.println("Please insert the path to the folder that contains `StreamingHistoryX.json`, " +
			"X being an integer >= 0. Defaults to " + (directory == null ? "null" : directory.getAbsolutePath()) + ".");
		directory = getDirectory(user, directory);

		// Get amount of items to show
		System.out.println("Please insert the how many top tracks and artists you want to see. " +
			"Enter 0 if you want no boundary (not recommended). Defaults to " + amount + ".");
		amount = getAmount(user, amount);

		// Select the year
		System.out.println("Please insert the year for which you want the results. " +
			"Defaults to " + year + ".");
		year = getYear(user, year);

	}


	/**
	 * Generates the Minified Spotify Wrapped.
	 *
	 * @param user the scanner used to get the user input
	 */
	private static void generate(Scanner user) {

		setVariables(user);
		Stream.getStreams(directory);
		isRead = true;
		// Compute total time listened in total, per track, per artist
		// Compute total streams in total, per track, per artist
		// Print results
		// Save in requested format(s)

	}


	/**
	 * The method that is called when the program is opened.
	 *
	 * @param args The arguments
	 */
	public static void main(String[] args) {

		// Print opening
		System.out.println("""
				Minified Spotify Wrapped
			================================
			If you want the default input to be used, simply press enter without entering any value.""");

		// Initialise a scanner that takes user input
		Scanner user = new Scanner(System.in);

		// Loop as long as the user wants to repeat
		boolean repeat;
		do {

			// Generate the output
			generate(user);

			// Ask if they want to generate again
			System.out.println("Do you want to generate another report? (Y/N)");
			String repeating = user.nextLine().toLowerCase(Locale.ROOT);
			repeat = repeating.equals("y") || repeating.equals("yes");

		}
		while(repeat);

		System.out.println("Thanks for using Minified Spotify Wrapped!");

	}

}
