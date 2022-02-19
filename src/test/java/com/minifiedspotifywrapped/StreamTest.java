package com.minifiedspotifywrapped;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import org.junit.jupiter.api.Test;

public class StreamTest {

	@Test
	public void testGetPath() {
		assertNull(Stream.getPath());
	}

	@Test
	public void testSetPath() {
		Stream.setPath("abc");
		assertEquals("abc", Stream.getPath());
	}

	@Test
	public void testGetAmount() {
		assertEquals(10, Stream.getAmount());
	}

	@Test
	public void testSetAmount() {
		Stream.setAmount(15);
		assertEquals(15, Stream.getAmount());
	}

	@Test
	public void testGetYear() {
		assertEquals(Calendar.getInstance().get(Calendar.YEAR), Stream.getYear());
	}

	@Test
	public void testSetYear() {
		Stream.setYear(2025);
		assertEquals(2025, Stream.getYear());
	}

}
