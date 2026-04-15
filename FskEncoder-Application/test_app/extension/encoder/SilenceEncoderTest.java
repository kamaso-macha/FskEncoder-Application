/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : SilenceEncoderTest.java
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

import java.net.MalformedURLException;
import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import extension.encoder.SilenceEncoder;

/**
 * Responsibilities:<br>
 * Tests the functionality of class SilenceEncoder.
 * 
 * <p>
 * Collaborators:<br>
 * Class under test.
 * 
 * <p>
 * Description:<br>
 * Verifies the correct error handling and encoding.
 * 
 * <p>
 * @author Stefan
 *
 */

class SilenceEncoderTest {

	private static Logger LOGGER = null;
	
	@BeforeAll
	public static void setLogger() throws MalformedURLException {
		
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
	 * Test method for {@link extension.encoder.SilenceEncoder#SilenceEncoder(int)}.
	 * <br>
	 * Verifies the correct error handling.
	 */
	@Test
	final void testSilenceEncoderErrorHandling() {
		LOGGER.info("testSilenceEncoderErrorHandling()");
		
		IllegalArgumentException thrown;
		
		/*
		 * Check for exception as long as the sampling rate is less or equal 0.
		 */
		thrown = assertThrows(IllegalArgumentException.class, () -> { new SilenceEncoder(-1); });
		assertTrue(thrown.getMessage().equals("aSamplingRate must be greater than 0!"));
		
		thrown = assertThrows(IllegalArgumentException.class, () -> { new SilenceEncoder(0); });
		assertTrue(thrown.getMessage().equals("aSamplingRate must be greater than 0!"));

		thrown = assertThrows(IllegalArgumentException.class, () -> { new SilenceEncoder(999); });
		assertTrue(thrown.getMessage().equals("aSamplingRate / 1000 must be greater or equal 1!"));

		/*
		 * Verify that a sampling rate greater 0 is accepted.
		 */
		assertDoesNotThrow(() -> new SilenceEncoder(1000) );
		
	} // testSilenceEncoderErrorHandling()

	
	/**
	 * Test method for {@link extension.encoder.SilenceEncoder#encode(int)}.
	 * <br>
	 * Ensures that the samples are assembled correctly.
	 */
	@Test
	final void testEncode() {
		LOGGER.info("testEncode()");
		
		IllegalArgumentException thrown;
		SilenceEncoder cut = new SilenceEncoder(1000);
		ByteBuffer result = null;
		
		/*
		 * Check for exception on duration == 0.
		 */
		thrown = assertThrows(IllegalArgumentException.class, () -> cut.encode(0) );
		assertTrue(thrown.getMessage().equals("aDuration must be greater than 0!"));
		
		assertDoesNotThrow(() -> cut.encode(1) );
		
		
		/*
		 * Check the correct encoding / buffer size. 
		 */
		result = cut.encode(1);
		assertTrue(result.position() == 0);
		assertTrue(result.limit() == 1);
		assertTrue(result.get(0) == 0);
		
		result = cut.encode(1000);
		assertTrue(result.position() == 0);
		assertTrue(result.limit() == 1000);
		
	} // testEncode()

	
	/**
	 * Verifies the correct behavior of toString()
	 * 
	 * Test method for {@link #toString()}.
	 */
	@Test
	void testToString() {
		LOGGER.info("testToString()");
		
		SilenceEncoder cut = new SilenceEncoder(1000);
		LOGGER.info("toString(): {}", cut.toString());
		assertEquals(
			"SilenceEncoder [samplingRate=1000]",
			cut.toString())
			;

	} // testToString()
	
	
} // class
