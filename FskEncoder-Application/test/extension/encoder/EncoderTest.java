/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : EncoderTest.java
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
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import extension.encoder.BitEncoder;
import extension.encoder.BitOrder;
import extension.encoder.BitValue;
import extension.encoder.ByteOrder;
import extension.encoder.Encoder;
import extension.encoder.SilenceEncoder;
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
 * Asserts the correct functionality of Encoder class.
 * 
 * For a detailed picture of applied tests please refer to the 
 * description of the test methods.
 * <p>
 * NOTE:
 * <br>
 * Lot of tests use excessively the capabilities of Mockito framework!
 * 
 * <p>
 * @author Stefan
 *
 */

class EncoderTest {

	private static Logger LOGGER = null;
	
	private static final int SB_SIZE = 1024;

	private WaveCycleEncoder waveCycleMock;
	private BitEncoder bitEncoderMock;
	private SilenceEncoder silenceEncoderMock;

	private ByteBuffer sampleBufferMock;
	
	
	/*
	 * ===================================================================================================================
	 */

	/*
	 * Needed for protocol tracking.
	 * 
	 * Mockito isn't able to track distinct calls of a method with the same parameters. 
	 * So we have to do this on our own.
	 * 
	 * TODO: Minor. Adopt this test to the utility classes in protocol.support.
	 * 
	 */
	
	/*
	 * Encapsulates all information about a single step in the protocol.
	 */
	class Step {
		
		protected final int bitValue;
		protected final int bitCount;
		
		public Step(final int aBitValue, final int aBitCont) {
			bitValue = aBitValue;
			bitCount = aBitCont;
		}
		
		public boolean verify(final Step aReference) {
			return (aReference.bitValue == bitValue && aReference.bitCount == bitCount);
		}

		@Override
		public String toString() {
			return "Step [bitValue=" + bitValue + ", bitCount=" + bitCount + "]";
		}
		
	} // Step
	
	
	/*
	 * Needed for protocol tracking.
	 * 
	 * Mockito isn't able to track distinct calls of a method with the same parameters. 
	 * So we have to do this on our own.
	 * 
	 */
	
	/*
	 * Keeps track of a sequence of steps.
	 */
	class Sequence {
		
		protected Step[] steps; 
		protected int sequence = 0;
		
		public Sequence(final int nbrOfSteps) {
			LOGGER.info("Sequence(): nbrOfSteps = {}", nbrOfSteps);
			steps = new Step[nbrOfSteps];
		}
		
		public void add(final Step aStep) {
			LOGGER.info("Sequence.add(): sequence = {}, step = {}",sequence, aStep);
			steps[sequence] = aStep;
			sequence++;
		}
		
		public int getSequence() { return sequence; }
		public Step[] getSteps() { return steps; }

		@Override
		public String toString() {
			return "Sequence [steps=" + Arrays.toString(steps) + ", sequence=" + sequence + "]";
		}
		
	} // Sequence
	

	/*
	 * Needed for protocol tracking.
	 * 
	 * Mockito isn't able to track distinct calls of a method with the same parameters. 
	 * So we have to do this on our own.
	 * 
	 */
	
	/*
	 * Interface to Mockito framework
	 */
	class EncodeAnswer implements Answer<Object> {
		
		final int bufferSize;
		final Sequence sequence;
		
		public EncodeAnswer(final int aBufferSize, final Sequence aSequence) {
			LOGGER.info("EncodeAnswer(): aBufferSize = {}, aSequence = {}", aBufferSize, aSequence);
			bufferSize = aBufferSize;
			sequence   = aSequence;
		}

		@Override
		public Object answer(InvocationOnMock invocation) throws Throwable {
			Object target = invocation.getMock();
			String method = invocation.getMethod().getName();
			int bitvalue = invocation.getArgument(0);
			int bitCount = invocation.getArgument(1);
			
			for(int n = 0; n < bitCount; n++) {
				sequence.add(new Step(bitvalue, 1));
			}
			ByteBuffer buffer = ByteBuffer.allocate(bufferSize * bitCount);
			
			LOGGER.debug("EncodeAnswer.answer(): arguments: {}, target: {}, method: {}, sequence = {}, buffer = {}", 
				invocation, target, method, sequence.getSequence(), buffer);
			
			return buffer;
		}
		
	} // EncodeAnswer
	
	/*
	 * ===================================================================================================================
	 */
	
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

		waveCycleMock = mock(WaveCycleEncoder.class);
		bitEncoderMock = mock(BitEncoder.class);
		silenceEncoderMock = mock(SilenceEncoder.class);
		
		sampleBufferMock = ByteBuffer.allocate(SB_SIZE / 2);
		
	}
	

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	
	
	/**
	 * Test method for {@link extension.encoder.Encoder#Encoder(extension.encoder.BitEncoder, extension.encoder.ByteOrder, extension.encoder.BitOrder)}.
	 * 
	 * Error recognition checking on constructor.
	 * 
	 */
	@Test
	final void testEncoder() {
		LOGGER.info("testEncoder()");
		
		IllegalArgumentException thrown;
		
		thrown = assertThrows(IllegalArgumentException.class, () -> new Encoder(null, null, null));
		assertTrue(thrown.getMessage().equals("aBitEncoder can't be null!"));
		
		thrown = assertThrows(IllegalArgumentException.class, () -> new Encoder(bitEncoderMock, null, null));
		assertTrue(thrown.getMessage().equals("aByteOrder can't be null!"));
		
		thrown = assertThrows(IllegalArgumentException.class, () -> new Encoder(bitEncoderMock, ByteOrder.BIG_ENDIAN, null));
		assertTrue(thrown.getMessage().equals("aBitOrder can't be null!"));
		
		assertDoesNotThrow(() -> new Encoder(bitEncoderMock, ByteOrder.LITTLE_ENDIAN, BitOrder.LSB_MSB));
		
	} // testEncoder()

	
	/**
	 * Test method for {@link extension.encoder.Encoder#withSilenceEncoder(extension.encoder.SilenceEncoder)}.
	 * 
	 * Error recognition checking on withSilenceEncoder method.
	 * 
	 */
	@Test
	final void testWithSilenceEncoder() {
		LOGGER.info("testWithSilenceEncoder()");
		
		IllegalAccessError illegalAccessthrown;
		IllegalArgumentException illegalArgThrown;
		
		Encoder cut = new Encoder(bitEncoderMock, ByteOrder.LITTLE_ENDIAN, BitOrder.LSB_MSB);
		
		illegalAccessthrown = assertThrows(IllegalAccessError.class, () -> cut.encodeSilence(500));
		assertTrue(illegalAccessthrown.getMessage().equals("No SilenceEncoder configured!"));
		
		illegalArgThrown = assertThrows(IllegalArgumentException.class, () -> cut.withSilenceEncoder(null) );
		assertTrue(illegalArgThrown.getMessage().equals("aSilenceEncoder can't be null!"));
		
	} // testWithSilenceEncoder()
	

	/**
	 * Test method for {@link extension.encoder.Encoder#withWaveCycleEncoder(encoder.withWaveCycleEncoder)}.
	 * 
	 * Error recognition checking on withWaveCycleEncoder method.
	 * 
	 */
	@Test
	final void testWithWaveCycleEncoder() {
		LOGGER.info("testWithWaveCycleEncoder()");
		
		IllegalAccessError illegalAccessthrown;
		IllegalArgumentException illegalArgThrown;

		Encoder cut = new Encoder(bitEncoderMock, ByteOrder.LITTLE_ENDIAN, BitOrder.LSB_MSB);

		// error handling
		illegalAccessthrown = assertThrows(IllegalAccessError.class, () -> cut.encodeWaveCycle(1, 1));
		assertTrue(illegalAccessthrown.getMessage().equals("No WaveCycleEncoder configured!"));
		
		illegalArgThrown = assertThrows(IllegalArgumentException.class, () -> cut.withWaveCycleEncoder(null) );
		assertTrue(illegalArgThrown.getMessage().equals("aWaveCycleEncoder can't be null!"));
		
		assertDoesNotThrow(() -> cut.withWaveCycleEncoder(waveCycleMock) );

	} // testWithWaveCycleEncoder()
	
	
	/**
	 * Test method for {@link extension.encoder.Encoder#withStartBits(int, extension.encoder.BitValue)}.
	 */
	@Test
	final void testWithStartBits() {
		LOGGER.info("testWithStartBits()");
		
		/*
		 * Method has NO error handling!
		 * 
		 *  Implicitly tested by the methods
		 *  ...WithEnvelope
		 *  
		 */
		
		assertTrue(true);
		
	} // testWithStartBits()

	
	/**
	 * Test method for {@link extension.encoder.Encoder#withStopBits(int, extension.encoder.BitValue)}.
	 */
	@Test
	final void testWithStopBits() {
		LOGGER.info("testWithStopBits()");
		
		/*
		 * Method has NO error handling!
		 * 
		 *  Implicitly tested by the methods
		 *  ...WithEnvelope
		 *  
		 */
		
		assertTrue(true);
		
	} // testWithStopBits()

	
	/**
	 * Test method for {@link extension.encoder.Encoder#getSampleBufferSize()}.
	 * 
	 * Check correct functionality of getSampleBufferSize().
	 * 
	 */
	@Test
	final void testGetSampleSize() {
		LOGGER.info("testGetSampleBuffer()");
		
		Encoder cut = new Encoder(bitEncoderMock, ByteOrder.LITTLE_ENDIAN, BitOrder.LSB_MSB);
		
		when(bitEncoderMock.getSampleBufferSize()).thenReturn(42);
		
		int sampleSize = cut.getSampleSize();
		verify(bitEncoderMock, times(1)).getSampleBufferSize();
		
		assertTrue(sampleSize == 42);
		
	} // testGetSampleSize()
	
	
	/**
	 * Test method for {@link extension.encoder.Encoder#getSampleBuffer()}.
	 * 
	 * Check correct functionality of getSampleBuffer().
	 * 
	 */
	@Test
	final void testGetSampleBuffer() {
		LOGGER.info("testGetSampleBuffer()");
		
		IllegalAccessError illegalAccessthrown;

		Encoder cut = new Encoder(bitEncoderMock, ByteOrder.LITTLE_ENDIAN, BitOrder.LSB_MSB);
		assertTrue(cut != null);
		
		illegalAccessthrown = assertThrows(IllegalAccessError.class, () -> cut.getSampleBuffer());
		assertTrue(illegalAccessthrown.getMessage().equals("Sample buffer size not set!"));
		
		cut.setBufferSize(SB_SIZE);
		
		ByteBuffer result = cut.getSampleBuffer();
		
		assertTrue(result.capacity() == SB_SIZE);
		
	} // testGetSampleBuffer()
	

	/**
	 * Test method for {@link extension.encoder.Encoder#clearSampleBuffer()}.
	 * 
	 * Check correct functionality of clearSampleBuffer().
	 * 
	 */
	@Test
	final void testClearSampleBuffer() {
		LOGGER.info("testClearSampleBuffer()");
		
		IllegalAccessError illegalAccessthrown;

		Encoder cut = new Encoder(bitEncoderMock, ByteOrder.LITTLE_ENDIAN, BitOrder.LSB_MSB);

		/*
		 * Error handling
		 */
		illegalAccessthrown = assertThrows(IllegalAccessError.class, () -> cut.clearSampleBuffer());
		assertTrue(illegalAccessthrown.getMessage().equals("Sample buffer size not set!"));
		
		cut.setBufferSize(SB_SIZE);
		
		ByteBuffer result = cut.getSampleBuffer();
		
		// verify the buffer is empty
		assertTrue(result.capacity() == SB_SIZE);
		assertTrue(result.position() == 0);
		assertTrue(result.limit() == SB_SIZE);
		
		when(bitEncoderMock.encode((byte)0, 1)).thenReturn(sampleBufferMock);

		cut.encodeBit((byte) 0, 1);
		verify(bitEncoderMock, times(1)).encode(0, 1);
		
		result = cut.getSampleBuffer();
		
		// verify buffer NOT empty
		assertTrue(result.capacity() == SB_SIZE);
		assertTrue(result.position() == 0);
		assertTrue(result.limit() == SB_SIZE / 2);
		
		cut.clearSampleBuffer();
		result = cut.getSampleBuffer();
		
		// verify buffer is empty
		assertTrue(result.capacity() == SB_SIZE);
		assertTrue(result.position() == 0);
		assertTrue(result.limit() == SB_SIZE);
		
	} // testClearSampleBuffer()
	

	/**
	 * Test method for {@link extension.encoder.Encoder#encodeSilence(int, int)}.
	 * 
	 * Check correct functionality of encodeSilence().
	 * 
	 */
	@Test
	final void testEncodeSilence() {
		LOGGER.info("testEncodeSilence()");
		
		
		IllegalAccessError illegalAccessthrown;
		
		Encoder cut = new Encoder(bitEncoderMock, ByteOrder.LITTLE_ENDIAN, BitOrder.LSB_MSB);
		cut.withSilenceEncoder(silenceEncoderMock);

		// error handling
		illegalAccessthrown = assertThrows(IllegalAccessError.class, () -> cut.encodeSilence(500));
		assertTrue(illegalAccessthrown.getMessage().equals("Sample buffer size not set!"));
		
		// setup
		cut.setBufferSize(SB_SIZE);
		
		when(silenceEncoderMock.encode(1000)).thenReturn(sampleBufferMock);
		
		// functionality
		cut.encodeSilence(1000);		
		verify(silenceEncoderMock, times(1)).encode(1000);
		
	} // testEncodeSilence()
	

	/**
	 * Test method for {@link extension.encoder.Encoder#encodeWaveCycle(int, int)}.
	 * 
	 * Check correct functionality of encodeWaveCycle().
	 * 
	 */
	@Test
	final void testEncodeWaveCycle() {
		LOGGER.info("testEncodeWaveCycle()");
		
		IllegalAccessError illegalAccessthrown;

		Encoder cut = new Encoder(bitEncoderMock, ByteOrder.LITTLE_ENDIAN, BitOrder.LSB_MSB);
		cut.withWaveCycleEncoder(waveCycleMock);
		
		// error handling
		illegalAccessthrown = assertThrows(IllegalAccessError.class, () -> cut.encodeWaveCycle(1000, 10));
		assertTrue(illegalAccessthrown.getMessage().equals("Sample buffer size not set!"));
		
		// setup
		cut.setBufferSize(SB_SIZE);
				
		when(waveCycleMock.encode(1000, 10)).thenReturn(sampleBufferMock);
		
		// functionality
		cut.encodeWaveCycle(1000, 10);
		verify(waveCycleMock, times(1)).encode(1000, 10);
		
		ByteBuffer result = cut.getSampleBuffer();
		
		// verify buffer NOT empty
		assertTrue(result.capacity() == SB_SIZE);
		assertTrue(result.position() == 0);
		assertTrue(result.limit() == SB_SIZE / 2);

	} // testEncodeWaveCycle()
	

	/**
	 * Test method for {@link extension.encoder.Encoder#encodeBit(byte, int)}.
	 * 
	 * Check correct functionality of encodeBit().
	 * 
	 */
	@Test
	final void testEncodeBit() {
		LOGGER.info("testEncodeBit()");
		
		IllegalAccessError illegalAccessthrown;

		Encoder cut = new Encoder(bitEncoderMock, ByteOrder.LITTLE_ENDIAN, BitOrder.LSB_MSB);

		// error handling
		illegalAccessthrown = assertThrows(IllegalAccessError.class, () -> cut.encodeBit((byte) 1, 8));
		assertTrue(illegalAccessthrown.getMessage().equals("Sample buffer size not set!"));
		
		// setup
		cut.setBufferSize(SB_SIZE);
				
		when(bitEncoderMock.encode((byte) 1, 8)).thenReturn(sampleBufferMock);
		
		// functionality
		cut.encodeBit((byte) 1, 8);
		verify(bitEncoderMock, times(1)).encode((byte) 1, 8);
		
		ByteBuffer result = cut.getSampleBuffer();
		
		// verify buffer NOT empty
		assertTrue(result.capacity() == SB_SIZE);
		assertTrue(result.position() == 0);
		assertTrue(result.limit() == SB_SIZE / 2);

	} // testEncodeBit()

	
	/**
	 * Test method for {@link extension.encoder.Encoder#encodeByte(byte, boolean, boolean)}.
	 * 
	 * Check correct functionality of encodeByte() in mode LSB_MSB.
	 * 
	 */	
	@Test
	final void testEncodeByteLsbFirst() throws Throwable {
		LOGGER.info("testEncodeByteLsbFirst()");

		final int BUFFER_SIZE = 4;

		// LSB first
		Step[] ref   = new Step[] {
			new Step(1, 1), new Step(1, 1), new Step(1, 1), new Step(1, 1),
			new Step(0, 1), new Step(0, 1), new Step(0, 1), new Step(0, 1),
		};

		Sequence sequence = new Sequence(ref.length * 2);
		EncodeAnswer ea = new EncodeAnswer(BUFFER_SIZE, sequence);

		IllegalAccessError illegalAccessthrown;

		Encoder cut = new Encoder(bitEncoderMock, ByteOrder.LITTLE_ENDIAN, BitOrder.LSB_MSB);

		// error handling
		illegalAccessthrown = assertThrows(IllegalAccessError.class, () -> cut.encodeByte((byte)0x0F, false));
		assertTrue(illegalAccessthrown.getMessage().equals("Sample buffer size not set!"));
		
		// setup
		cut.setBufferSize(SB_SIZE);
		
		doAnswer(ea).when(bitEncoderMock).encode(anyInt(), anyInt());

		// functionality
		cut.encodeByte((byte)0x0F, false);
		
		ByteBuffer result = cut.getSampleBuffer();
		LOGGER.info("testEncodeByte(): result = {}", result);
		
		// verify buffer NOT empty
		assertTrue(result.capacity() == SB_SIZE);
		assertTrue(result.position() == 0);
		assertTrue(result.limit() == BUFFER_SIZE * 8);
		
		Step[] steps = sequence.getSteps();
		
		assertTrue(sequence.getSequence() == ref.length);
		
		for(int n = 0; n < ref.length; n++) {
			LOGGER.info("n: {}, step: {}, ref: {}", n, steps[n], ref[n]);
			assertTrue(steps[n].verify(ref[n]));
		}
		
	} // testEncodeByteLsbFirst()

	
	/**
	 * Test method for {@link extension.encoder.Encoder#encodeByte(byte, boolean, boolean)}.
	 * 
	 * Check correct functionality of encodeByte() in mode MSB_LSB.
	 * 
	 */
	@Test
	final void testEncodeByteMsbFirst() {
		LOGGER.info("testEncodeByteMsbFirst()");

		final int BUFFER_SIZE = 4;

		// MSB first
		Step[] ref   = new Step[] {
			new Step(0, 1), new Step(0, 1), new Step(0, 1), new Step(0, 1),
			new Step(1, 1), new Step(1, 1), new Step(1, 1), new Step(1, 1),
		};

		Sequence sequence = new Sequence(ref.length * 2);
		EncodeAnswer ea = new EncodeAnswer(BUFFER_SIZE, sequence);

		IllegalAccessError illegalAccessthrown;

		Encoder cut = new Encoder(bitEncoderMock, ByteOrder.LITTLE_ENDIAN, BitOrder.MSB_LSB);

		// error handling
		illegalAccessthrown = assertThrows(IllegalAccessError.class, () -> cut.encodeByte((byte)0x0F, false));
		assertTrue(illegalAccessthrown.getMessage().equals("Sample buffer size not set!"));

		// setup
		cut.setBufferSize(SB_SIZE);
		
		doAnswer(ea).when(bitEncoderMock).encode(anyInt(), anyInt());

		// functionality
		cut.encodeByte((byte)0x0F, false);
		
		ByteBuffer result = cut.getSampleBuffer();
		LOGGER.info("testEncodeByte(): result = {}", result);
		
		// verify buffer NOT empty
		assertTrue(result.capacity() == SB_SIZE);
		assertTrue(result.position() == 0);
		assertTrue(result.limit() == BUFFER_SIZE * 8);
		
		Step[] steps = sequence.getSteps();
		
		assertTrue(sequence.getSequence() == ref.length);
		
		for(int n = 0; n < ref.length; n++) {
			LOGGER.info("n: {}, step: {}, ref: {}", n, steps[n], ref[n]);
			assertTrue(steps[n].verify(ref[n]));
		}
		
	} // testEncodeByteMsbFirst()

	
	/**
	 * Test method for {@link extension.encoder.Encoder#encodeByte(byte, boolean, boolean)}.
	 * 
	 * Check correct functionality of encodeByte() with envelope (start and stop bits).
	 * 
	 */
	@Test
	final void testEncodeByteWithEnvelope() {
		LOGGER.info("testEncodeByteWithEnvelope()");

		final int BUFFER_SIZE = 4;

		/*
		 * If start and stop bits are configured, than they MUST appear as the first 
		 * entries in the reference buffer.
		 * Why?
		 * Because they are generated in the point when they are configured as precompiled samples 
		 * and NOT when they are placed into the sample buffer!
		 */
		// LSB first
		Step[] ref   = new Step[] {
			// start bits
			new Step(1, 1),
			// stop bits
			new Step(0, 1), new Step(0, 1),
			
			// data bits
			new Step(1, 1), new Step(1, 1), new Step(1, 1), new Step(1, 1),
			new Step(0, 1), new Step(0, 1), new Step(0, 1), new Step(0, 1),
		};

		Sequence sequence = new Sequence(ref.length * 2);
		EncodeAnswer ea = new EncodeAnswer(BUFFER_SIZE, sequence);
		
		doAnswer(ea).when(bitEncoderMock).encode(anyInt(), anyInt());

		IllegalAccessError illegalAccessthrown;

		Encoder cut = new Encoder(bitEncoderMock, ByteOrder.LITTLE_ENDIAN, BitOrder.LSB_MSB);
		cut.withStartBits(1, BitValue.HIGH);
		cut.withStopBits(2, BitValue.LOW);

		// error handling
		illegalAccessthrown = assertThrows(IllegalAccessError.class, () -> cut.encodeByte((byte)0x0F, true));
		assertTrue(illegalAccessthrown.getMessage().equals("Sample buffer size not set!"));
		
		// setup
		cut.setBufferSize(SB_SIZE);
		
		// functionality
		cut.encodeByte((byte)0x0F, true);
		
		ByteBuffer result = cut.getSampleBuffer();
		LOGGER.info("testEncodeByte(): result = {}", result);
		
		// verify buffer NOT empty
		assertTrue(result.capacity() == SB_SIZE);
		assertTrue(result.position() == 0);
		assertTrue(result.limit() == BUFFER_SIZE * 11);
		
		Step[] steps = sequence.getSteps();
		
		LOGGER.info("sequence: {}, ref: {}", sequence.getSequence(), ref.length);
		assertTrue(sequence.getSequence() == ref.length);
		
		// compare the recorded sequence against the predefined reference.
		for(int n = 0; n < ref.length; n++) {
			LOGGER.info("n: {}, step: {}, ref: {}", n, steps[n], ref[n]);
			assertTrue(steps[n].verify(ref[n]));
		}
		
	} // testEncodeByteWithEnvelope()

	
	/**
	 * Test method for {@link extension.encoder.Encoder#encodeByteBuffer(java.nio.ByteBuffer, boolean, boolean)}.
	 * 
	 * Check correct functionality of encodeByteBuffer().
	 * 
	 */
	@Test
	final void testEncodeByteBuffer() {
		LOGGER.info("testEncodeByteBuffer()");

		final int BUFFER_SIZE = 4;

		/*
		 * If start and stop bits are configured, than they MUST appear as the first 
		 * entries in the reference buffer.
		 * Why?
		 * Because they are generated in the point when they are configured as precompiled samples 
		 * and NOT when they are placed into the sample buffer!
		 */
		// LSB first
		Step[] ref   = new Step[] {
			// data bits
			new Step(1, 1), new Step(1, 1), new Step(1, 1), new Step(1, 1),		// F
			new Step(1, 1), new Step(1, 1), new Step(1, 1), new Step(1, 1),		// F

			new Step(1, 1), new Step(0, 1), new Step(1, 1), new Step(0, 1),		// 5
			new Step(0, 1), new Step(1, 1), new Step(0, 1), new Step(1, 1),		// A
			
			new Step(0, 1), new Step(1, 1), new Step(0, 1), new Step(1, 1),		// A
			new Step(1, 1), new Step(0, 1), new Step(1, 1), new Step(0, 1),		// 5
			
			new Step(0, 1), new Step(0, 1), new Step(0, 1), new Step(0, 1),		// 0
			new Step(0, 1), new Step(0, 1), new Step(0, 1), new Step(0, 1),		// 0
		};
		
		ByteBuffer dataBuffer = ByteBuffer.allocate(4)
			.put((byte) 0xFF)
			.put((byte) 0xA5)
			.put((byte) 0x5A)
			.put((byte) 0x00)
		;

		Sequence sequence = new Sequence(ref.length * 2);
		EncodeAnswer ea = new EncodeAnswer(BUFFER_SIZE, sequence);
		
		doAnswer(ea).when(bitEncoderMock).encode(anyInt(), anyInt());

		IllegalAccessError illegalAccessthrown;

		Encoder cut = new Encoder(bitEncoderMock, ByteOrder.LITTLE_ENDIAN, BitOrder.LSB_MSB);

		// error handling
		illegalAccessthrown = assertThrows(IllegalAccessError.class, () -> cut.encodeByteBuffer(dataBuffer, false));
		assertTrue(illegalAccessthrown.getMessage().equals("Sample buffer size not set!"));
		
		// setup
		cut.setBufferSize(SB_SIZE);
		
		// functionality
		cut.encodeByteBuffer(dataBuffer, false);
		
		ByteBuffer result = cut.getSampleBuffer();
		LOGGER.info("testEncodeByte(): result = {}", result);
		
		// verify buffer NOT empty
		assertTrue(result.capacity() == SB_SIZE);
		assertTrue(result.position() == 0);
		assertTrue(result.limit() == BUFFER_SIZE * 32);
		
		Step[] steps = sequence.getSteps();
		
		LOGGER.info("sequence: {}, ref: {}", sequence.getSequence(), ref.length);
		assertTrue(sequence.getSequence() == ref.length);
		
		// compare the recorded sequence against the predefined reference.
		for(int n = 0; n < ref.length; n++) {
			LOGGER.info("n: {}, step: {}, ref: {}", n, steps[n], ref[n]);
			assertTrue(steps[n].verify(ref[n]));
		}
		
		LOGGER.info("testEncodeByteBuffer(): E N D");

	} // testEncodeByteBuffer()

	
	/**
	 * Test method for {@link extension.encoder.Encoder#encodeByteBuffer(java.nio.ByteBuffer, boolean, boolean)}.
	 * 
	 * Check correct functionality of encodeByteBuffer().
	 * 
	 */
	@Test
	final void testEncodeByteBufferWithEnvelope() {
		LOGGER.info("testEncodeByteBufferWithEnvelope()");

		final int BUFFER_SIZE = 4;

		/*
		 * If start and stop bits are configured, than they MUST appear as the first 
		 * entries in the reference buffer.
		 * Why?
		 * Because they are generated in the point when they are configured as precompiled samples 
		 * and NOT when they are placed into the sample buffer!
		 */
		// LSB first
		Step[] ref   = new Step[] {
			// start bits
			new Step(1, 1), new Step(1, 1), 
			// stop bits
			new Step(0, 1), new Step(0, 1),

			// data bits
			new Step(1, 1), new Step(1, 1), new Step(1, 1), new Step(1, 1),		// F
			new Step(1, 1), new Step(1, 1), new Step(1, 1), new Step(1, 1),		// F

			new Step(1, 1), new Step(0, 1), new Step(1, 1), new Step(0, 1),		// 5
			new Step(0, 1), new Step(1, 1), new Step(0, 1), new Step(1, 1),		// A
			
			new Step(0, 1), new Step(1, 1), new Step(0, 1), new Step(1, 1),		// A
			new Step(1, 1), new Step(0, 1), new Step(1, 1), new Step(0, 1),		// 5
			
			new Step(0, 1), new Step(0, 1), new Step(0, 1), new Step(0, 1),		// 0
			new Step(0, 1), new Step(0, 1), new Step(0, 1), new Step(0, 1),		// 0
		};
		
		ByteBuffer dataBuffer = ByteBuffer.allocate(4)
			.put((byte) 0xFF)
			.put((byte) 0xA5)
			.put((byte) 0x5A)
			.put((byte) 0x00)
		;

		int expectedLimit = (BUFFER_SIZE * 12) * 4;		// 12 bit/envelope * 4 bytes 
		
		Sequence sequence = new Sequence(ref.length * 2);
		EncodeAnswer ea = new EncodeAnswer(BUFFER_SIZE, sequence);
		
		doAnswer(ea).when(bitEncoderMock).encode(anyInt(), anyInt());

		IllegalAccessError illegalAccessthrown;

		Encoder cut = new Encoder(bitEncoderMock, ByteOrder.LITTLE_ENDIAN, BitOrder.LSB_MSB);
		cut.withStartBits(2, BitValue.HIGH);
		cut.withStopBits(2, BitValue.LOW);

		// error handling
		illegalAccessthrown = assertThrows(IllegalAccessError.class, () -> cut.encodeByteBuffer(dataBuffer, true) );
		assertTrue(illegalAccessthrown.getMessage().equals("Sample buffer size not set!"));
		
		// setup
		cut.setBufferSize(SB_SIZE);
		
		// functionality
		cut.encodeByteBuffer(dataBuffer, true);
		
		ByteBuffer result = cut.getSampleBuffer();
		LOGGER.info("testEncodeByte(): result = {}", result);
		
		// verify buffer NOT empty
		assertTrue(result.capacity() == SB_SIZE);
		assertTrue(result.position() == 0);
		assertTrue(result.limit() == expectedLimit);
		
		Step[] steps = sequence.getSteps();
		
		LOGGER.info("sequence: {}, ref: {}", sequence.getSequence(), ref.length);
		assertTrue(sequence.getSequence() == ref.length);
		
		// compare the recorded sequence against the predefined reference.
		for(int n = 0; n < ref.length; n++) {
			LOGGER.info("n: {}, step: {}, ref: {}", n, steps[n], ref[n]);
			assertTrue(steps[n].verify(ref[n]));
		}
		
		LOGGER.info("testEncodeByteBufferWithEnvelope(): E N D");

	} // testEncodeByteBufferWithEnvelope()

	
	/**
	 * Test method for {@link extension.encoder.Encoder#encodeWord(int, boolean, boolean)}.
	 * 
	 * Check correct functionality of encodeWord() using ByteOrder.LITTLE_ENDIAN and BitOrder.LSB_MSB
	 * 
	 */
	@Test
	final void testEncodeWord() {
		LOGGER.info("testEncodeWord()");

		final int BUFFER_SIZE = 4;

		/*
		 * If start and stop bits are configured, than they MUST appear as the first 
		 * entries in the reference buffer.
		 * Why?
		 * Because they are generated in the point when they are configured as precompiled samples 
		 * and NOT when they are placed into the sample buffer!
		 */
		// LSB first
		Step[] ref   = new Step[] {
			// data bits
			new Step(1, 1), new Step(0, 1), new Step(1, 1), new Step(0, 1),		// 5
			new Step(0, 1), new Step(1, 1), new Step(0, 1), new Step(1, 1),		// A
			new Step(1, 1), new Step(1, 1), new Step(1, 1), new Step(1, 1),		// F
			new Step(0, 1), new Step(0, 1), new Step(0, 1), new Step(0, 1),		// 0
			
		};

		int expectedLimit = (BUFFER_SIZE * 8) * 2;		// 8 bit/envelope * 2 bytes 
		
		Sequence sequence = new Sequence(ref.length * 2);
		EncodeAnswer ea = new EncodeAnswer(BUFFER_SIZE, sequence);
		
		doAnswer(ea).when(bitEncoderMock).encode(anyInt(), anyInt());

		IllegalAccessError illegalAccessthrown;

		Encoder cut = new Encoder(bitEncoderMock, ByteOrder.LITTLE_ENDIAN, BitOrder.LSB_MSB);

		// error handling
		illegalAccessthrown = assertThrows(IllegalAccessError.class, () -> cut.encodeWord((int)0x0FA5, false) );
		assertTrue(illegalAccessthrown.getMessage().equals("Sample buffer size not set!"));
		
		// setup
		cut.setBufferSize(SB_SIZE);

		// functionality
		cut.encodeWord((int)0x0FA5, false);
		
		ByteBuffer result = cut.getSampleBuffer();
		LOGGER.info("testEncodeByte(): result = {}", result);
		
		// verify buffer NOT empty
		assertTrue(result.capacity() == SB_SIZE);
		assertTrue(result.position() == 0);
		assertTrue(result.limit() == expectedLimit);
		
		Step[] steps = sequence.getSteps();
		
		LOGGER.info("sequence: {}, ref: {}", sequence.getSequence(), ref.length);
		assertTrue(sequence.getSequence() == ref.length);
		
		// compare the recorded sequence against the predefined reference.
		for(int n = 0; n < ref.length; n++) {
			LOGGER.info("n: {}, step: {}, ref: {}", n, steps[n], ref[n]);
			assertTrue(steps[n].verify(ref[n]));
		}
		
	} // testEncodeWord()

	
	/**
	 * Test method for {@link extension.encoder.Encoder#encodeWord(int, boolean, boolean)}.
	 * 
	 * Check correct functionality of encodeWord() using ByteOrder.BIG_ENDIAN and BitOrder.MSB_LSB
	 * 
	 */
	@Test
	final void testEncodeWordWithEnvelope() {
		LOGGER.info("testEncodeWord()");

		final int BUFFER_SIZE = 4;

		/*
		 * If start and stop bits are configured, than they MUST appear as the first 
		 * entries in the reference buffer.
		 * Why?
		 * Because they are generated in the point when they are configured as precompiled samples 
		 * and NOT when they are placed into the sample buffer!
		 */
		// LSB first
		Step[] ref   = new Step[] {
			// start bits
			new Step(1, 1),
			// stop bits
			new Step(0, 1), new Step(0, 1),
			
			// data bits
			new Step(0, 1), new Step(0, 1), new Step(0, 1), new Step(0, 1),		// 0
			new Step(1, 1), new Step(1, 1), new Step(1, 1), new Step(1, 1),		// F			
			new Step(1, 1), new Step(0, 1), new Step(1, 1), new Step(0, 1),		// A
			new Step(0, 1), new Step(1, 1), new Step(0, 1), new Step(1, 1),		// 5
			
		};

		int expectedLimit = (BUFFER_SIZE * 11) * 2;		// 8 bit/envelope * 2 bytes 
		
		Sequence sequence = new Sequence(ref.length * 2);
		EncodeAnswer ea = new EncodeAnswer(BUFFER_SIZE, sequence);
		
		doAnswer(ea).when(bitEncoderMock).encode(anyInt(), anyInt());

		IllegalAccessError illegalAccessthrown;

		Encoder cut = new Encoder(bitEncoderMock, ByteOrder.BIG_ENDIAN, BitOrder.MSB_LSB);
		cut.withStartBits(1, BitValue.HIGH);
		cut.withStopBits(2, BitValue.LOW);

		// error handling
		illegalAccessthrown = assertThrows(IllegalAccessError.class, () -> cut.encodeWord((int)0x0FA5, true) );
		assertTrue(illegalAccessthrown.getMessage().equals("Sample buffer size not set!"));
		
		// setup
		cut.setBufferSize(SB_SIZE);
		
		// functionality
		cut.encodeWord((int)0x0FA5, true);
		
		ByteBuffer result = cut.getSampleBuffer();
		LOGGER.info("testEncodeByte(): result = {}", result);
		
		// verify buffer NOT empty
		assertTrue(result.capacity() == SB_SIZE);
		assertTrue(result.position() == 0);
		assertTrue(result.limit() == expectedLimit);
		
		Step[] steps = sequence.getSteps();
		
		LOGGER.info("sequence: {}, ref: {}", sequence.getSequence(), ref.length);
		assertTrue(sequence.getSequence() == ref.length);
		
		// compare the recorded sequence against the predefined reference.
		for(int n = 0; n < ref.length; n++) {
			LOGGER.info("n: {}, step: {}, ref: {}", n, steps[n], ref[n]);
			assertTrue(steps[n].verify(ref[n]));
		}
		
	} // testEncodeWord()
	
	
	/**
	 * Test method for {@link extension.encoder.Encoder#toString()}.
	 * 
	 * Check correct functionality of toString()
	 * 
	 */
	@Test
	final void testToString() {
		LOGGER.info("testToString()");

		BitEncoder bitEncoderMock = mock(BitEncoder.class);
		Encoder cut = new Encoder(bitEncoderMock, ByteOrder.LITTLE_ENDIAN, BitOrder.LSB_MSB);
		
		LOGGER.info("cut: {}", cut.toString());
		
		assertTrue(cut.toString() != null);

	} // testToString
	
	
} // ssalc
