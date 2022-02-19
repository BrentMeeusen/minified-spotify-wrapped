package com.minifiedspotifywrapped;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Scanner;
import org.junit.jupiter.api.Test;

public class StreamTest {


	// ============================================================
	// STATIC GETTERS AND SETTERS THROUGH "GATEWAY"
	@Test
	public void testSetPathFirstTry() {

		Scanner scanner = new Scanner("C:\\");
		Stream.setVariable(scanner, "path");
		assertEquals("C:\\", Stream.getPath());

	}

	@Test
	public void testSetPathSecondTry() {

		Scanner scanner = new Scanner("my/beautiful/path\r\nC:/Program Files");
		Stream.setVariable(scanner, "path");
		assertEquals("C:\\Program Files", Stream.getPath());

	}

	@Test
	public void testSetAmount() {

		Scanner scanner = new Scanner("5");
		Stream.setVariable(scanner, "amount");
		assertEquals(5, Stream.getAmount());

	}

	@Test
	public void testSetAmountNonInt() {

		Scanner scanner = new Scanner("some string\r\n 12");
		Stream.setVariable(scanner, "amount");
		assertEquals(12, Stream.getAmount());

	}

	@Test
	public void testSetAmountNegative() {

		Scanner scanner = new Scanner("-5 -7 -15 non-int 9\r\n8");
		Stream.setVariable(scanner, "amount");
		assertEquals(8, Stream.getAmount());

	}

	@Test
	public void testSetFull() {

		Scanner scanner = new Scanner("");
		Stream.setVariable(scanner, "full");
		assertEquals(-1, Stream.getAmount());

	}

	@Test
	public void testSetYear() {

		Scanner scanner = new Scanner("2025");
		Stream.setVariable(scanner, "year");
		assertEquals(2025, Stream.getYear());

	}

	@Test
	public void testSetYearNonInt() {

		Scanner scanner = new Scanner("some string\r\n 12 2021");
		Stream.setVariable(scanner, "year");
		assertEquals(2021, Stream.getYear());

	}

	@Test
	public void testSetYearNegative() {

		Scanner scanner = new Scanner("-5 doesnt exist \r\n 200 5050");
		Stream.setVariable(scanner, "year");
		assertEquals(5050, Stream.getYear());

	}

}
