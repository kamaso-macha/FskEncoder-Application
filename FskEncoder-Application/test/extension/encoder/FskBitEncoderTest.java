/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : FskBitEncoderTest.java
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import extension.encoder.BitEncoder;
import extension.encoder.FskBitEncoder;
import extension.encoder.WaveCycleEncoder;

/**
 * Responsibilities:<br>
 * Verifies the functionality of class FskBitEncoder.
 * 
 * 
 * <p>
 * Collaborators:<br>
 * Class under test.
 * 
 * <p>
 * Description:<br>
 * Asserts the correct error handling and encoding of 0- and 1-bits.
 * 
 * <p>
 * @author Stefan
 *
 */

class FskBitEncoderTest {

	private static Logger LOGGER = null;
	
	protected WaveCycleEncoder waveCycleMock = mock(WaveCycleEncoder.class);
	protected WaveCycleEncoder waveCycleEncoder;
	protected static final int SAMPLING_RATE = 8;

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
	 * Test method for {@link extension.encoder.FskBitEncoder#FskBitEncoder(int, int, int)}.
	 * 
	 * Check the correct exceptions on erroneous parameters.
	 * 
	 */
	@Test
	final void testFskBitEncoder() {
		LOGGER.info("testFskBitEncoder()");

		IllegalArgumentException thrown;
		
		/*
		 * Check for exception as long as the sampling rate is less or equal 0.
		 */
		thrown = assertThrows(IllegalArgumentException.class, () -> { new FskBitEncoder(1, 1, waveCycleMock); });
		assertTrue(thrown.getMessage().equals("aLowBitFrerquency can't be equal to aHighBitFrequency!"));
		
		thrown = assertThrows(IllegalArgumentException.class, () -> { new FskBitEncoder(0, 1, waveCycleMock); });
		assertTrue(thrown.getMessage().equals("aLowBitFrerquency must be greater 0!"));
		
		thrown = assertThrows(IllegalArgumentException.class, () -> { new FskBitEncoder(1, 0, waveCycleMock); });
		assertTrue(thrown.getMessage().equals("aHighBitFrequency must be greater 0!"));
		
		assertDoesNotThrow(() -> new FskBitEncoder(1, 2, waveCycleMock));
		
	} // testFskBitEncoder()

	/**
	 * Test method for {@link extension.encoder.BitEncoder#encode(int, int)}.
	 * 
	 * Verify the correct encoding of a 0-bit
	 * 
	 */
	@Test
	final void testEncode0Bit() {
		LOGGER.info("testEncode0Bit()");

		waveCycleEncoder = new WaveCycleEncoder(SAMPLING_RATE);
		BitEncoder cut = new FskBitEncoder(1, 2, waveCycleEncoder);
		
		ByteBuffer result = cut.encode(0, 1);
		byte[] ref = new byte[] { 0, 89, 127, 89, 0, -89, -127, -89 };
		
		LOGGER.debug("result " + result);
		
		for(int n = 0; n < result.limit(); n++) {
			LOGGER.debug("n: {}, result: {}, ref: {}", n, result.get(n), ref[n]);
			assertTrue(result.get(n) == ref[n]);
		}
		
	} // testEncode0Bit


	/**
	 * Test method for {@link extension.encoder.BitEncoder#encode(int, int)}.
	 * 
	 * Verify the correct encoding of a 1-bit
	 * 
	 */
	@Test
	final void testEncode1Bit() {
		LOGGER.info("testEncode1Bit()");

		waveCycleEncoder = new WaveCycleEncoder(SAMPLING_RATE);
		BitEncoder cut = new FskBitEncoder(1, 2, waveCycleEncoder);
		
		ByteBuffer result = cut.encode(1, 1);
		byte[] ref = new byte[] { 0, 127, 0, -127 };
		
		LOGGER.debug("result " + result);
		
		for(int n = 0; n < result.limit(); n++) {
			LOGGER.debug("n: {}, result: {}, ref: {}", n, result.get(n), ref[n]);
			assertTrue(result.get(n) == ref[n]);
		}
		
	} // testEncode1Bit


	/**
	 * Test method for {@link extension.encoder.Encoder#toString()}.
	 * 
	 * Check correct functionality of toString()
	 * 
	 */
	@Test
	final void testToString() {
		LOGGER.info("testToString()");

		WaveCycleEncoder wceMock = mock(WaveCycleEncoder.class);
		FskBitEncoder cut = new FskBitEncoder(10, 20, wceMock);
		
		LOGGER.info("cut: {}", cut.toString());
		
		assertTrue(cut.toString() != null);

	} // testToString
	

} // class
