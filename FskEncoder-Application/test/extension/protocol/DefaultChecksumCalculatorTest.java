/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : DefaultChecksumCalculatorTest.java
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

package extension.protocol;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import extension.protocol.ChecksumCalculator;
import extension.protocol.DefaultChecksumCalculator;

/**
 * Responsibilities:<br>
 * Test the  class.
 * 
 * 
 * <p>
 * Collaborators:<br>
 * Class under test.
 * 
 * 
 * <p>
 * Description:<br>
 * Performs all tests required to guarantee the correct function of CUT
 * 
 * <p>
 * @author Stefan
 *
 */

class DefaultChecksumCalculatorTest {

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
	 * Test method for {@link extension.protocol.DefaultChecksumCalculator#sumUp(int)}.
	 * 
	 * Test is run with a mask that gives a MOD 256 behavior.
	 * 
	 */
	@Test
	public void testSumUp_Mod256() {
		LOGGER.info("testSumUp_Mod256()");
		
		ChecksumCalculator cut = new DefaultChecksumCalculator(0x00FF);
		
		try {
			
			// Mask == 0x00FF for byte value
			
			// 0x0123 & 0x00FF = 0x0023
			cut.sumUp(0x0123);
			assertTrue(0x23 == cut.getCheckSum());
			
			// 0x0456 & 0x00FF = 0x0056
			// 0x23 + 0x56 = 0x79
			cut.sumUp(0x0456);
			assertTrue(0x79 == cut.getCheckSum());
			
		} catch (IllegalAccessException e) {
			fail("Unexpected Exception caught!");
		}
		
	} // testSumUp_Mod256()
	

	/**
	 * Test method for {@link extension.protocol.DefaultChecksumCalculator#sumUp(int)}.
	 * 
	 * Test is run with a mask that gives a MOD 4096 behavior.
	 * 
	 */
	@Test
	public void testSumUp_Mod4096() {
		LOGGER.info("testSumUp_Mod4096()");
		
		ChecksumCalculator cut = new DefaultChecksumCalculator(0x0FFF);
		
		try {
			
			// Mask == 0x00FF for byte value
			
			// 0x0123 & 0x0FFF = 0x0123
			cut.sumUp(0x0123);
			assertTrue(0x123 == cut.getCheckSum());
			
			// 0x1456 & 0x0FFF = 0x0456
			// 0x0123 + 0x0456 = 0x0579
			cut.sumUp(0x0456);
			assertTrue(0x0579 == cut.getCheckSum());
			
			// 0x0CDE & 0x0FFF = 0x0CDE
			// 0x0579 + 0x0CDE = 0x0257
			cut.sumUp(0x0CDE);
			assertTrue(0x0257 == cut.getCheckSum());
			
		} catch (IllegalAccessException e) {
			fail("Unexpected Exception caught!");
		}
		
	} // testSumUp_Mod4096()
	

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


} // sslac
