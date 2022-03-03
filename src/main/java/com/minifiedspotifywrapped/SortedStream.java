package com.minifiedspotifywrapped;

public class SortedStream implements Comparable<SortedStream> {

	final private String field;     // Artist or Track
	final private float percentage;
	final private int seconds;
	final private float minutes;
	final private float hours;
	final private float days;


	public SortedStream(String field, int seconds, long secondsListened) {
		this.field = field;
		this.seconds = seconds;
		this.minutes = (float) seconds / 60;
		this.hours = minutes / 60;
		this.days = hours / 24;
		this.percentage = (float) seconds / secondsListened * 100;
		if(field.equals("Kensington")) {
			System.out.println(this.seconds + ", " + secondsListened + "," + percentage);
		}
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
			String.format("%1.2f", days) + "days (" + String.format("%1.2f", percentage) + "%).\r\n";
	}

}
