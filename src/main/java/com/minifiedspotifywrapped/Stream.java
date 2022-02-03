package com.minifiedspotifywrapped;

import java.io.File;
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

    final private Calendar endTime;
    private String artist;
    private String track;
    private int msPlayed;

    private static ArrayList<Stream> streams;


    public String getArtist() {
        return artist;
    }

	public String getTrack() {
		return track;
	}

	/**
     * Creates a stream from a formatted string.
     *
     * @param stream The format
     */
    private Stream(String stream) {

        // Create a scanner
        Scanner scanner = null;
        scanner = new Scanner(stream);
        scanner.useDelimiter(Pattern.compile("\"?,?\\s*\"[\\w\\W][^\"]*\"\\s*:\\s*\"?"));

        // Get endTime
        String[] values = scanner.next().split("[-\\s:]");
        endTime = Calendar.getInstance();
        endTime.set(Integer.parseInt(values[0]), Integer.parseInt(values[1]) - 1,
            Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4])
        );

        // Get artist, track, msPlayed
	    if(!scanner.hasNext()) {
			System.err.println("Error: no next artist!" + stream);
			return;
	    }
        artist = scanner.next();
	    if(!scanner.hasNext()) {
		    System.err.println("Error: no next track! " + stream);
		    return;
	    }
        track = scanner.next();
	    if(!scanner.hasNextInt()) {
		    System.err.println("Error: no next msPlayed!" + stream);
		    return;
	    }
        msPlayed = scanner.nextInt();

    }


    /**
     * Creates an ArrayList of streams from an ArrayList of files
     *
     * @param files The files to read from
     */
    public static void generate(ArrayList<File> files) {

        // Create streams ArrayList
        ArrayList<Stream> streams = new ArrayList<>();
		ArrayList<String> skip = new ArrayList<>();
        int read = 0, skipped = 0;

		// Print files
	    System.out.println(files);

        // Read files
	    // TODO: Add out/artifacts/MSW.jar to GitHub
	    // TODO: Create release 1.0.0 on GitHub
        for(File file : files) {

			System.out.println("Reading from " + file + "...");

            // Create a scanner
            Scanner scanner = null;
            try {
                scanner = new Scanner(file);
                scanner.useDelimiter(
					Pattern.compile("\\[\\s*\\{[\\r\\n]|\\{|\\s*},\\s*\\{[\\r\\n]|\\s*}\\s]")
                );
            }
            catch (FileNotFoundException exception) {
                System.err.println("The file was not found.");
                return;
            }

            // Read files
	        String tmp = "";
            while(scanner.hasNext()) {
                String test = scanner.next();
				tmp = test;
                if(test.contains("}")) {
					skipped++;
					System.out.println("Before: " + test);
					test = test.substring(0, test.indexOf("}") - 3);
	                System.out.println("After: " + test);
                }
	            read++;
                streams.add(new Stream(test));
            }
			System.out.println("Ends with " + tmp);

        } // for(File file : Files)

        // Set streams variable
        Stream.streams = streams;
		System.out.println("Got " + streams.size() + " streams (" + read + ", " + skipped + ").");
//        System.err.println("Read " + read + " tracks.");
//        System.err.println("Skipped " + skipped + " tracks.");
//		System.err.println(skip + "\r\n");

    }


    /**
     * Generates the report.
     *
     * @param max the number of elements to show
     * @return the report
     */
    public static String generateReport(int max) {

        // Filter only tracks that have been listened to this year
        int year = Calendar.getInstance().get(Calendar.YEAR);

		// Commented 30s so that it fits my test dataset
        Stream.streams = (ArrayList<Stream>) Stream.streams.stream()
            .filter(s -> s.endTime.get(Calendar.YEAR) == year)      // Only tracks that are played this year
//            .filter(s -> s.msPlayed >= 30000)                       // Only tracks played longer than 30s
            .collect(Collectors.toList());

	    System.out.println("Got " + streams.size() + " streams.");

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
		max = max <= 0 ? sorted.size() : max;
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
