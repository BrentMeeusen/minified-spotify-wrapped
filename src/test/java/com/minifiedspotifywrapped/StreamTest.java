package com.minifiedspotifywrapped;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

public class StreamTest {

	// ============================================================
	// STATIC GETTERS
	@Test
	public void testGetPath() {
		assertNull(Stream.getPath());
	}

	@Test
	public void testGetAmount() {
		assertEquals(10, Stream.getAmount());
	}

	@Test
	public void testGetYear() {
		assertEquals(Calendar.getInstance().get(Calendar.YEAR), Stream.getYear());
	}



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
	public void testSetAmountFirstTry() {

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

		Scanner scanner = new Scanner("-5 -7 -15 non-int 8");
		Stream.setVariable(scanner, "amount");
		assertEquals(8, Stream.getAmount());

	}


}
