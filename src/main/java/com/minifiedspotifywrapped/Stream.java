package com.minifiedspotifywrapped;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.InputMismatchException;
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


	// Non-static getters and setters
    public String getArtist() {
        return artist;
    }

	public String getTrack() {
		return track;
	}


	// Static getters and setters
	public static String getPath() {
		return path;
	}

	public static void setPath(String path) {
		Stream.path = path;
	}

	public static int getAmount() {
		return amount;
	}

	public static void setAmount(int amount) {
		Stream.amount = amount;
	}

	public static int getYear() {
		return year;
	}

	public static void setYear(int year) {
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

		System.out.println("Insert value for \"" + variable + "\":");

		// Set the type of variable based on the input
		switch(variable) {

			// If it's path: keep asking paths until file is directory
			case "path":
				File file = new File(scanner.next());
				while(!file.isDirectory()) {
					file = new File(scanner.next());
				}
				setPath(file.getAbsolutePath());
				break;

			// If it's amount: keep asking until a positive integer is given
			case "amount":
				int amount = -1;
				while(amount <= 0) {
					try {
						amount = scanner.nextInt();
					} catch(InputMismatchException ime) {
						System.out.println("Please input a positive integer.");
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
				int year = -1;
				while(year <= 2000) {
					try {
						year = scanner.nextInt();
					} catch(InputMismatchException ime) {
						System.out.println("Please input a positive integer.");
					}
				}
				setYear(year);
				break;

			default:
				System.out.println("Cannot set \"" + variable + "\".\r\n");
		}

	}


    /**
     * Creates an ArrayList of streams from an ArrayList of files
     *
     * @param files The files to read from
     */
    public static void generate(ArrayList<File> files) {

        // Create streams ArrayList
        ArrayList<Stream> streams = new ArrayList<>();

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
			}

            // Read files
            while(scanner.hasNext()) {
                String test = scanner.next();
                if(test.contains("}")) {
					System.out.println(test);
					test = test.substring(0, test.indexOf("}") - 3);
                }
                streams.add(new Stream(test));
            }

			scanner.close();

        } // for(File file : Files)

        // Set streams variable
        Stream.streams = streams;

    }


    /**
     * Generates the report.
     *
     * @param max the number of elements to show
     * @return the report
     */
    public static String generateReport(int max, int year) {

		// Commented 30s so that it fits my test dataset
        Stream.streams = (ArrayList<Stream>) Stream.streams.stream()
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
	        + getTotalTimeListened(max, Stream::getArtist) + "\r\nIn total, you listened per track:\r\n"
	        + getTotalTimeListened(max, Stream::getTrack) + "\r\n";

    }


    /**
     * Gets the total time listened.
     *
     * @return the total time spent listening to Spotify
     */
    private static String getTotalTimeListened() {

        // Calculate number of seconds listened
        int seconds = Stream.streams.stream()
            .map(s -> s.msPlayed / 1000)
            .reduce(0, (a, b) -> a + b);

        // Calculate different time measures
        float minutes = (float) seconds / 60;
        float hours = minutes / 60;
        float days = hours / 24;

        // Return in format
        return seconds + " seconds\r\n"
            + String.format("%1.2f", minutes) + " minutes\r\n"
            + String.format("%1.2f", hours) + " hours\r\n"
            + String.format("%1.2f", days) + " days\r\n";

    }


    /**
     * Gets the total time listened.
     *
     * @param max the number of elements to show
     * @param function what parameter we're looking for
     * @return the total time spent listening to Spotify
     */
    private static String getTotalTimeListened(int max, Function<Stream, String> function) {

        // Calculate number of seconds listened
        Map<String, List<Stream>> grouped = Stream.streams.stream().collect(Collectors.groupingBy(function));
	    ArrayList<SortedStream> sorted = new ArrayList<>();

		// Get seconds for each artist
	    for(String key : grouped.keySet()) {

			List<Stream> streams = grouped.get(key);
			int secs = streams.stream()
				.map(s -> s.msPlayed / 1000)
				.reduce(0, (a, b) -> a + b);

			sorted.add(new SortedStream(key, secs));

	    }

		// Sort array and get top x elements to string
		sorted = (ArrayList<SortedStream>) sorted.stream().sorted().collect(Collectors.toList());

	    String res = "";
		max = max <= 0 ? sorted.size() : max;       // top x < 0? make it max
	    max = Math.min(max, sorted.size());         // max > sorted.size? make it sorted.size to prevent IOOB
		for(int i = 0; i < max; i++) {
			res += sorted.get(sorted.size() - i - 1);
		}

        // Return in format
		return res;

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
