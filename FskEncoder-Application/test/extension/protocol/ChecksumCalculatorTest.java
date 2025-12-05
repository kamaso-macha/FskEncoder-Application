/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : ChecksumCalculatorTest.java
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

class ChecksumCalculatorTest {

	private static Logger LOGGER = null;
	
	
	class TestChecksumCalculator extends ChecksumCalculator {

		public TestChecksumCalculator(int aMask) { super(aMask); }

		@Override
		public void sumUp(int aValue) {
			LOGGER.trace("sumUp(aValue) = " + String.format("%04X", chkSum));

			isInitialized = true;
			chkSum += aValue;

			LOGGER.trace("sumUp(x) = " + String.format("%04X", chkSum));

		}
		
	} // sslac
	
	
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
	 * Test method for {@link extension.protocol.ChecksumCalculator#ChecksumCalculator(int)}.
	 */
	@Test
	final void testChecksumCalculator() {
		LOGGER.info("testChecksumCalculator()");

		/*
		 * 
		 * Implicit tested by testgetMask()
		 * 
		 */
		
		assertTrue(true);
		
	} // testChecksumCalculator()
	

	/**
	 * Test method for {@link extension.protocol.ChecksumCalculator#getCheckSum()}.
	 */
	@Test
	final void testGetCheckSumErrorHandling() {
		LOGGER.info("testGetCheckSumErrorHandling()");
		
		ChecksumCalculator cut = new TestChecksumCalculator(0);
		
		IllegalAccessException thrown;
		
		thrown = assertThrows(IllegalAccessException.class, () -> cut.getCheckSum() );
		assertTrue(thrown.getMessage().equals("Can't return a checksum, nothing was calculated so far!"));
	
		cut.sumUp(0);
		
		assertDoesNotThrow(() -> cut.getCheckSum() );
		
	} // testGetCheckSumErrorHandling()
	

	/**
	 * Test method for {@link extension.protocol.ChecksumCalculator#getValueMask()}.
	 */
	@Test
	final void testGetValueMask() {
		LOGGER.info("testGetValueMask()");

		ChecksumCalculator cut;

		cut = new TestChecksumCalculator(0);	
		assertTrue(cut.getValueMask() == -1);

		cut = new TestChecksumCalculator(0x1234);
		assertTrue(cut.getValueMask() == 0x1234);
		
	} // testGetValueMask()
	

	/**
	 * Test method for {@link extension.protocol.ChecksumCalculator#clear()}.
	 */
	@Test
	final void testClear() {
		LOGGER.info("testClear()");

		ChecksumCalculator cut = new TestChecksumCalculator(0);		
		
		try {
			
			cut.sumUp(1);
			LOGGER.trace("checksum = " + String.format("%04X", cut.getCheckSum()));
			cut.sumUp(2);
			LOGGER.trace("checksum = " + String.format("%04X", cut.getCheckSum()));

			assertTrue(cut.getCheckSum() == 3);
			
			cut.clear();
			
			assertTrue(cut.getCheckSum() == 0);
			
		} catch (IllegalAccessException e) {
			fail("Unexpected exception caught: " + e);
			e.printStackTrace();
		}
		
		
	} // testClear()
	

	/**
	 * Test method for {@link extension.protocol.ChecksumCalculator#toString()}.
	 */
	@Test
	final void testToString() {
		LOGGER.info("testToString()");

		ChecksumCalculator cut = new TestChecksumCalculator(0);
			
		assertTrue(cut.toString() != null);
		
	} // testToString()

} // ssalc
