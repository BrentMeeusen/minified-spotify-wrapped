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
	 * Gets the files from the given path if any are found.
	 *
	 * @param directory the directory to search for the files
	 * @return The files
	 */
	private static File[] getFiles(File directory) {

		// Initialise ArrayList, get path to directory
		ArrayList<File> history = new ArrayList<>();

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
	 * @param directory the directory to search for the files
     */
    public static void getStreams(File directory) {

        // Create streams ArrayList
        ArrayList<Stream> streams = new ArrayList<>();

		// Get all the files in the directory
	    File[] files = getFiles(directory);
		if(files == null) {
			return;
		}

        // Read files
        for(File file : files) {

            // Create a scanner and set a delimiter so that the streams are split
            Scanner scanner;
			try {
				scanner = new Scanner(new FileInputStream(file));
				scanner.useDelimiter(
					Pattern.compile("(\\[|[\\s\\r\\n]*},?)?[\\s\\r\\n]*(\\{[\\r\\n]*|[\\r\\n]])")
				);
			}
			catch(FileNotFoundException fnfe) {
				System.err.println("Error: could not find the file.");
				System.err.println(fnfe.getMessage());
				return;
			}

            // Read the streams one by one and add them to the streams list
            while(scanner.hasNext()) {
                streams.add(new Stream(scanner.next()));
            }

			scanner.close();

        } // for(File file : Files)

        // Filter out streams that were played for less than 30 seconds
	    streams = (ArrayList<Stream>) streams.stream()
		    .filter(s -> s.msPlayed >= 30000).collect(Collectors.toList());

		// Set the streams variable
        Stream.streams = streams;

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
	    Calendar start = Calendar.getInstance();

	    long totalMs = streams.get(streams.size() - 1).getEndTime().getTimeInMillis() - start.getTimeInMillis();
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
