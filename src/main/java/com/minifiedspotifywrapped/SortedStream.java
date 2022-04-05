package com.minifiedspotifywrapped;

import com.minifiedspotifywrapped.sorting.SortingStrategy;

public class SortedStream implements Comparable<SortedStream> {

	final private String field;     // Artist or Track
	final private float percentage;
	final private int seconds;
	final private float minutes;
	final private float hours;
	final private float days;
	final private int numStreams;

	private static SortingStrategy strategy;


	public SortedStream(String field, int seconds, int numStreams, float secondsListened) {
		this.field = field;
		this.seconds = seconds;
		this.minutes = (float) seconds / 60;
		this.hours = minutes / 60;
		this.days = hours / 24;
		this.percentage = (float) seconds / secondsListened * 100;
		this.numStreams = numStreams;
	}


	public static void setStrategy(SortingStrategy strategy) {
		SortedStream.strategy = strategy;
	}

	public String getField() {
		return field;
	}

	public int getSeconds() {
		return seconds;
	}

	public int getNumStreams() {
		return numStreams;
	}

	@Override
	public int compareTo(SortedStream s) {
		return strategy.sort(this, s);
	}


	@Override
	public String toString() {
		return field + ": " + numStreams + " streams, " +
			seconds + " seconds, " +
			String.format("%1.2f", minutes) + " minutes, " +
			String.format("%1.2f", hours) + " hours, " +
			String.format("%1.2f", days) + "days (" + String.format("%1.2f", percentage) + "%).\r\n";
	}

}
