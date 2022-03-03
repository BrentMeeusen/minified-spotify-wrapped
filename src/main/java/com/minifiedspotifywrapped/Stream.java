package com.minifiedspotifywrapped;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Stream {

	// Stream variables
    final private Calendar endTime;
    final private String artist;
    final private String track;
    final private int msPlayed;

	// Program variables
    private static ArrayList<Stream> streams;
	private static String path;
	private static int amount = 10;
	private static int year = Calendar.getInstance().get(Calendar.YEAR);
	private static long secondsListened = 0;


	// Non-static getters and setters
    public String getArtist() {
        return artist;
    }

	public String getTrack() {
		return track;
	}

	public Calendar getEndTime() {
		return endTime;
	}


	// Static getters and setters
	public static String getPath() {
		return path;
	}

	private static void setPath(String path) {
		Stream.path = path;
	}

	public static int getAmount() {
		return amount;
	}

	private static void setAmount(int amount) {
		Stream.amount = amount;
	}

	public static int getYear() {
		return year;
	}

	private static void setYear(int year) {
		Stream.year = year;
	}


	/**
     * Creates a stream from a formatted string.
     *
     * @param stream The format
     */
    private Stream(String stream) {

        // Create a scanner
        Scanner scanner = new Scanner(stream);
        scanner.useDelimiter(Pattern.compile("(\",)?[\\s\\r\\n]*\"\\w*\"\\s*:\\s*\"?"));

        // Get endTime
        String[] values = scanner.next().split("[-\\s:]");
        endTime = Calendar.getInstance();
        endTime.set(Integer.parseInt(values[0]), Integer.parseInt(values[1]) - 1,
            Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4])
        );

        // Get artist, track, msPlayed
        artist = scanner.next();
        track = scanner.next();
        msPlayed = scanner.nextInt();

		scanner.close();

    }


	/**
	 * Get the variables in the current settings.
	 *
	 * @return The variables in human-readable format
	 */
	public static String getVariables() {
		return "Path:   " + path + "\r\n" +
		   "Amount: " + amount + "\r\n" +
		   "Year:   " + year + "\r\n";
	}


	/**
	 * Sets the variable using user input.
	 *
	 * @param scanner The input scanner
	 * @param variable The variable to set
	 */
	public static void setVariable(Scanner scanner, String variable) {

		// Set the type of variable based on the input
		switch(variable) {

			// If it's path: keep asking paths until file is directory
			case "path":
				System.out.println("Insert value for \"" + variable + "\":");
				File file = new File(scanner.nextLine());
				while(!file.isDirectory()) {
					file = new File(scanner.nextLine());
				}
				setPath(file.getAbsolutePath());
				break;

			// If it's amount: keep asking until a positive integer is given
			case "amount":
				System.out.println("Insert value for \"" + variable + "\":");
				int amount = -1;
				while(amount <= 0) {
					try {
						amount = scanner.nextInt();
					} catch(Exception e) {
						System.out.println("Please input an integer greater than or equal to 1.");
						scanner.nextLine();
					}
				}
				setAmount(amount);
				break;

			// If it's full: set amount to -1
			case "full":
				setAmount(-1);
				break;

			// If it's year: keep asking until integer >= 2000 is given
			case "year":
				System.out.println("Insert value for \"" + variable + "\":");
				int year = -1;
				while(year <= 2000) {
					try {
						year = scanner.nextInt();
					} catch(Exception e) {
						System.out.println("Please input an integer greater than or equal to 2000.");
						scanner.nextLine();
					}
				}
				setYear(year);
				break;

			default:
				System.out.println("Cannot set \"" + variable + "\".\r\n");

		}

	}


	/**
	 * Shows a report of the generated data.
	 */
	public static void showResults() {
		if(generate()) {
			System.out.println("\r\n\r\n" + generateReport(amount, year));
		}
	}


	/**
	 * Gets the files from the given path if any are found.
	 *
	 * @return The files if found, null otherwise
	 */
	private static File[] getFiles() {

		// Initialise ArrayList, get path to directory
		ArrayList<File> history = new ArrayList<>();

		// If it's not a directory, return false
		if(path == null) {
			System.out.println("Please insert a path.");
			return null;
		}

		File directory = new File(path);
		if(!directory.isDirectory()) {
			System.out.println("The given path is not a directory.");
			return null;
		}

		// Get history files
		int i = 0;
		File file = new File(directory.getAbsolutePath() + "\\StreamingHistory" + i++ + ".json");
		while(file.isFile()) {
			history.add(file);
			file = new File(directory.getAbsolutePath() + "\\StreamingHistory" + i++ + ".json");
		}

		// If no files are found, return error
		if(history.size() == 0) {
			System.out.println("No suitable files are found. Make sure the path points to the folder that contains StreamingHistoryX.json files, X being 0 or higher.");
			return null;
		}

		// Return files found
		return history.toArray(File[]::new);

	}


	/**
     * Creates an ArrayList of streams.
	 *
	 * @return True on success, false otherwise
     */
    public static boolean generate() {

        // Create streams ArrayList
        ArrayList<Stream> streams = new ArrayList<>();

		// Get all the files in the directory
	    File[] files = getFiles();
		if(files == null) {
			return false;
		}

        // Read files
        for(File file : files) {

            // Create a scanner
            Scanner scanner = null;
			try {
				scanner = new Scanner(new FileInputStream(file));
				scanner.useDelimiter(
					Pattern.compile("(\\[|[\\s\\r\\n]*},?)?[\\s\\r\\n]*(\\{[\\r\\n]*|[\\r\\n]])")
				);
			}
			catch(FileNotFoundException fnfe) {
				System.err.println("Error: could not find the file.");
				System.err.println(fnfe.getMessage());
				return false;
			}

            // Read files
            while(scanner.hasNext()) {
                streams.add(new Stream(scanner.next()));
            }

			scanner.close();

        } // for(File file : Files)

        // Set streams variable
        Stream.streams = streams;
		return true;

    }


    /**
     * Generates the report.
     *
     * TODO: Rewrite so that its output can also be saved easily; use a design pattern?
     *
     * @param n the number of elements to show
     * @return the report
     */
    public static String generateReport(int n, int year) {

		// Commented 30s so that it fits my test dataset
        streams = (ArrayList<Stream>) streams.stream()
            .filter(s -> s.endTime.get(Calendar.YEAR) == year)      // Only tracks that are played this year
            .filter(s -> s.msPlayed >= 30000)                       // Only tracks played longer than 30s
            .collect(Collectors.toList());

		// Edge case; what if no tracks are found?
		if(streams.size() == 0) {
			return "MINIFIED SPOTIFY WRAPPED " + year
				+ "\r\n=============================\r\n" +
				"No streams were found for this year.";
		}

        // Return formatted
        return "MINIFIED SPOTIFY WRAPPED " + year
            + "\r\n=============================\r\n" +
	        "In total, you listened:\r\n"
	        + getTotalTimeListened() + "\r\nIn total, you listened per artist:\r\n"
	        + getTotalTimeListened(n, Stream::getArtist) + "\r\nIn total, you listened per track:\r\n"
	        + getTotalTimeListened(n, Stream::getTrack) + "\r\n";

    }


    /**
     * Gets the total time listened.
     *
     * @return the total time spent listening to Spotify
     */
    private static String getTotalTimeListened() {

        // Calculate number of seconds listened
        secondsListened = Stream.streams.stream()
            .map(s -> s.msPlayed / 1000)
            .reduce(0, (a, b) -> a + b);

        // Calculate different time measures
        float minutes = (float) secondsListened / 60;
        float hours = minutes / 60;
        float days = hours / 24;

		// Calculate the percentage of time listened in total
	    long totalMs = streams.get(streams.size() - 1).getEndTime().getTimeInMillis() -
		        streams.get(0).getEndTime().getTimeInMillis();
		long totalSeconds = totalMs / 1000;
		float percentage = (float) secondsListened / totalSeconds * 100;

        // Return in format
        return secondsListened + " seconds (" + String.format("%1.2f", percentage) + "%)\r\n"
            + String.format("%1.2f", minutes) + " minutes\r\n"
            + String.format("%1.2f", hours) + " hours\r\n"
            + String.format("%1.2f", days) + " days\r\n";

    }


    /**
     * Gets the total time listened.
     *
     * @param n the number of elements to show
     * @param function what parameter we're looking for
     * @return the total time spent listening to Spotify
     */
    private static String getTotalTimeListened(int n, Function<Stream, String> function) {

        // Calculate number of seconds listened
        Map<String, List<Stream>> grouped = Stream.streams.stream().collect(Collectors.groupingBy(function));
	    ArrayList<SortedStream> sorted = new ArrayList<>();

		// Get seconds for each track/artist
	    for(String key : grouped.keySet()) {

			List<Stream> streams = grouped.get(key);
			int secs = streams.stream()
				.map(s -> s.msPlayed / 1000)
				.reduce(0, (a, b) -> a + b);

			int numStreams = streams.size();

			sorted.add(new SortedStream(key, secs, numStreams, secondsListened));

	    }

		// Sort array and get top x elements to string
		sorted = (ArrayList<SortedStream>) sorted.stream().sorted().collect(Collectors.toList());

		// Set number of elements to read to a value within boundaries
		n = n <= 0 ? sorted.size() : n;       // top x < 0? make it max
	    n = Math.min(n, sorted.size());         // max > sorted.size? make it sorted.size to prevent IOOB

	    // Read the streams
	    StringBuilder res = new StringBuilder();
		for(int i = 0; i < n; i++) {
			res.append(sorted.get(sorted.size() - i - 1));
		}

        // Return in format
		return res.toString();

    }


    @Override
    public String toString() {
        return "Stream{endTime=" + endTime.getTimeInMillis()
            + ",artist=" + artist
            + ",track=" + track
            + ",msPlayed=" + msPlayed
            + "}";
    }


    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }
        if(!(other instanceof Stream)) {
            return false;
        }
        Stream o = (Stream) other;

        return endTime.equals(o.endTime)
            && artist.equals(o.artist)
            && track.equals(o.track)
            && msPlayed == o.msPlayed;
    }

}
