package com.minifiedspotifywrapped;

import com.minifiedspotifywrapped.sorting.AlphabeticalSortingStrategy;
import com.minifiedspotifywrapped.sorting.SortingStrategy;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Application {

	// Setup variables to be changed by user
	private static File directory = null;
	private static int amount = 10, year = Calendar.getInstance().get(Calendar.YEAR), sort = 3;

	// Setup variables to be changed by the program when changes are made
	private static boolean isRead = false;          // Whether the files are read
	private static boolean isGenerated = false;     // Whether the output is computed for this year and amount
	private static boolean isSorted = false;        // Whether it's sorted accordingly already

	// Setup streams and report variables
	private static ArrayList<Stream> streams = new ArrayList<>();
	private final static Report report = new Report(amount);


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
	 * Keeps asking the user for a valid sort.
	 *
	 * @param user the scanner through which the user input is given
	 * @param current the current sort
	 * @return the sort
	 */
	private static int getSort(Scanner user, int current) {

		int sort = -1;
		do {
			String n = user.nextLine();
			if(n.equals("")) return current;
			try {
				sort = Integer.parseInt(n);
			} catch(Exception ignored) {}
		}
		while(sort < 1 || sort > 6);

		isSorted = false;
		return sort;

	}


	/**
	 * Gets the correct sorting strategy from an integer.
	 *
	 * @param sort the chosen strategy
	 * @return the strategy
	 */
	private static SortingStrategy getSortingStrategy(int sort) {
		return switch (sort) {
			case 1 -> new AlphabeticalSortingStrategy(true);
			case 2 -> new AlphabeticalSortingStrategy(false);
			default -> null;
		};
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
		report.setAmount(amount);

		// Select the year
		System.out.println("Please insert the year for which you want the results. " +
			"Defaults to " + year + ".");
		year = getYear(user, year);

		// Select the sorting
		System.out.println("Please choose how you want the data to be sorted. Defaults to " + sort + ".");
		System.out.println("""
            1. Alphabetically (A-Z)
            2. Alphabetically (Z-A)
            3. Most streams
            4. Least streams
            5. Most time spent listening
            6. Least time spent listening""");
		sort = getSort(user, sort);

	}


	/**
	 * Generates the Minified Spotify Wrapped.
	 *
	 * @param user the scanner used to get the user input
	 */
	private static void generate(Scanner user) {

		// Let the user set the variables
		setVariables(user);

		// Read streams if a new set of files is loaded
		if(!isRead) {

			System.out.print("Reading data...\r");
			streams = Stream.getStreams(directory);
			isRead = true; isGenerated = false;

		}

		// Compute streams and total time listened in total, per track, per artist if not done already
		if(!isGenerated) {

			System.out.print("Calculating your results...\r");
			assert streams != null;
			ArrayList<Stream> currentStreams = (ArrayList<Stream>) streams.stream()
				.filter(s -> s.getEndTime().get(Calendar.YEAR) == year).collect(Collectors.toList());

			float[] timeListened = Stream.getTotalTimeListened(currentStreams, year);
			report.setTotalTimeListened(timeListened);
			report.setTracks(Stream.getTimeListenedPerTrack(currentStreams, timeListened[1]));
			report.setArtists(Stream.getTimeListenedPerArtist(currentStreams, timeListened[1]));

			isGenerated = true; isSorted = false;

		}

		// Sort the data if required
		if(!isSorted) {
			report.sort(getSortingStrategy(sort));
		}

		// Print results
		System.out.print("                           \r");
		report.show();

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
			If you want the default input to be used, simply press enter without entering any value.
			Your input is accepted when the program asks a new question.""");

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

		System.out.println("Thank you for using Minified Spotify Wrapped!");

	}

}
