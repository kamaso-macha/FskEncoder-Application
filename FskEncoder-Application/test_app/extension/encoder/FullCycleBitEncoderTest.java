/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : FullCycleBitEncoderTest.java
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
import extension.encoder.FullCycleBitEncoder;
import extension.encoder.WaveCycleEncoder;

/**
 * Responsibilities:<br>
 * Verifies the correct function of class FullCycleBitEncoder.
 * 
 * <p>
 * Collaborators:<br>
 * Class under test.
 * Private test implementation of the abstract class. 
 * 
 * 
 * <p>
 * Description:<br>
 * Verifies the correct error handling on erroneous parameters.
 * Tests the functionality of the two public methods.
 * 
 * <p>
 * @author Stefan
 *
 */

class FullCycleBitEncoderTest {

	private static Logger LOGGER = null;
	
	
	/**
	 * 
	 * Responsibilities:<br>
	 * Implement the abstract methods of the abstract FullCycleBitEncoder.
	 * 
	 * <p>
	 * Collaborators:<br>
	 * Base class.
	 * 
	 * <p>
	 * Description:<br>
	 * Implementation of abstract class for test purpose.
	 * 
	 * <p>
	 * @author Stefan
	 *
	 */
	protected class TestFullCycleBitEncoder extends FullCycleBitEncoder {

		/**
		 * @param aWaveCycleEncoder
		 */
		public TestFullCycleBitEncoder(final WaveCycleEncoder aWaveCycleEncoder) { 
			super(aWaveCycleEncoder); 
			
			prepareSampleBuffers();
		}

		/**
		 * To keep the tests as simple as possible, we fake the sample buffers.
		 */
		@Override
		protected void prepareSampleBuffers() {
			
			lowBitSamples = ByteBuffer.allocate(1);
			lowBitSamples.put((byte) 0);
			lowBitSamples.flip();
			LOGGER.info("lowBitSamples.position = {}", lowBitSamples.position());
			
			highBitSamples = ByteBuffer.allocate(2);
			highBitSamples.put((byte) 1).put((byte) 1);
			highBitSamples.flip();
			LOGGER.info("highBitSamples.position = {}", highBitSamples.position());
			
		} // prepareSampleBuffers()


	} // class
	
	protected WaveCycleEncoder waveCycleMock = mock(WaveCycleEncoder.class);

	
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
	 * Test method for {@link extension.encoder.FullCycleBitEncoder#FullCycleBitEncoder(int)}.
	 * 
	 * Assert that correct exceptions are thrown in case of invalid parameters.
	 * 
	 */
	@Test
	final void testFullCycleBitEncoder() {
		LOGGER.info("testFullCycleBitEncoder()");
		
		IllegalArgumentException thrown;
		
		thrown = assertThrows(IllegalArgumentException.class, () -> { new TestFullCycleBitEncoder(null); });
		assertTrue(thrown.getMessage().equals("aWaveCycleEncoder can't be null!"));
		
		assertDoesNotThrow(() -> { new TestFullCycleBitEncoder(waveCycleMock); });
		
	} // testFullCycleBitEncoder()
	

	/**
	 * Test method for {@link extension.encoder.FullCycleBitEncoder#getSampleBufferSize()}.
	 * 
	 * Validate the getSampleBufferSize() method.
	 * 
	 */
	@Test
	final void testGetSampleBufferSize() {
		
		BitEncoder cut = new TestFullCycleBitEncoder(waveCycleMock);

		/*
		 * In class TestFullCycleBitEncoder we set up two sampling buffers,
		 * one with size of 1 byte and one with 2 bytes..
		 */
		assertTrue(cut.getSampleBufferSize() == 2);
		
	} // testGetSampleBufferSize()

	
	/**
	 * Test method for {@link extension.encoder.FullCycleBitEncoder#encode(int, int)}.
	 * 
	 * Validate correct encoding of 0-bits
	 * 
	 */
	@Test
	final void testEncode0Bit() {
		LOGGER.info("testEncode0Bit()");
		
		BitEncoder cut = new TestFullCycleBitEncoder(waveCycleMock);
		ByteBuffer result;
		
		result = cut.encode(0, 1);

		// we're expecting a buffer of 1 byte.
		assertTrue(result != null);
		assertTrue(result.limit() == 1);
		assertTrue(result.get(0) == 0);
		
	} // testEncode0Bit()
	

	/**
	 * Test method for {@link extension.encoder.FullCycleBitEncoder#encode(int, int)}.
	 * 
	 * Validate correct encoding of 1-bits
	 * 
	 */
	@Test
	final void testEncode1Bit() {
		LOGGER.info("testEncode1Bit()");
		
		BitEncoder cut = new TestFullCycleBitEncoder(waveCycleMock);
		ByteBuffer result;
		
		result = cut.encode(1, 10);
		assertTrue(result != null);
		
		// we're expecting a buffer of 20 bytes.
		assertTrue(result.limit() == 20);
		assertTrue(result.get(0) == 1);
		assertTrue(result.get(19) == 1);
		
	} // testEncode1Bit()
	

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
		FullCycleBitEncoder cut = new TestFullCycleBitEncoder(wceMock);
		
		LOGGER.info("toString(): {}", cut.toString());

		assertTrue(cut.toString().startsWith(
				"FullCycleBitEncoder [lowBitSamples=java.nio.HeapByteBuffer[pos=0 lim=1 cap=1], " 
			      + "highBitSamples=java.nio.HeapByteBuffer[pos=0 lim=2 cap=2], " 
			      + "BitEncoder [waveCycleEncoder=Mock for WaveCycleEncoder, hashCode: "));
		
		assertTrue(cut.toString().endsWith("]]"));

	} // testToString
	

} // class
