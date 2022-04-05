package com.minifiedspotifywrapped;

import com.minifiedspotifywrapped.sorting.SortingStrategy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Report {

	private float[] totalTimeListened;
	private ArrayList<SortedStream> tracks;
	private ArrayList<SortedStream> artists;
	private int amount;

	/**
	 * Report constructor.
	 *
	 * @param amount how many items to show.
	 */
	public Report(int amount) {
		this.amount = amount;
	}

	/**
	 * Sorts the data using the correct strategy.
	 *
	 * @param strategy the strategy to sort with
	 */
	public void sort(SortingStrategy strategy) {
		SortedStream.setStrategy(strategy);
		tracks = (ArrayList<SortedStream>) tracks.stream().sorted().collect(Collectors.toList());
	}

	/**
	 * Prints the report.
	 */
	public void show() {
		System.out.println(this.tracks);
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setTotalTimeListened(float[] totalTimeListened) {
		this.totalTimeListened = totalTimeListened;
	}

	public void setTracks(ArrayList<SortedStream> tracks) {
		this.tracks = tracks;
	}

	public void setArtists(ArrayList<SortedStream> artists) {
		this.artists = artists;
	}

	@Override
	public String toString() {
		return "Report{" +
			"totalTimeListened=" + Arrays.toString(totalTimeListened) +
			", tracks=" + tracks +
			", artists=" + artists +
			", amount=" + amount +
			'}';
	}

}
