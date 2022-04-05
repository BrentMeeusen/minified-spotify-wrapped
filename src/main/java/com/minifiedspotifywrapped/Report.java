package com.minifiedspotifywrapped;

import java.util.ArrayList;
import java.util.Arrays;

public class Report {

	private float[] totalTimeListened;
	private ArrayList<SortedStream> tracks;
	private ArrayList<SortedStream> artists;
	private int amount;

	public Report(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
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
