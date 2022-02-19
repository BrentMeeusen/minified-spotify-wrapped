package com.minifiedspotifywrapped;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

public class Application {

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
					Stream.setVariable(command);

				case "show":
					Stream.showResults();

				case "save":
					Stream.saveResults();

				case "sort":
					comingSoon();
					break;

				default:
					System.out.println(
						"Unknown command. Please type \"help\" to see a list of commands.");

			}

			// Ask for next command
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
			"path - Specify the path to the folder in which the StreamingHistoryX.json files are located.\r\n" +
			"amount - Set the number of tracks and artists to show.\r\n" +
			"full - Show the full report, including all tracks and artists.\r\n" +
			"year - Set the year for which the data should be looked up.\r\n" +
			"show - Shows the output in the console.\r\n" +
			"[N/A] save - Saves the output in the supported formats you wish. The output is saved in the folder where it is reading the data from.\r\n" +
			"[N/A] sort - Allows you to choose whether you want to sort on time listened or number of streams.\r\n" +
			"exit/quit - Quit the program.\r\n");
	}


	/**
	 * Prints the variables with which the program will calculate the Wrapped statistics.
	 */
	private static void showVariables() {
		System.out.println(Stream.getVariables());
	}

}
