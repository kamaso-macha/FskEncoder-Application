/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : MemoryRegionTest.java
 *
 * More information about this project can be found on Github
 * http://github.com/kamaso-macha/FskEncoder-Application
 *
 * **********************************************************************
 *
 * Copyright (C)2025 by Kama So Macha (http://github.com/kamaso-macha)
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

import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import extension.model.MemoryRegion;

/**
 * Responsibilities:<br>
 * Verification of the correct behavior of class MemoryRegion.
 * 
 * <p>
 * Collaborators:<br>
 * Class under test.
 * 
 * <p>
 * Description:<br>
 * Unit test for candidate class.
 * 
 * <p>
 * @author Stefan
 *
 */

class MemoryRegionTest {

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
	 * Test method for {@link extension.model.MemoryRegion#MemoryRegion()}.
	 */
	@Test
	final void testMemoryRegion() {
		LOGGER.info("testMemoryRegion()");
		
		/*
		 * Nothing to test here
		 */
		
		assertTrue(true);
		
	} // testMemoryRegion()

	
	/**
	 * Verifies that illegal parameters are detected and handled with the correct exception.
	 * 
	 * Test method for {@link extension.model.MemoryRegion#addContent(byte[])}.
	 */
	@Test
	final void testAddContent() {
		LOGGER.info("testAddContent()");
		
		IllegalArgumentException thrown;
		final MemoryRegion cut = new MemoryRegion(0L);
		
		thrown = assertThrows(IllegalArgumentException.class, () -> cut.addContent(null));
		assertTrue(thrown.getMessage().equals("aContent can't be null"));
		
		thrown = assertThrows(IllegalArgumentException.class, () -> cut.addContent(new byte[0]));
		assertTrue(thrown.getMessage().equals("aContent can't be empty"));
		
	} // testAddContent()


	/**
	 * Verifies the correct handling of the start address 
	 * 
	 * Test method for {@link extension.model.MemoryRegion#changeStartAddress()}.
	 */
	@Test
	final void testChangeStartAddress() {
		LOGGER.info("testChangeStartAddress()");
		
		final long START_ADDRESS = 10;
		final long CHANGED_START_ADDRESS = 42;

		final MemoryRegion cut = new MemoryRegion(START_ADDRESS);
		assertTrue(cut.getStartAddress() == START_ADDRESS);

		cut.changeStartAddress(CHANGED_START_ADDRESS);
		assertEquals(CHANGED_START_ADDRESS, cut.getStartAddress());
		
	} // testChangeStartAddress()


	/**
	 * Verifies the correct behavior of the method getContent().
	 * 
	 * Test method for {@link extension.model.MemoryRegion#getContent()}.
	 */
	@Test
	final void testGetContent() {
		LOGGER.info("testGetContent()");
		
		IllegalAccessError thrown;
		final MemoryRegion cut = new MemoryRegion(0L);
		final byte[] content = new byte[16];
		
		thrown = assertThrows(IllegalAccessError.class, () -> cut.getContent());
		assertTrue(thrown.getMessage().equals("No content available"));
		
		cut.addContent(content);
		
		assertDoesNotThrow(() -> cut.getContent());
		
		ByteBuffer result = cut.getContent();
		
		assertTrue(result != null);
		assertTrue(result.capacity() == content.length);
		
	} // testGetContent()


	/**
	 * Asserts that illegal calls to this method are detected and handled with the correct exception.
	 * 
	 * Test method for {@link extension.model.MemoryRegion#getEndAddress()}.
	 */
	@Test
	final void testGetEndAddress() {
		LOGGER.info("testGetEndAddress()");
		
		IllegalAccessError thrown;
		
		final long START_ADDRESS = 10;

		final MemoryRegion cut = new MemoryRegion(START_ADDRESS);
		final byte[] content = new byte[16];
		
		thrown = assertThrows(IllegalAccessError.class, () -> cut.getEndAddress());
		assertTrue(thrown.getMessage().equals("No content available"));
		
		cut.addContent(content);
		
		assertDoesNotThrow(() -> cut.getEndAddress());
		
		long result = cut.getEndAddress();
		
		assertTrue(result == START_ADDRESS + content.length - 1);
		
	} // testGetEndAddress()


	/**
	 * Verifies the correct calculation of the size attribute.
	 * 
	 * Test method for {@link extension.model.MemoryRegion#getSize()}.
	 */
	@Test
	final void testGetSize() {
		LOGGER.info("testGetSize()");
		
		final long START_ADDRESS = 10;

		final MemoryRegion cut = new MemoryRegion(START_ADDRESS);
		final byte[] content = new byte[16];

		assertTrue(cut.getSize() == 0);
		
		cut.addContent(content);
		assertTrue(cut.getSize() == content.length);
		
		cut.addContent(content);
		assertTrue(cut.getSize() == 2 * content.length);
		
	} // testGetSize()


	/**
	 * Verifies the correct handling of the start address 
	 * 
	 * Test method for {@link extension.model.MemoryRegion#getStartAddress()}.
	 */
	@Test
	final void testGetStartAddress() {
		LOGGER.info("testGetStartAddress()");
		
		final long START_ADDRESS = 10;

		final MemoryRegion cut = new MemoryRegion(START_ADDRESS);
		
		assertTrue(cut.getStartAddress() == START_ADDRESS);
		
	} // testGetStartAddress()


	/**
	 * Verifies the correct behavior of toString()
	 * 
	 * Test method for {@link #toString()}.
	 */
	@Test
	void testToString() {
		LOGGER.info("testToString()");
		
		final MemoryRegion cut = new MemoryRegion(0x1234);
		cut.addContent(new byte[16]);
		
		LOGGER.info("toString(): {}", cut.toString());
		assertEquals(
			"MemoryRegion [startAddress=0x1234, size=0x10 / 16]",
			cut.toString())
			;

	} // testToString()
	
	
} // ssalc
