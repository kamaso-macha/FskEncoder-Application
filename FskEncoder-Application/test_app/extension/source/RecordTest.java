/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : RecordTest.java
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


package extension.source;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import extension.source.Record;

/**
 * Responsibilities:<br>
 * Verification of the correct behavior of the class Record
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

class RecordTest {

	private static Logger LOGGER = null;

	
	/*
	 * Test implementation of the abstract base class Record
	 */
	class TestRecord extends Record {

		boolean hasParsed = false;
		
		public TestRecord(final int aRecordNumber, String aContent) throws Exception {
			super(aRecordNumber);

			parseContent(aContent);
			
		}
		
		public int getRecordNumber() { return recordNumber; }
		public boolean hasParsed() { return hasParsed; }

		@Override
		protected void parseContent(Object aContent) throws Exception {
			LOGGER.info("parseContent(): aContent = {}", aContent);
			
			hasParsed = true;
			
		}

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
	@AfterAll static void tearDownAfterClass() throws Exception { }

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach void setUp() throws Exception { }

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach void tearDown() throws Exception { }
	

	/**
	 * Test method for {@link extension.source.Record#Record(int, java.lang.String)}.
	 */
	@Test
	final void testRecordIntString() {
		LOGGER.info("testRecordIntString()");
		
		try {
			
			final int RECORD_NBR = 42;
			final String CONTENT = "foo";
			
			TestRecord cut = new TestRecord(RECORD_NBR, CONTENT);
			
			LOGGER.info("cut: {}", cut);
			
			assertTrue(cut.getRecordNumber() == RECORD_NBR);
			assertTrue(cut.hasParsed == true);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception caught");
		}
		
	} // testRecordIntString()
	
	
	/**
	 * Verifies the correct behavior of toString()
	 * 
	 * Test method for {@link #toString()}.
	 */
	@Test
	void testToString() {
		LOGGER.info("testToString()");
		
		try {
			
			final int RECORD_NBR = 42;
			final String CONTENT = "foo";
			
			TestRecord cut = new TestRecord(RECORD_NBR, CONTENT);
			LOGGER.info("toString(): {}", cut.toString());
			assertEquals(
				"Record [recordNumber=42, offset=0, dataBuffer=null]",
				cut.toString())
				;
	
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception caught");
		}
	
	} // testToString()
	
	
} // ssalc
