/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : MemoryBlockDescriptionTest.java
 *
 * PURPOSE       : what is it for?
 *
 * This file is part of the FSK-Encoder project. More information about
 * this project can be found here:  http://...
 * **********************************************************************
 *
 * Copyright (C) [2024] by Stefan Dickel, id4mqtt at gmx.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 *
 */


package extension.model;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import extension.model.MemoryBlockDescription;

/**
 * Responsibilities:<br>
 * functional testing of class MemoryBlockDescription
 * 
 * <p>
 * Collaborators:<br>
 * Class under test.
 * 
 * <p>
 * Description:<br>
 * Verifies the correct behavior of the class under test.
 * 
 * <p>
 * @author Stefan
 *
 */

class MemoryBlockDescriptionTest {

	private static Logger LOGGER = null;

	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		System.setProperty("log4j.configurationFile","./test-cfg/log4j2.xml");
		LOGGER = LogManager.getLogger();
	}


	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}
	

	/**
	 * Verifies that illegal constructor parameters are detected and the right exception
	 * is thrown.<br>
	 * Verifies that the given constructor parameters are assigned correctly to the attributes.
	 * 
	 * Test method for {@link extension.model.MemoryBlockDescription#MemoryBlockDescription(long, int)}.
	 */
	@Test
	final void testMemoryBlockDescription() {
		LOGGER.info("testMemoryBlockDescription()");
		
		IllegalArgumentException thrown;

		
		// size
		thrown = assertThrows(IllegalArgumentException.class, () -> new MemoryBlockDescription(0, 0, 0));
		assertTrue(thrown.getMessage().equals("aSize can't be 0"));
		
		
		// aStartAddress  > aEndAddress
		assertDoesNotThrow(() -> new MemoryBlockDescription(10, 10, 1));
		
		thrown = assertThrows(IllegalArgumentException.class, () -> new MemoryBlockDescription(11, 10, 1));
		assertTrue(thrown.getMessage().equals("Illegal value for aStartAddress or aEndAddress: aStartAddress  > aEndAddress"));
		
		
		// aStartAddress + aSize - 1 != aEndAddress
		thrown = assertThrows(IllegalArgumentException.class, () -> new MemoryBlockDescription(0, 9, 9));
		assertTrue(thrown.getMessage().equals("Illegal value for aEndAddress or aSize: aStartAddress + aSize - 1 != aEndAddress"));
		
		thrown = assertThrows(IllegalArgumentException.class, () -> new MemoryBlockDescription(0, 10, 10));
		assertTrue(thrown.getMessage().equals("Illegal value for aEndAddress or aSize: aStartAddress + aSize - 1 != aEndAddress"));
		
		assertDoesNotThrow(() -> new MemoryBlockDescription(0, 9, 10));

		thrown = assertThrows(IllegalArgumentException.class, () -> new MemoryBlockDescription(0, 9, 11));
		assertTrue(thrown.getMessage().equals("Illegal value for aEndAddress or aSize: aStartAddress + aSize - 1 != aEndAddress"));
		
		
		final long START_ADR = 0x02000;
		final int  SIZE      = 0x0100;
		final long END_ADR   = START_ADR + SIZE - 1;
		
		MemoryBlockDescription cut = new MemoryBlockDescription(START_ADR, END_ADR, SIZE);
		assertTrue(cut.START_ADDRESS == START_ADR);
		assertTrue(cut.SIZE         == SIZE);
		assertTrue(cut.END_ADDRESS   == END_ADR);
		
	} // testMemoryBlockDescription()

	
	/**
	 * Verifies that the compareTo method evaluates the correct result.
	 * 
	 * Test method for {@link extension.model.MemoryBlockDescription#compareTo(extension.model.MemoryBlockDescription)}.
	 */
	@Test
	final void testCompareTo() {
		LOGGER.info("testCompareTo()");
		
		MemoryBlockDescription low  = new MemoryBlockDescription(0x1000, 0x10FF, 0x100);
		MemoryBlockDescription mid1 = new MemoryBlockDescription(0x2000, 0x20FF, 0x100);
		MemoryBlockDescription mid2 = new MemoryBlockDescription(0x2000, 0x20FF, 0x100);
		MemoryBlockDescription high = new MemoryBlockDescription(0x3000, 0x30FF, 0x100);
		
		assertTrue(low.compareTo(mid1)  <  0);
		assertTrue(mid1.compareTo(mid2) == 0);
		assertTrue(mid2.compareTo(high) <  0);

		assertTrue(high.compareTo(mid2) >   0);
		assertTrue(mid2.compareTo(mid1) ==  0);
		assertTrue(mid1.compareTo(low)  >   0);

	} // testCompareTo()

	
	/**
	 * Verifies the correct behavior of toString()
	 * 
	 * Test method for {@link #toString()}.
	 */
	@Test
	void testToString() {
		LOGGER.info("testToString()");
		
		MemoryBlockDescription cut = new MemoryBlockDescription(0, 1, 2);
		LOGGER.info("toString(): {}", cut.toString());
		assertEquals(
			"MemoryBlockDescription [START_ADDRESS=0x0000, END_ADDRESS=0x0001, SIZE=0x02 / 2]",
			cut.toString())
			;

	} // testToString()
	
	
} // ssalc
