package com.minifiedspotifywrapped;

import com.minifiedspotifywrapped.saving.JsonStrategy;
import com.minifiedspotifywrapped.saving.MdStrategy;
import com.minifiedspotifywrapped.saving.SavingStrategy;
import com.minifiedspotifywrapped.saving.TxtStrategy;
import com.minifiedspotifywrapped.sorting.SortingStrategy;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class Report {

	private float[] totalTimeListened;
	private ArrayList<SortedStream> tracks;
	private ArrayList<SortedStream> artists;
	private int amount;
	private int year;
	private File directory = null;

	/**
	 * Report constructor.
	 *
	 * @param amount how many items to show
	 * @param year the year the data is computed for
	 */
	public Report(int amount, int year) {
		this.amount = amount;
		this.year = year;
	}

	/**
	 * Sorts the data using the correct strategy.
	 *
	 * @param strategy the strategy to sort with
	 */
	public void sort(SortingStrategy strategy) {
		SortedStream.setStrategy(strategy);
		tracks = (ArrayList<SortedStream>) tracks.stream().sorted().collect(Collectors.toList());
		artists = (ArrayList<SortedStream>) artists.stream().sorted().collect(Collectors.toList());
	}

	/**
	 * Prints the report.
	 */
	public void show() {
		TxtStrategy txtStrategy = new TxtStrategy();
		System.out.println(txtStrategy.generate(this));
	}

	/**
	 * Saves the data in the wished formats.
	 *
	 * @param formats the formats to save the data in
	 */
	public void save(String formats) {

		// Parse formats and create strategies
		ArrayList<SavingStrategy> savingStrategies = new ArrayList<>();
		for(String format : formats.toLowerCase(Locale.ROOT).split(",\\s*")) {
			switch (format) {
				case "txt" -> savingStrategies.add(new TxtStrategy());
				case "json" -> savingStrategies.add(new JsonStrategy());
				case "md" -> savingStrategies.add(new MdStrategy());
			}
		}

		// Save in the correct formats
		for(SavingStrategy strategy : savingStrategies) {
			strategy.save(this);
		}

	}


	public int getAmount() {
		return amount;
	}

	public int getYear() {
		return year;
	}

	public File getDirectory() {
		return directory;
	}

	public float[] getTotalTimeListened() {
		return totalTimeListened;
	}

	public ArrayList<SortedStream> getTracks() {
		return tracks;
	}

	public ArrayList<SortedStream> getArtists() {
		return artists;
	}


	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
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
