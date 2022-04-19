package com.minifiedspotifywrapped.sorting;

import com.minifiedspotifywrapped.SortedStream;
import java.util.Locale;

public class NumStreamsStrategy implements SortingStrategy {

	final private boolean isAscending;

	/**
	 * Alphabetical sorting strategy constructor.
	 *
	 * @param isAscending whether it's ascending or descending
	 */
	public NumStreamsStrategy(boolean isAscending) {
		this.isAscending = isAscending;
	}

	@Override
	public int sort(SortedStream a, SortedStream b) {
		return Integer.compare(a.getNumStreams(), b.getNumStreams()) * (isAscending ? 1 : -1);
	}

}
