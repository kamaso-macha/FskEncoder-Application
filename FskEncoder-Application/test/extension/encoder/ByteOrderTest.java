/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : ByteOrderTest.java
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

package extension.encoder;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import extension.encoder.ByteOrder;

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

class ByteOrderTest {

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
	 * 
	 * Assert that only LITTLE_ENDIAN and BIG_ENDIAN are members of the enum
	 * AND that the ordinal value of both members are correct.
	 * 
	 */
	@Test
	final void testOrdinalValues() {
		LOGGER.info("testOrdinalValues()");
		
		assertTrue(ByteOrder.values().length == 2);
		assertTrue(ByteOrder.LITTLE_ENDIAN.ordinal() == 0);
		assertTrue(ByteOrder.BIG_ENDIAN.ordinal() == 1);
		
	} // testOrdinalValues()


} // class
