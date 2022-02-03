package com.minifiedspotifywrapped;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Stream {

    final private Calendar endTime;
    final private String artist;
    final private String track;
    final private int msPlayed;

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
    public static ArrayList<Stream> generate(ArrayList<File> files) {

        // Create streams ArrayList
        ArrayList<Stream> streams = new ArrayList<>();

        // Read files
        for(File file : files) {

            // Create a scanner
            Scanner scanner = null;
            try {
                scanner = new Scanner(file);
                scanner.useDelimiter(Pattern.compile("\\[\\s*\\{[\\r\\n]|\\{|\\s*},\\s*\\{[\\r\\n]|\\s*}\\s]"));
            }
            catch (FileNotFoundException exception) {
                System.err.println("The file was not found.");
                return null;
            }

            // Read files
            while(scanner.hasNext()) {
                streams.add(new Stream(scanner.next()));
                break;
            }

        } // for(File file : Files)

        // Return streams
        return streams;

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
