/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : BackgroundTaskProtokolTest.java
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

package extension.protocol;

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

import extension.protocol.BackgroundTaskProtokol;
import extension.sound.FskAudioFormat;

/**
 * Responsibilities:<br>
 * Unit test of the concrete  methods of class BackgroundTaskProtokol.
 * 
 * <p>
 * Collaborators:<br>
 * BackgroundTaskProtokol
 * 
 * <p>
 * Description:<br>
 * To run the tests, BackgroundTaskProtokolImpl implements
 * the abstract methods required for testing.
 * 
 * <p>
 * @author Stefan
 *
 */

class BackgroundTaskProtokolTest {

	private static Logger LOGGER = null;
	
	private static final int SQR_25 = (int) Math.sqrt(25);
	
	
	/*
	 * ===========================================================================
	 */
	
	class BackgroundTaskProtokolImpl extends BackgroundTaskProtokol {
		
		public BackgroundTaskProtokolImpl() { isRunning = false; }
		
		@Override public ByteBuffer compile(ByteBuffer aDataBuffer) { 
			isRunning = true;
			LOGGER.info("isRunning = {}", isRunning);
			soundsampleBuffer = ByteBuffer.allocate(SQR_25); 
			return soundsampleBuffer;
		}
		
		public ByteBuffer getDataBuffer() { return dataBuffer; }
		public boolean isRunning() { return isRunning; }
		
		@Override protected void progress(int aStep) { /* Not to be tested here! */ }
		@Override public FskAudioFormat getAudioFormat() { return null; }
		@Override protected void setFullProgress(int aBufferSize) { }
		
	} // ssalc

	/*
	 * ===========================================================================
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
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Verifies the correct functionality of the execute() method.
	 * 
	 * Test method for {@link extension.protocol.BackgroundTaskProtokol#executeInBackground()}.
	 */
	@Test
	final void testExecute() {
		LOGGER.info("testExecute()");
		
		IllegalAccessError accessError;
		
		ByteBuffer buffer = ByteBuffer.allocate(42);
		
		BackgroundTaskProtokolImpl cut = new BackgroundTaskProtokolImpl();
		BackgroundTaskProtokolImpl cutSpy = spy(cut);
		
		
		// error handling
		accessError = assertThrows(IllegalAccessError.class, () -> cut.runBackgroundTask() );
		assertTrue(accessError.getMessage().equals("No data buffer set!"));
				

		// correct invocation
		cutSpy.setDataBuffer(buffer);

		assertDoesNotThrow(() -> cutSpy.runBackgroundTask() );
		
		verify(cutSpy, times(1)).compile(buffer);

	} // testExecute()


	/**
	 * Verifies the correct behavior of setDataBuffer(...)
	 * 
	 * Test method for {@link extension.protocol.BackgroundTaskProtokol#setDataBuffer(java.nio.ByteBuffer)}.
	 */
	@Test
	final void testSetDataBuffer() {
		LOGGER.info("testSetDataBuffer()");
		
		IllegalArgumentException thrown;		

		ByteBuffer buffer = ByteBuffer.allocate(42);

		BackgroundTaskProtokolImpl cut = new BackgroundTaskProtokolImpl();
		
		
		// error handling
		thrown = assertThrows(IllegalArgumentException.class, () -> cut.setDataBuffer(null) );
		assertTrue(thrown.getMessage().equals("aDataBuffer can't be null!"));
		
		
		// function test
		assertDoesNotThrow(() -> cut.setDataBuffer(buffer) );
		
		ByteBuffer result = cut.getDataBuffer();
		
		assertTrue(result.equals(buffer));
		
	} // testSetDataBuffer()


	/**
	 * Verifies the correct behavior of getSoundSampleBuffer(...)
	 * 
	 * Test method for {@link extension.protocol.BackgroundTaskProtokol#getSoundSampleBuffer()}.
	 */
	@Test
	final void testGetSoundSampleBuffer() {
		LOGGER.info("testGetSoundSampleBuffer()");
		
		IllegalAccessError accessError;
		
		ByteBuffer buffer = ByteBuffer.allocate(42);
		ByteBuffer result = null;
		
		BackgroundTaskProtokolImpl cut1 = new BackgroundTaskProtokolImpl();
		
		
		// error handling
		accessError = assertThrows(IllegalAccessError.class, () -> cut1.getSoundSampleBuffer() );
		assertTrue(accessError.getMessage().equals("Nothing has been compiled so far."));
		
		
		// path = compile(...)
		cut1.setDataBuffer(buffer);
		cut1.compile(buffer);
		
		try { result = cut1.getSoundSampleBuffer(); } 
		catch (IllegalAccessException e) { 
			fail("Unexpected exception caught"); e.printStackTrace(); }
		
		assertTrue(result != null);
		assertTrue(result.capacity() == SQR_25);
		
		
		// path = execute()
		BackgroundTaskProtokolImpl cut2 = new BackgroundTaskProtokolImpl();
		cut2.setDataBuffer(buffer);
		cut2.compile(buffer);

		try { result = cut2.getSoundSampleBuffer(); } 
		catch (IllegalAccessException e) { 
			fail("Unexpected exception caught"); e.printStackTrace(); }
		
		assertTrue(result != null);
		assertTrue(result.capacity() == SQR_25);
	
	} // testGetSoundSampleBuffer()

	
	/**
	 * Verifies the correct behavior of setEndAddress()
	 * 
	 * Test method for {@link extension.protocol.BackgroundTaskProtokol#setEndAddress()}.
	 */
	@Test
	void testSetEndAddress() {
		LOGGER.info("testSetEndAddress()");
		
		BackgroundTaskProtokolImpl cut = new BackgroundTaskProtokolImpl();
		
		IllegalAccessError thrown = assertThrows(IllegalAccessError.class, () -> cut.setEndAddress(0));
		assertEquals("Unsupported method", thrown.getMessage());
	
	} // testSetEndAddress()
	
	
	/**
	 * Verifies the correct behavior of setSize()
	 * 
	 * Test method for {@link extension.protocol.BackgroundTaskProtokol#setSize()}.
	 */
	@Test
	void testSetSize() {
		LOGGER.info("testSetSize()");
		
		BackgroundTaskProtokolImpl cut = new BackgroundTaskProtokolImpl();
		
		IllegalAccessError thrown = assertThrows(IllegalAccessError.class, () -> cut.setSize(0));
		assertEquals("Unsupported method", thrown.getMessage());
	
	} // testSetSize()
	
	
	/**
	 * Verifies the correct behavior of setStartAddress()
	 * 
	 * Test method for {@link extension.protocol.BackgroundTaskProtokol#setStartAddress()}.
	 */
	@Test
	void testSetStartAddress() {
		LOGGER.info("testSetStartAddress()");
		
		BackgroundTaskProtokolImpl cut = new BackgroundTaskProtokolImpl();
		
		IllegalAccessError thrown = assertThrows(IllegalAccessError.class, () -> cut.setStartAddress(0));
		assertEquals("Unsupported method", thrown.getMessage());
	
	} // testSetStartAddress()
	
	
	/**
	 * Verifies the correct behavior of stop()
	 * 
	 * Test method for {@link extension.protocol.BackgroundTaskProtokol#stop()}.
	 */
	@Test
	void testStop() {
		LOGGER.info("testStop()");
		
		ByteBuffer buffer = ByteBuffer.allocate(42);
		boolean isRunning;
		
		BackgroundTaskProtokolImpl cut = new BackgroundTaskProtokolImpl();
		cut.setDataBuffer(buffer);
		
		
		// is idle?
		isRunning = cut.isRunning();
		assertTrue(!isRunning);
		
		
		// is running?
		cut.compile(buffer);
		isRunning = cut.isRunning();
		assertTrue(isRunning);
		
		
		// is stopped
		cut.stop();
		isRunning = cut.isRunning();
		assertTrue(!isRunning);
		
	} // testStop()
	

	/**
	 * Verifies the correct behavior of toString()
	 * 
	 * Test method for {@link extension.protocol.BackgroundTaskProtokol#toString()}.
	 */
	@Test
	void testToString() {
		LOGGER.info("testToString()");
		
		BackgroundTaskProtokolImpl cut = new BackgroundTaskProtokolImpl();
		LOGGER.info("toString(): {}", cut.toString());
		assertEquals(
				"BackgroundTaskProtokol [checksumCalculator=null, encoder=null, " 
			      + "dataBuffer=null, soundsampleBuffer=null, soundSampleBufferSize=0, " 
			      + "isRunning=false, currentProgress=0, " 
			      + "BackgroundTask [backgroundExecutor=null, isRunning=false]]",
			
			cut.toString())
			;
	
	} // testToString()
	
	
} // ssalc
