package com.minifiedspotifywrapped.saving;

import com.minifiedspotifywrapped.Report;
import java.io.FileWriter;
import java.io.IOException;

public interface SavingStrategy {

	/**
	 * Generates a report.
	 *
	 * @param report the report to generate a report for
	 * @return the report in the correct format, as a String representation
	 */
	String generate(Report report);

	/**
	 * Saves a report in the given format.
	 *
	 * @param report the report to save
	 */
	default void save(Report report) {

		try {
			FileWriter fileWriter = new FileWriter(report.getDirectory().getAbsolutePath() + "\\MSW.txt");
			fileWriter.append(generate(report));
			fileWriter.close();
		} catch(IOException e) {
			System.err.println(e.getMessage());
		}

	}

}
