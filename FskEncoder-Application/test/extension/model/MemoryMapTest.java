/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : MemoryMapTest.java
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

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import extension.model.MemoryBlockDescription;
import extension.model.MemoryMap;

/**
 * Responsibilities:<br>
 * Verify the correct behavior of class MemoryMap
 * 
 * <p>
 * Collaborators:<br>
 * Class under Test.
 * 
 * <p>
 * Description:<br>
 * Verifies all behavior of the class under test.
 * 
 * <p>
 * @author Stefan
 *
 */

class MemoryMapTest {

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
	 * Test method for {@link extension.model.MemoryMap#MemoryMap()}.
	 */
	@Test
	final void testMemoryMap() {
		LOGGER.info("testMemoryMap()");
	
		/*
		 * 	Currently nothing to test
		 */
		
		assertTrue(true);

	} // testMemoryMap()


	/**
	 * Verifies that parameter errors are detected and handled the right way.
	 * 
	 * Test method for {@link extension.model.MemoryMap#addRegion(extension.model.MemoryRegion)}.
	 */
	@Test
	final void testAddRegion() {
		LOGGER.info("testAddRegion()");
		
		IllegalArgumentException thrown;
		
		final MemoryMap cut = new MemoryMap();
		
		thrown =assertThrows(IllegalArgumentException.class, () -> cut.addRegion(null));
		assertTrue(thrown.getMessage().equals("aRegion can't be null"));
		
		assertDoesNotThrow(() -> cut.addRegion(new MemoryRegion(0)));
		
	} // testAddRegion()


	/**
	 * Asserts that a valid memory layout is build and provided.
	 * 
	 * Test method for {@link extension.model.MemoryMap#getMemoryLayout()}.
	 */
	@Test
	final void testGetMemoryLayout() {
		LOGGER.info("testGetMemoryLayout()");
		
		MemoryMap cut = new MemoryMap();
		
		MemoryRegion region0 = new MemoryRegion(0);
		region0.addContent(new byte[50]);
		
		MemoryRegion region1 = new MemoryRegion(100);
		region1.addContent(new byte[50]);
		
		MemoryRegion region2 = new MemoryRegion(200);
		region2.addContent(new byte[50]);

		cut.addRegion(region0);
		cut.addRegion(region1);
		cut.addRegion(region2);
		
		List<MemoryBlockDescription> result = cut.getMemoryLayout();
		assertTrue(result != null);
		assertTrue(result.size() == 3);
		
		assertTrue(result.get(0).START_ADDRESS == region0.getStartAddress());
		assertTrue(result.get(1).START_ADDRESS == region1.getStartAddress());
		assertTrue(result.get(2).START_ADDRESS == region2.getStartAddress());
		
		assertTrue(result.get(0).END_ADDRESS == region0.getEndAddress());
		assertTrue(result.get(1).END_ADDRESS == region1.getEndAddress());
		assertTrue(result.get(2).END_ADDRESS == region2.getEndAddress());

		assertTrue(result.get(0).SIZE == region0.getSize());
		assertTrue(result.get(1).SIZE == region1.getSize());
		assertTrue(result.get(2).SIZE == region2.getSize());

	} // testGetMemoryLayout()


	/**
	 * Makes sure that on request the right memory region is returned.
	 * 
	 * Test method for {@link extension.model.MemoryMap#getMemoryRegion(long)}.
	 */
	@Test
	final void testGetMemoryRegion() {
		LOGGER.info("testGetMemoryRegion()");
		
		MemoryMap cut = new MemoryMap();
		
		MemoryRegion region0 = new MemoryRegion(0);
		region0.addContent(new byte[50]);
		
		MemoryRegion region1 = new MemoryRegion(100);
		region1.addContent(new byte[50]);
		
		MemoryRegion region2 = new MemoryRegion(200);
		region2.addContent(new byte[50]);

		cut.addRegion(region0);
		cut.addRegion(region1);
		cut.addRegion(region2);
		
		List<MemoryBlockDescription> result = cut.getMemoryLayout();
		MemoryRegion resultRegion;
		long startAddress;
		
		startAddress = result.get(0).START_ADDRESS;
		resultRegion = cut.getMemoryRegion(startAddress);
		
		assertTrue(resultRegion != null);
		assertTrue(resultRegion == region0);
		
		startAddress = result.get(1).START_ADDRESS;
		resultRegion = cut.getMemoryRegion(startAddress);
		
		assertTrue(resultRegion != null);
		assertTrue(resultRegion == region1);
		
		startAddress = result.get(2).START_ADDRESS;
		resultRegion = cut.getMemoryRegion(startAddress);
		
		assertTrue(resultRegion != null);
		assertTrue(resultRegion == region2);
		
	} // testGetMemoryRegion()


	/**
	 * Verifies the correct number of memory regions.
	 * 
	 * Test method for {@link extension.model.MemoryMap#getRegionCount()}.
	 */
	@Test
	final void testGetRegionCount() {
		LOGGER.info("testGetRegionCount()");
		
		MemoryMap cut = new MemoryMap();
		
		MemoryRegion region0 = new MemoryRegion(0);
		region0.addContent(new byte[50]);
		
		MemoryRegion region1 = new MemoryRegion(100);
		region1.addContent(new byte[50]);
		
		MemoryRegion region2 = new MemoryRegion(200);
		region2.addContent(new byte[50]);

		cut.addRegion(region0);
		cut.addRegion(region1);
		
		List<MemoryBlockDescription> result = cut.getMemoryLayout();
		assertTrue(result.size() == 2);
		
		assertTrue(result.size() == cut.getRegionCount());
		
		cut.addRegion(region2);
		
		result = cut.getMemoryLayout();
		assertTrue(result.size() == 3);
		
		assertTrue(result.size() == cut.getRegionCount());
		
	} // testGetRegionCount()
	
	
	/**
	 * Verifies the correct behavior of clear()
	 * 
	 * Test method for {@link #clear()}
	 */
	@Test
	void testClear() {
		LOGGER.info("testClear()");
		
		MemoryMap cut = new MemoryMap();
		
		MemoryRegion region0 = new MemoryRegion(0);
		region0.addContent(new byte[50]);
		
		MemoryRegion region1 = new MemoryRegion(100);
		region1.addContent(new byte[50]);
		
		MemoryRegion region2 = new MemoryRegion(200);
		region2.addContent(new byte[50]);

		cut.addRegion(region0);
		cut.addRegion(region1);
		cut.addRegion(region2);
		
		assertTrue(3 == cut.getRegionCount());
		
		cut.clear();
		
		assertTrue(0 == cut.getRegionCount());
		
	} // testClear()


	/**
	 * Verifies the correct behavior of toString()
	 * 
	 * Test method for {@link #toString()}.
	 */
	@Test
	void testToString() {
		LOGGER.info("testToString()");
		
		MemoryRegion region = new MemoryRegion(200);
		region.addContent(new byte[50]);

		MemoryMap cut = new MemoryMap();
		cut.addRegion(region);
		
		LOGGER.info("toString(): {}", cut.toString());
		assertTrue(cut.toString().startsWith("MemoryMap [memoryLayout={200=MemoryRegion"));
		assertTrue(cut.toString().endsWith("}]"));
	
	} // testToString()
	
	
} // ssalc
