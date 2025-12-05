/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : BitOrderTest.java
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

package extension.encoder;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import extension.encoder.BitOrder;

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

class BitOrderTest {

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
	 * Assert that only LSB_MSB and MSB_LSB are members of the enum
	 * AND that the ordinal value of both members are correct.
	 * 
	 */
	@Test
	final void testOrdinalValues() {
		LOGGER.info("testOrdinalValues()");
		
		assertTrue(BitOrder.values().length == 2);
		assertTrue(BitOrder.LSB_MSB.ordinal() == 0);
		assertTrue(BitOrder.MSB_LSB.ordinal() == 1);
		
	} // testOrdinalValues()

} // class
