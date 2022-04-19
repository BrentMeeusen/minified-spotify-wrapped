package com.minifiedspotifywrapped.sorting;

import com.minifiedspotifywrapped.SortedStream;

public interface SortingStrategy {

	/**
	 * Sorts two SortedStream instances based on the given strategy.
	 *
	 * @param a the first SortedStream
	 * @param b the second SortedStream
	 * @return -1 if a should come first, 0 if the order should be preserved, 1 if b should come first
	 */
	int sort(SortedStream a, SortedStream b);

}
