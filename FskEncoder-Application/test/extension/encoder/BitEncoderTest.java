/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : BitEncoderTest.java
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
import static org.mockito.Mockito.*;
import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;

import extension.encoder.BitEncoder;
import extension.encoder.WaveCycleEncoder;

/**
 * Responsibilities:<br>
 * Test the BitEncoder class.
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

class BitEncoderTest {

	private static Logger LOGGER = null;
	
	
	/**
	 * 
	 * Responsibilities:<br>
	 * 	Implementation to test the abstract base class.
	 * 
	 * <p>
	 * Collaborators:<br>
	 * 	None.
	 * 
	 * <p>
	 * Description:<br>
	 *  It implements two simple stubs for the encodeLowBit and encodeHighBit methods
	 *  which returns a buffer with one single byte that indicates which method was called.<br>
	 *  A 0 value indicates encodeLowBit while a 1 byte points to encodeHighBit.
	 * 
	 * <p>
	 * @author Stefan
	 *
	 */
	protected class TestBitEncoder extends BitEncoder {

		protected ByteBuffer buffer;

		public TestBitEncoder(final WaveCycleEncoder aWaveCycleEncoder) {
			super(aWaveCycleEncoder);
		}

		@Override
		public int getSampleBufferSize() {
			return 0;
		}
		
		@Override
		protected ByteBuffer encodeLowBit(int aCount) { return null; }

		@Override
		protected ByteBuffer encodeHighBit(int aCount) { return null; }

	} // class TestBitEncoder
	
	
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
	 * Test method for {@link extension.encoder.BitEncoder#BitEncoder(int)}.
	 * 
	 * Verifies that erroneous arguments are handled with the correct exception.
	 * 
	 */
	@Test
	final void testBitEncoderErrorHandling() {
		LOGGER.info("testBitEncoderErrorHandling()");

		WaveCycleEncoder waveCycleMock = mock(WaveCycleEncoder.class);
		
		IllegalArgumentException thrown;
		
		/*
		 * Check for exception as long as the sampling rate is less or equal 0.
		 */
		thrown = assertThrows(IllegalArgumentException.class, () -> { new TestBitEncoder(null); });
		assertTrue(thrown.getMessage().equals("aWaveCycleEncoder can't be null!"));
		
		assertDoesNotThrow(() -> new TestBitEncoder(waveCycleMock) );
		
	} // testBitEncoderErrorHandling()

	
	/**
	 * Test method for {@link extension.encoder.BitEncoder#encode(int, int)}.
	 * 
	 * Verifies that erroneous arguments are handled with the correct exception.
	 * 
	 */
	@Test
	final void testEncodeErrorHandling() {
		LOGGER.info("testEncodeErrorHandling()");
		
		IllegalArgumentException thrown;
		BitEncoder mock = Mockito.mock(BitEncoder.class, Answers.CALLS_REAL_METHODS);
		
		thrown = assertThrows(IllegalArgumentException.class, () -> { mock.encode(-1, 1); });
		assertTrue(thrown.getMessage().equals("aBitValue must be in range 0 ... 1!"));
		
		thrown = assertThrows(IllegalArgumentException.class, () -> { mock.encode(0, -1); });
		assertTrue(thrown.getMessage().equals("aCount must be greater than 0!"));
		
		thrown = assertThrows(IllegalArgumentException.class, () -> { mock.encode(0, 0); });
		assertTrue(thrown.getMessage().equals("aCount must be greater than 0!"));
		
		assertDoesNotThrow(() -> mock.encode(0, 1));
		assertDoesNotThrow(() -> mock.encode(1, 1));
		
		thrown = assertThrows(IllegalArgumentException.class, () -> { mock.encode(2, 1); });
		assertTrue(thrown.getMessage().equals("aBitValue must be in range 0 ... 1!"));
		
	} // testEncodeErrorHandling()

	
	/**
	 * Test method for {@link extension.encoder.BitEncoder#encode(int, int)}.
	 * 
	 * Verifies the correct encoding of a 0-bit
	 * 
	 */
	@Test
	final void testEncode0Bit() {
		LOGGER.info("testEncode0Bit()");
		
		BitEncoder cut = Mockito.mock(BitEncoder.class, Answers.CALLS_REAL_METHODS);
		
		when(cut.encodeLowBit(0))
			.thenReturn(null);
	
		cut.encode(0, 1);
		verify(cut, times(1)).encodeLowBit(1);
		
	} // testEncode0Bit()

	
	/**
	 * Test method for {@link extension.encoder.BitEncoder#encode(int, int)}.
	 * 
	 * Verifies the correct encoding of a 1-bit
	 * 
	 */
	@Test
	final void testEncode1Bit() {
		LOGGER.info("testEncode1Bit()");
		
		BitEncoder cut = Mockito.mock(BitEncoder.class, Answers.CALLS_REAL_METHODS);
		
		when(cut.encodeLowBit(1))
		.thenReturn(null);
	
		cut.encode(1, 1);
		verify(cut, times(1)).encodeHighBit(1);
		
	} // testEncode1Bit()
	
	
	/**
	 * Test method for {@link extension.encoder.BitEncoder#getSampleBufferSize()}.
	 * 
	 * Verifies the correct action for getSampleBufferSize().
	 * 
	 */
	@Test
	final void testGetSampleBufferSize() {
		LOGGER.info("testGetSampleBufferSize()");
		
		BitEncoder cut = Mockito.mock(BitEncoder.class, Answers.CALLS_REAL_METHODS);
		
		when(cut.encodeLowBit(1))
		.thenReturn(null);
	
		cut.getSampleBufferSize();
		verify(cut, times(1)).getSampleBufferSize();
		
	} // testGetSampleBufferSize()
	
	
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
		BitEncoder cut = new TestBitEncoder(wceMock); 
		
		LOGGER.info("cut: {}", cut.toString());
		
		assertTrue(cut.toString() != null);

	} // testToString
	

} // class
