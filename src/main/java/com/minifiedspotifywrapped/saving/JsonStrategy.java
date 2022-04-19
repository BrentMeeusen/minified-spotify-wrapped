package com.minifiedspotifywrapped.saving;

import com.minifiedspotifywrapped.Report;
import com.minifiedspotifywrapped.SortedStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JsonStrategy implements SavingStrategy {

	@Override
	public String generate(Report report) {

		// Get data
		int amount = report.getAmount(), year = report.getYear();
		float[] totalTimeListened = report.getTotalTimeListened();

		// Create a StringBuilder for creating the report txt format
		StringBuilder sb = new StringBuilder();

		// General stuffs
		sb.append("{\r\n");
		sb.append("    \"general\": {\r\n");
		sb.append("        \"year\": ").append(year).append(",\r\n");
		sb.append("        \"amount\": ").append(amount).append(",\r\n");
		sb.append("        \"time_listened\": {\r\n");
		sb.append("            \"percentage\": ").append(String.format("%1.2f", totalTimeListened[0])).append(",\r\n");
		sb.append("            \"seconds\": ").append(totalTimeListened[1]).append(",\r\n");
		sb.append("            \"minutes\": ").append(String.format("%1.2f", totalTimeListened[2])).append(",\r\n");
		sb.append("            \"hours\": ").append(String.format("%1.2f", totalTimeListened[3])).append(",\r\n");
		sb.append("            \"days\": ").append(String.format("%1.2f", totalTimeListened[4])).append("\r\n");
		sb.append("        }\r\n");
		sb.append("    },\r\n\r\n");

		// Show tracks
		sb.append("    \"tracks\": [\r\n");
		sb.append(generate(amount, report.getTracks(), "track"));
		sb.append("    ],\r\n");

		// Show artists
		sb.append("    \"artists\": [\r\n");
		sb.append(generate(amount, report.getArtists(), "artist"));
		sb.append("    ]\r\n");
		sb.append("}");

		return sb.toString();
	}

	/**
	 * Generates the JSON for the tracks and artists.
	 *
	 * @param amount the number of artists to show
	 * @param data the data to save
	 * @param fieldName the name of the field
	 * @return the JSON string representation
	 */
	private String generate(int amount, ArrayList<SortedStream> data, String fieldName) {

		StringBuilder sb = new StringBuilder();
		int max = Math.max(1, Math.min(amount, data.size()));     // Between 1 and tracks.size()

		for(int i = 0; i < max; i++) {
			SortedStream track = data.get(i);
			sb.append("        {\r\n");
			sb.append("            \"").append(fieldName).append("\": \"").append(track.getField()).append("\",\r\n");
			sb.append("            \"num_streams\": ").append(track.getNumStreams()).append(",\r\n");
			sb.append("            \"percentage\": ").append(track.getPercentage()).append(",\r\n");
			sb.append("            \"seconds\": ").append(track.getSeconds()).append(",\r\n");
			sb.append("            \"minutes\": ").append(String.format("%1.2f", track.getMinutes())).append(",\r\n");
			sb.append("            \"hours\": ").append(String.format("%1.2f", track.getHours())).append(",\r\n");
			sb.append("            \"days\": ").append(String.format("%1.2f", track.getDays())).append("\r\n");
			sb.append("        },\r\n");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));

		return sb.toString();
	}

	@Override
	public FileWriter getFileWriter(Report report) throws IOException {
		return new FileWriter(report.getDirectory().getAbsolutePath() + "\\MSW.json");
	}

}
