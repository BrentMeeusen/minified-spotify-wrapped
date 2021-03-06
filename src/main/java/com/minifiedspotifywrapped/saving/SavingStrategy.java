package com.minifiedspotifywrapped.saving;

import com.minifiedspotifywrapped.Report;
import java.io.FileWriter;
import java.io.IOException;

public interface SavingStrategy {

	String outro = "This report was generated by Minified Spotify Wrapped (https://github.com/BrentMeeusen/minified-spotify-wrapped).";

	/**
	 * Generates a report.
	 *
	 * @param report the report to generate a report for
	 * @return the report in the correct format, as a String representation
	 */
	String generate(Report report);


	/**
	 * Gets a FileWriter to write to the correct file.
	 *
	 * @param report the report to generate a FileWriter for
	 * @return the FileWriter
	 */
	FileWriter getFileWriter(Report report) throws IOException;

	/**
	 * Saves a report in the given format.
	 *
	 * @param report the report to save
	 */
	default void save(Report report) {

		try {
			FileWriter fileWriter = getFileWriter(report);
			fileWriter.append(generate(report));
			fileWriter.close();
		} catch(IOException e) {
			System.err.println(e.getMessage());
		}

	}

}
