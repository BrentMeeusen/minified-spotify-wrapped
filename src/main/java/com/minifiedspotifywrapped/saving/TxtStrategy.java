package com.minifiedspotifywrapped.saving;

import com.minifiedspotifywrapped.Report;
import java.io.FileWriter;
import java.io.IOException;

public class TxtStrategy implements SavingStrategy {

	/**
	 * Generates the output for a .txt file of the report.
	 *
	 * @param report the report to generate the output for
	 * @return the string representation
	 */
	public String generate(Report report) {

		// Get data
		int amount = report.getAmount(), year = report.getYear();
		float[] totalTimeListened = report.getTotalTimeListened();

		// Create a StringBuilder for creating the report txt format
		StringBuilder sb = new StringBuilder();

		// General stuffs
		sb.append("GENERAL\r\n");
		sb.append("In ").append(year).append(", you spent ")
			.append(String.format("%1.2f", totalTimeListened[0]))
			.append("% of your time listening to Spotify.\r\n");
		sb.append("That is ").append(totalTimeListened[1]).append(" seconds, ")
			.append(String.format("%1.2f", totalTimeListened[2])).append(" minutes, ")
			.append(String.format("%1.2f", totalTimeListened[3])).append(" hours, ")
			.append(String.format("%1.2f", totalTimeListened[4])).append(" days.\r\n\r\n");

		// Show tracks
		sb.append("TRACKS\r\n");
		int max = Math.max(1, Math.min(amount, report.getTracks().size()));     // Between 1 and tracks.size()
		for(int i = 0; i < max; i++) {
			sb.append(report.getTracks().get(i)).append("\r\n");
		}

		// Show artists
		sb.append("\r\nARTISTS\r\n");
		max = Math.max(1, Math.min(amount, report.getArtists().size()));     // Between 1 and tracks.size()
		for(int i = 0; i < max; i++) {
			sb.append(report.getArtists().get(i)).append("\r\n");
		}
		return sb.append("\r\n\r\n").append(outro).toString();

	}

	@Override
	public FileWriter getFileWriter(Report report) throws IOException {
		return new FileWriter(report.getDirectory().getAbsolutePath() + "\\MSW.txt");
	}

}
