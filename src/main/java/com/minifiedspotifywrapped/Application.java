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
	 * @param current the current file
	 * @return the path
	 */
	private static File getDirectory(Scanner user, File current) {
		String n = user.nextLine();
		if(n.equals("") && current != null) return current;

		File file = new File(n);
		while(!file.isDirectory()) {
			file = new File(user.nextLine());
		}
		isRead = false;
		return file;
	}


	/**
	 * Keeps asking the user for a valid amount.
	 *
	 * @param user the scanner through which the user input is given
	 * @param current the current amount
	 * @return the amount
	 */
	private static int getAmount(Scanner user, int current) {
		String n = user.nextLine();
		if(n.equals("")) return current;

		int amount = -1;
		try {
			amount = Integer.parseInt(n);
		} catch(Exception ignored) {}

		while(amount <= 0) {
			try {
				amount = user.nextInt();
			} catch(Exception e) {
				user.nextLine();
			}
		}
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
		String n = user.nextLine();
		if(n.equals("")) return current;

		int year = -1;
		try {
			year = Integer.parseInt(n);
		} catch(Exception ignored) {}

		while(year < 2000) {
			try {
				year = user.nextInt();
			} catch(Exception e) {
				user.nextLine();
			}
		}
		isGenerated = false;
		return year;
	}


	/**
	 * Sets the variables for the program.
	 *
	 * @param user the scanner used to get the user input
	 */
	private static void setVariables(Scanner user) {

		// TODO: fix the bug that causes bad inputs (especially with enters) to use that input
		//  for the next input value (e.g., amount: NaN \n \n 5 should not also prefill year,
		//  which it currently does)

		// Print opening and get path
		System.out.println("Please insert the path to the folder that contains `StreamingHistoryX.json`, " +
			"X being an integer >= 0. Defaults to " + (directory == null ? "null" : directory.getAbsolutePath()) + ".");
		directory = getDirectory(user, directory);

		// Get amount of items to show
		System.out.println("Please insert the amount of tracks and artists you want to see. " +
			"Enter 0 if you want no boundary. Defaults to " + amount + ".");
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
			String repeating = user.next().toLowerCase(Locale.ROOT);
			repeat = repeating.equals("y") || repeating.equals("yes");

		}
		while(repeat);



		System.exit(0);









		// OLD


		// Print opening
		System.out.println("Minified Spotify Wrapped\r\n" +
			"========================\r\n");
		showCommands();
		askCommand();


		// Ask for command and check its validity
		String command = user.nextLine().toLowerCase(Locale.ROOT).replaceAll("\\s+", "");
		while (!(command.equals("exit") || command.equals("quit"))) {

			// Select command
			switch (command) {

				case "help":
					showCommands();
					break;

				case "variables":
					showVariables();
					break;

				case "path":
				case "amount":
				case "full":
				case "year":
//					Stream.setVariable(user, command);
					break;

				case "show":
//					Stream.showResults();
					break;

				case "save":
//					Stream.saveResults();
					comingSoon();
					break;

				case "sort":
//					Stream.setVariable(user, command);
					break;

				default:
					System.out.println(
						"Unknown command. Please type \"help\" to see a list of commands.");

			}

			// Ask for next command
			askCommand();
			command = user.next();

		}

	}


	/**
	 * Prints the valid commands.
	 */
	private static void showCommands() {
		System.out.println("Commands:\r\n" +
			"help - Show a list of commands.\r\n" +
			"variables - Show the variables that are currently initialised.\r\n" +
			"path - Set the path to the folder in which the StreamingHistoryX.json files are located.\r\n" +
			"amount - Set the number of tracks and artists to show.\r\n" +
			"full - Removes the limit of number of tracks and artists to show.\r\n" +
			"year - Set the year for which the data should be looked up.\r\n" +
			"show - Shows the output in the console.\r\n" +
			"[N/A] save - Saves the output in the supported formats you wish. The output is saved in the folder where it is reading the data from.\r\n" +
			"[N/A] sort - Allows you to choose whether you want to sort on time listened or number of streams.\r\n" +
			"exit/quit - Quit the program.\r\n");
	}


	/**
	 * Prints an Insert Command message
	 */
	private static void askCommand() {
		System.out.println("Please insert a command.");
	}


	/**
	 * Prints the variables with which the program will calculate the Wrapped statistics.
	 */
	private static void showVariables() {
//		System.out.println(Stream.getVariables());
	}


	/**
	 * Prints a Coming Soon message
	 */
	private static void comingSoon() {
		System.out.println("This feature is not yet implemented. Keep an eye on the GitHub release page, it might be in the next update! https://github.com/BrentMeeusen/minified-spotify-wrapped/releases");
	}

}
