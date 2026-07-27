/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : Modulo65536ChecksumCalculatorTest.java
 *
 * More information about this project can be found on Github
 * http://github.com/kamaso-macha/FskEncoder
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


package extension.protocol;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Responsibilities:<br>
 * 
 * 
 * <p>
 * Collaborators:<br>
 * 
 * 
 * <p>
 * Description:<br>
 * 
 * 
 * <p>
 * @author Stefan
 *
 */

// DOC
// Created at 2026-07-27 11:13:09

class Modulo65536ChecksumCalculatorTest {

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
	 * Test method for {@link extension.protocol.Modulo65536ChecksumCalculator#sumUp(int)}.
	 */
	@Test
	public void testSumUp_CorrectMasking() {
		LOGGER.info("testSumUp_CorrectMasking()");
		
		Modulo65536ChecksumCalculator cut = new Modulo65536ChecksumCalculator();
		
		try {
			
			// Mask == 0x00_FFFF for word value
			
			// 0x00_1234 & 0x00_FFFF = 0x00_1234
			cut.sumUp(0x01234);
			assertTrue(0x01234 == cut.getCheckSum());
			
			
			// 0x00_9876 & 0x00_FFFF = 0x00_9876
			// 0x00_9876 + 0x00_9876 = 0x01_30EC
			// 0x01_30EC & 0x00_FFFF = 0x00_30EC
			cut.clear();

			cut.sumUp(0x009876);
			cut.sumUp(0x009876);
			
			assertTrue(0x0030EC == cut.getCheckSum());
			
		} catch (IllegalAccessException e) {
			fail("Unexpected Exception caught!");
		}
		
	} // testSumUp()
	

	/**
	 * Test method for {@link fskencoder.ChecksumCalculator#getCheckSum()}.
	 */
	@Test
	public void testGetCheckSum() {
		LOGGER.info("testGetCheckSum()");
		
		
		/*
		 * This method is tested implicitly on the sumUp() methods.
		 */
		
		assertTrue(true);
		
	} // testGetCheckSum()

	
} // ssalc
