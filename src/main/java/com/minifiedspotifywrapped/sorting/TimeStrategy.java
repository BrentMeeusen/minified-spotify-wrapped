package com.minifiedspotifywrapped.sorting;

import com.minifiedspotifywrapped.SortedStream;

public class TimeStrategy implements SortingStrategy {

	final private boolean isAscending;

	/**
	 * Alphabetical sorting strategy constructor.
	 *
	 * @param isAscending whether it's ascending or descending
	 */
	public TimeStrategy(boolean isAscending) {
		this.isAscending = isAscending;
	}

	@Override
	public int sort(SortedStream a, SortedStream b) {
		return Integer.compare(a.getSeconds(), b.getSeconds()) * (isAscending ? 1 : -1);
	}

}
