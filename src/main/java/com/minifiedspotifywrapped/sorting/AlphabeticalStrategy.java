package com.minifiedspotifywrapped.sorting;

import com.minifiedspotifywrapped.SortedStream;
import java.util.Locale;

public class AlphabeticalStrategy implements SortingStrategy {

	final private boolean isAscending;

	/**
	 * Alphabetical sorting strategy constructor.
	 *
	 * @param isAscending whether it's ascending or descending
	 */
	public AlphabeticalStrategy(boolean isAscending) {
		this.isAscending = isAscending;
	}

	@Override
	public int sort(SortedStream a, SortedStream b) {
		return a.getField().toLowerCase(Locale.ROOT).compareTo(b.getField().toLowerCase(Locale.ROOT)) * (isAscending ? 1 : -1);
	}

}
