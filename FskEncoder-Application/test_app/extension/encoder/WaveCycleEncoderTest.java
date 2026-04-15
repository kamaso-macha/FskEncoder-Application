/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : WaveCycleTest.java
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import extension.encoder.WaveCycleEncoder;


/**
 * Responsibilities:<br>
 * Does all unit testing on class WaveEncoder.<br>
 * See tests below for a detailed picture of applied tests.
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

class WaveCycleEncoderTest {

	private static Logger LOGGER = null;
	
	@BeforeAll
	public static void setLogger() throws MalformedURLException {
		
		System.setProperty("log4j.configurationFile","./test-cfg/log4j2.xml");
		LOGGER = LogManager.getLogger();
		
	}

	
	/**
	 * Test method for {@link extension.encoder.WaveCycleEncoder#WaveCycle(int)}.
	 * <p>
	 * Verify that sampling rates less or equal 0 are rejected with an exception.
	 *  
	 */
	@Test
	void testWaveCycle() {
		LOGGER.info("testWaveCycle()");
		
		IllegalArgumentException thrown;
		
		/*
		 * Check for exception as long as the sampling rate is less or equal 0.
		 */
		thrown = assertThrows(IllegalArgumentException.class, () -> { new WaveCycleEncoder(-1); });
		assertTrue(thrown.getMessage().equals("aSamplingRate must be greater than 0"));
		
		thrown = assertThrows(IllegalArgumentException.class, () -> { new WaveCycleEncoder(0); });
		assertTrue(thrown.getMessage().equals("aSamplingRate must be greater than 0"));

		/*
		 * Verify that a sampling rate greater 0 is accepted.
		 */
		assertDoesNotThrow(() -> new WaveCycleEncoder(1) );
		
	} // testWaveCycle()

	
	/**
	 * Test method for {@link extension.encoder.WaveCycleEncoder#encode(int, int)}.
	 * <p>
	 * Check, if sample rate is applicable for requested frequency.
	 */
	@Test
	final void testEncodeError() {		
		LOGGER.info("testEncodeError()");
		
		IllegalArgumentException thrown;
		
		/*
		 * Sampling rate below required minimum value is rejected with an exception
		 */
		WaveCycleEncoder cut = new WaveCycleEncoder(3999);
		
		thrown = assertThrows(IllegalArgumentException.class, () -> { cut.encode(1000, 1); });
		assertTrue(thrown.getMessage().equals("Given sample rate is not suitable for the requested frequency!"));

		
		/*
		 * Acceptable sampling rate
		 */
		WaveCycleEncoder cut2 = new WaveCycleEncoder(4000);
		assertDoesNotThrow(() -> cut2.encode(1000, 1) );
		
	} // testEncodeError()

	
	/**
	 * Test method for {@link extension.encoder.WaveCycleEncoder#encode(int, int)}.
	 * <p>
	 * Check for correct encoding of the sound samples part I:<br>
	 * Correct number of samples.
	 * 
	 */
	@Test
	final void testEncode() {		
		LOGGER.info("testEncode()");
		
		WaveCycleEncoder cut;
		ByteBuffer result;
		
		
		/*
		 * One single wave cycle
		 */
		cut = new WaveCycleEncoder(4000);
		result = cut.encode(1000, 1);
		
		LOGGER.debug("pos: " + result.limit());
		
		for(int n = 0; n < result.limit(); n++) {
			LOGGER.debug("n: " + n + " - " + result.get(n));
		}
		
		assertTrue(result.limit() == 4);
		
		
		/*
		 * Ten wave cycles
		 */
		cut = new WaveCycleEncoder(4000);
		result = cut.encode(1000, 10);
		
		LOGGER.debug("pos: " + result.limit());
		
		for(int n = 0; n < result.limit(); n++) {
			LOGGER.debug("n: " + n + " - " + result.get(n));
		}
		
		assertTrue(result.limit() == 40);
		
		
		/*
		 * Higher resolution
		 */
		
		cut = new WaveCycleEncoder(16000);
		
		/*
		 * Ten wave cycles
		 */
		result = cut.encode(1000, 10);
		
		LOGGER.debug("pos: " + result.limit());
		
		for(int n = 0; n < result.limit(); n++) {
			LOGGER.debug("n: " + n + " - " + result.get(n));
		}
		
		assertTrue(result.limit() == 160);
		
	} // testEncode()
	
	
	/**
	 * Test method for {@link extension.encoder.WaveCycleEncoder#encode(int, int)}.
	 * <p>
	 * Check for correct encoding of the sound samples part II:<br>
	 * Correct encoding of samples.
	 * 
	 */
	@Test
	final void testWaveForm() {		
		LOGGER.info("testWaveForm()");
		
		WaveCycleEncoder cut 		= null;
		ByteBuffer result 	= null;
		byte[] ref 			= null; 
		
		/*
		 * One single wave cycle @ 4 times oversampling
		 */
		cut = new WaveCycleEncoder(4000);
		result = cut.encode(1000, 1);
		ref = new byte[] { 0, 127, 0, -127 };
		
		LOGGER.debug("pos: " + result.limit());
		
		for(int n = 0; n < result.limit(); n++) {
			LOGGER.debug("n: " + n + " - " + result.get(n));
			assertTrue(result.get(n) == ref[n]);
		}
		
		
		/*
		 * One single wave cycle @ 8 times oversampling
		 */
		cut = new WaveCycleEncoder(16000);
		result = cut.encode(2000, 1);
		ref = new byte[] { 0, 89, 127, 89, 0, -89, -127, -89 };
		
		LOGGER.debug("pos: " + result.limit());
		
		for(int n = 0; n < result.limit(); n++) {
			LOGGER.debug("n: " + n + " - " + result.get(n));
			assertTrue(result.get(n) == ref[n]);
		}
		
	} // testWaveForm()
	
	
	/**
	 * Verifies the correct behavior of toString()
	 * 
	 * Test method for {@link #toString()}.
	 */
	@Test
	void testToString() {
		LOGGER.info("testToString()");
		
		WaveCycleEncoder cut = new WaveCycleEncoder(42);
		LOGGER.info("toString(): {}", cut.toString());
		assertEquals(
			"WaveCycle [samplingRate=42]",
			cut.toString())
			;

	} // testToString()
	
	
} // class

