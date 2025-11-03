/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : ReaderTest.java
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


package extension.source;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import extension.model.MemoryMap;
import extension.source.ReaderBase;

/**
 * Responsibilities:<br>
 * Verification of the correct behavior of the class Reader
 * 
 * <p>
 * Collaborators:<br>
 * Class under test.
 * 
 * <p>
 * Description:<br>
 * Unit test for the class under test.
 * 
 * <p>
 * @author Stefan
 *
 */

class ReaderTest {

	private static Logger LOGGER = null;

	MemoryMap mmMock = mock(MemoryMap.class);
	FileNameExtensionFilter filterMock = mock(FileNameExtensionFilter.class);
	
	/*
	 * Test implementation of the abstract class Reader.
	 */
	class TestReader extends ReaderBase {

		public TestReader(MemoryMap aMemoryMap) throws IllegalArgumentException {
			super(aMemoryMap);
			
			operationStatus = "relaxed";
			filter = filterMock;
		}

		@Override public boolean loadFile() throws IllegalAccessError { return false; }
		
	} // ssalc
	

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
	 * Test method for {@link extension.source.ReaderBase#Reader(extension.model.MemoryMap)}.
	 */
	@Test
	final void testReader() {
		LOGGER.info("testReader()");
		
		IllegalArgumentException thrown;
		
		thrown = assertThrows(IllegalArgumentException.class, () -> new TestReader(null));
		assertEquals("aMemoryMap can't be null", thrown.getMessage());
		
	} // testReader()


	/**
	 * Test method for {@link extension.source.ReaderBase#getFileNameExtensionFilter()}.
	 */
	@Test
	final void testGetFileNameExtensionFilter() {
		LOGGER.info("testGetFileNameExtensionFilter()");
		
		TestReader cut = new TestReader(mmMock);
		assertEquals(filterMock, cut.getFileNameExtensionFilter());
		
	} // testGetFileNameExtensionFilter()


	/**
	 * Test method for {@link extension.source.ReaderBase#getOperationStatus()}.
	 */
	@Test
	final void testGetOperationStatus() {
		LOGGER.info("testGetOperationStatus()");
		
		TestReader cut = new TestReader(mmMock);
		assertEquals("relaxed", cut.getOperationStatus());
		
	} // testGetOperationStatus()


	/**
	 * Test method for {@link extension.source.ReaderBase#setFilename(java.lang.String)}.
	 */
	@Test
	final void testSetFilename() {
		LOGGER.info("testSetFilename()");
		
		/*
		 * Tested by testToString()
		 */
		
		assertTrue(true);
		
	} // testSetFilename()


	/**
	 * Verifies the correct behavior of toString()
	 * 
	 * Test method for {@link #toString()}.
	 */
	@Test
	void testToString() {
		LOGGER.info("testToString()");
		
		TestReader cut = new TestReader(mmMock);
		LOGGER.info("toString(): {}", cut.toString());

		assertTrue(cut.toString().startsWith("Reader [memoryMap=Mock for MemoryMap, hashCode: "));
		assertTrue(cut.toString().endsWith(", sourceFileName=null, operationStatus=relaxed]"));
		
		cut.setFilename("foo.bar");
		assertTrue(cut.toString().endsWith(", sourceFileName=foo.bar, operationStatus=relaxed]"));
	
	} // testToString()
	
	
} // ssalc
