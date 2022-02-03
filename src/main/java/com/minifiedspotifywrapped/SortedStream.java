package com.minifiedspotifywrapped;

public class SortedStream implements Comparable<SortedStream> {

	final private String field;     // Artist or Track
	final private int seconds;
	final private float minutes;
	final private float hours;
	final private float days;


	public SortedStream(String field, int seconds) {
		this.field = field;
		this.seconds = seconds;
		this.minutes = (float) seconds / 60;
		this.hours = minutes / 60;
		this.days = hours / 24;
	}


	@Override
	public int compareTo(SortedStream s) {
		return Integer.compare(seconds, s.seconds);
	}


	@Override
	public String toString() {
		return field + ": " + seconds + " seconds, " +
			String.format("%1.2f", minutes) + " minutes, " +
			String.format("%1.2f", hours) + " hours, " +
			String.format("%1.2f", days) + "days.\r\n";
	}

}
