package com.minifiedspotifywrapped.saving;

import com.minifiedspotifywrapped.Report;

public interface SavingStrategy {

	/**
	 * Saves a report in the given format.
	 *
	 * @param report the report to save
	 */
	void save(Report report);

}
