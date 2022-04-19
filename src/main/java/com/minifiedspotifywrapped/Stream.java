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
            Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4]));

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
	private static ArrayList<File> getFiles(File directory) {

		// Initialise ArrayList, get path to directory
		ArrayList<File> history = new ArrayList<>();

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
     * Creates an ArrayList of streams.
	 *
	 * @param directory the directory to search for the files
	 * @return the streams
     */
    public static ArrayList<Stream> getStreams(File directory) {

        // Create streams ArrayList
        ArrayList<Stream> streams = new ArrayList<>();

		// Get all the files in the directory
	    ArrayList<File> files = getFiles(directory);

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
			catch(FileNotFoundException ignored) {
				return null;
			}

            // Read the streams one by one and add them to the streams list
            while(scanner.hasNext()) {
                streams.add(new Stream(scanner.next()));
            }
			scanner.close();

        } // for(File file : Files)

        // Filter out streams that were played for less than 30 seconds
	    return (ArrayList<Stream>) streams.stream()
		    .filter(s -> s.msPlayed >= 30000).collect(Collectors.toList());

    }


    /**
     * Gets the total time listened.
     *
     * @param streams the streams to get the total time listened from
     * @param year the year to get the information for
     * @return the total time spent listening to Spotify as percentage, in seconds, minutes, hours, days.
     */
    public static float[] getTotalTimeListened(ArrayList<Stream> streams, int year) {

        // Calculate number of seconds listened
        float secondsListened = (float) streams.stream()
            .map(s -> s.msPlayed / 1000)
            .reduce(0, Integer::sum);

        // Calculate different time measures
        float minutes = secondsListened / 60;
        float hours = minutes / 60;
        float days = hours / 24;

		// Calculate the percentage of time listened in total
	    Calendar start = Calendar.getInstance();
		start.set(year, Calendar.JANUARY, 1, 0, 0, 0);

	    long totalMs = streams.get(streams.size() - 1).getEndTime().getTimeInMillis() - start.getTimeInMillis();
		float secondsSinceStart = (float) totalMs / 1000;
		float percentage = secondsListened / secondsSinceStart * 100;

        // Return in format
        return new float[] { percentage, secondsListened, minutes, hours, days };

    }


	/**
	 * Gets the time listened per track.
	 *
	 * @param streams the streams to get the total time listened from
	 * @param secondsListened how many seconds the user has listened to Spotify
	 * @return the time listened per track in sortable instances
	 */
	public static ArrayList<SortedStream> getTimeListenedPerTrack(ArrayList<Stream> streams, float secondsListened) {
		return getTotalTimeListened(streams, Stream::getTrack, secondsListened);
	}


	/**
	 * Gets the time listened per artist.
	 *
	 * @param streams the streams to get the total time listened from
	 * @param secondsListened how many seconds the user has listened to Spotify
	 * @return the time listened per artist in sortable instances
	 */
	public static ArrayList<SortedStream> getTimeListenedPerArtist(ArrayList<Stream> streams, float secondsListened) {
		return getTotalTimeListened(streams, Stream::getArtist, secondsListened);
	}


    /**
     * Gets the total time listened per track/artist.
     *
     * @param streams the streams to get the total time listened from
     * @param function what parameter we're looking for
     * @param secondsListened how many seconds the user has listened to Spotify
     * @return the SortedStream instances with the amount of time spent listening
     */
    private static ArrayList<SortedStream> getTotalTimeListened(ArrayList<Stream> streams, Function<Stream, String> function, float secondsListened) {

        // Calculate number of seconds listened
        Map<String, List<Stream>> grouped = streams.stream().collect(Collectors.groupingBy(function));
	    ArrayList<SortedStream> sortedStreams = new ArrayList<>();

		// Get seconds for each track/artist
	    for(String key : grouped.keySet()) {

			List<Stream> currentStreams = grouped.get(key);
			int secs = currentStreams.stream().map(s -> s.msPlayed / 1000).reduce(0, Integer::sum);
			int numStreams = currentStreams.size();
			sortedStreams.add(new SortedStream(key, secs, numStreams, secondsListened));

	    }

		// Return the found functions
		return sortedStreams;

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
        if(!(other instanceof Stream o)) {
            return false;
        }

	    return endTime.equals(o.endTime)
            && artist.equals(o.artist)
            && track.equals(o.track)
            && msPlayed == o.msPlayed;
    }

}
