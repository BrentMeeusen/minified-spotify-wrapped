package com.minifiedspotifywrapped;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Stream {

<<<<<<< HEAD
    final private Calendar endTime;
    final private String artist;
    final private String track;
    final private int msPlayed;

    private static ArrayList<Stream> streams;
=======
    private Calendar endTime;
    private String artist;
    private String track;
    private int msPlayed;
>>>>>>> ad4010f38a0a3a2ad51b46277b34569115586b5e

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
        artist = scanner.next();
        track = scanner.next();
        msPlayed = scanner.nextInt();

    }


    /**
     * Creates an ArrayList of streams from an ArrayList of files
     *
     * @param files The files to read from
     * @return an ArrayList of Stream instances
     */
    public static void generate(ArrayList<File> files) {

        // Create streams ArrayList
        ArrayList<Stream> streams = new ArrayList<>();
        int i = 0;

        // Read files
        for(File file : files) {

            // Create a scanner
            Scanner scanner = null;
            try {
                scanner = new Scanner(file);
                scanner.useDelimiter("\\[\\s*\\{[\\r\\n]|\\{|\\s*},\\s*\\{[\\r\\n]|\\s*}\\s]");
            }
            catch (FileNotFoundException exception) {
                System.err.println("The file was not found.");
                return;
            }

            // Read files
            while(scanner.hasNext()) {
                String test = scanner.next();
                if(test.contains("}")) {
                    i++;
                    continue;
                }
                streams.add(new Stream(test));
            }

        } // for(File file : Files)

        // Set streams variable
        Stream.streams = streams;
        if(i > 0) {
            System.err.println("Skipped " + i + " tracks.");
        }

    }


    /**
     * Generates the report.
     */
    public static String generateReport() {

        // Filter only tracks that have been listened to this year
        int year = Calendar.getInstance().get(Calendar.YEAR);

        Stream.streams = (ArrayList<Stream>) Stream.streams.stream()
            .filter(s -> s.endTime.get(Calendar.YEAR) == year)      // Only tracks that are played this year
            .filter(s -> s.msPlayed >= 30000)                       // Only tracks played longer than 30s
            .collect(Collectors.toList());

        // Get specifics
        String total = getTotalTimeListened();


        // Return formatted
        return "MINIFIED SPOTIFY WRAPPED " + year
            + "\r\n=============================\r\n"
            + total;

    }


    /**
     * Gets the total time listened.
     */
    private static String getTotalTimeListened() {

        // Calculate number of seconds listened
        int seconds = Stream.streams.stream()
            .map(s -> (int) s.msPlayed / 1000)
            .reduce(0, (a, b) -> a + b);

        // Calculate different time measures
        float minutes = (float) seconds / 60;
        float hours = minutes / 60;
        float days = hours / 24;

        // Return in format
        return "In total, you listened: \r\n"
            + seconds + " seconds\r\n"
            + String.format("%1.2f", minutes) + " minutes\r\n"
            + String.format("%1.2f", hours) + " hours\r\n"
            + String.format("%1.2f", days) + " days\r\n";

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
