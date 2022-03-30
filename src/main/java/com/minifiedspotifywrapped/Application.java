package com.minifiedspotifywrapped;

import java.util.Locale;
import java.util.Scanner;

public class Application {

	/**
	 * The method that is called when the program is opened.
	 *
	 * @param args The arguments
	 */
	public static void main(String[] args) {


		// TODO: refactor program usage; make it a linear process
		//  Select folder, set amount, ..., choose sorting
		//  Show output. Let user choose what format(s) to output
		//  Give them the option to rerun. If they do so, prefill previous replies if possible

		// Initialise a scanner that takes user input
		Scanner user = new Scanner(System.in);

		// Print opening and get path
		System.out.println("""
			Minified Spotify Wrapped
			========================
			Please insert the path to the folder that contains `StreamingHistoryX.json`, X being an integer >= 0.""");
//		String path = Application.getPath();

		// Get amount of items to show
		System.out.println("""
			Please insert the amount of tracks and artists you want to see. Enter 0 or lower if you want no boundary.""");
//		int amount = Application.getAmount();



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
					Stream.setVariable(user, command);
					break;

				case "show":
					Stream.showResults();
					break;

				case "save":
//					Stream.saveResults();
					comingSoon();
					break;

				case "sort":
					Stream.setVariable(user, command);
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
		System.out.println(Stream.getVariables());
	}


	/**
	 * Prints a Coming Soon message
	 */
	private static void comingSoon() {
		System.out.println("This feature is not yet implemented. Keep an eye on the GitHub release page, it might be in the next update! https://github.com/BrentMeeusen/minified-spotify-wrapped/releases");
	}

}
