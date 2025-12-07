/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : SoundPlayerTest.java
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

package sound;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.ByteBuffer;
import java.util.ServiceConfigurationError;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.MockedConstruction;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import extension.execution.BackgroundExecutor;


/**
 * Responsibilities:<br>
 * Assure the functionality of class SoundPlayer
 * 
 * <p>
 * Collaborators:<br>
 * Class under test
 * 
 * <p>
 * Description:<br>
 * Verifies the correct functionality of all methods of SoundPlayer.
 * <br>
 * NOTE: Mocking is used if applicable. 
 * 
 * <p>
 * @author Stefan
 *
 */

class SoundPlayerTest {

	private static Logger LOGGER = null;
	
	private SourceDataLine sdlMock = mock(SourceDataLine.class);
	private AudioFormat afMock = mock(AudioFormat.class);

	
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
	 * Test method for {@link sound.SoundPlayer#SoundPlayer(javax.sound.sampled.SourceDataLine, javax.sound.sampled.AudioFormat)}.
	 * 
	 * Check the correct error handling of the constructor. 
	 */
	@Test
	final void testSoundPlayer() {
		LOGGER.info("testSoundPlayer()");

		IllegalArgumentException thrown;
		
		
		// error handling
		thrown = assertThrows(IllegalArgumentException.class, () -> new SoundPlayer(null, afMock));
		assertTrue(thrown.getMessage().equals("aLine can't be null!"));
		
		thrown = assertThrows(IllegalArgumentException.class, () -> new SoundPlayer(sdlMock, null));
		assertTrue(thrown.getMessage().equals("aAudioFormat can't be null!"));
		
		assertDoesNotThrow(() -> new SoundPlayer(sdlMock, afMock));
		
	} // testSoundPlayer()
	

	/**
	 * Test method for {@link sound.SoundPlayer#play(ByteBuffer])}.
	 * 
	 * Verifies the correct playback of the given sound samples.
	 */
	@Test
	final void testPlay() {
		LOGGER.info("testPlay()");
		
		final int   NBR_OF_SAMPLES	= 8000;
		final int   FULL_CHUNK_SIZE	= 1500;
		
		final int NBR_FULL_CHUNKS		= (int)NBR_OF_SAMPLES / FULL_CHUNK_SIZE;
		final int REMAINING_CHUNKSIZE	= NBR_OF_SAMPLES - ( FULL_CHUNK_SIZE * NBR_FULL_CHUNKS);
		
		InOrder playSequence = inOrder(sdlMock);

		doReturn(FULL_CHUNK_SIZE).when(sdlMock).getBufferSize();
		doReturn(afMock).when(sdlMock).getFormat();		
		
		ByteBuffer soundSampleBuffer = ByteBuffer.allocate(NBR_OF_SAMPLES);
		
		SoundPlayer cut = new SoundPlayer(sdlMock, afMock);
		
		try {
			
			LOGGER.trace("soundSampleBuffer: " + soundSampleBuffer);
			cut.play(soundSampleBuffer);
			
			playSequence.verify(sdlMock, times(1)).start();

			playSequence.verify(sdlMock, times(NBR_FULL_CHUNKS)).write(any(), eq(0), eq(FULL_CHUNK_SIZE));
			playSequence.verify(sdlMock, times(1)).write(any(), eq(0), eq(REMAINING_CHUNKSIZE));
			
			playSequence.verify(sdlMock).drain();
			playSequence.verify(sdlMock).stop();
			playSequence.verify(sdlMock).close();

		} catch (LineUnavailableException e) {
			e.printStackTrace();
			fail("Caught unexpected exception " + e.getClass());
		}
		
		
		// test opening of source data line

		InOrder isNotOpenSequence = inOrder(sdlMock);
		when(sdlMock.isOpen()).thenReturn(false);
		
		cut = new SoundPlayer(sdlMock, afMock);
		
		try {
			
			cut.play(ByteBuffer.allocate(1));
			
			isNotOpenSequence.verify(sdlMock, times(1)).isOpen();
			isNotOpenSequence.verify(sdlMock, times(1)).open(afMock);
			isNotOpenSequence.verify(sdlMock, times(1)).start();
			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			fail("Caught unexpected exception " + e.getClass());
		}

		
		// test open source data line

		reset(sdlMock);
		doReturn(FULL_CHUNK_SIZE).when(sdlMock).getBufferSize();
		doReturn(afMock).when(sdlMock).getFormat();		
		
		InOrder isOpenSequence = inOrder(sdlMock);
		when(sdlMock.isOpen()).thenReturn(true);
		
		cut = new SoundPlayer(sdlMock, afMock);
		
		try {
			
			cut.play(ByteBuffer.allocate(1));
			
			isOpenSequence.verify(sdlMock, times(1)).isOpen();
			isOpenSequence.verify(sdlMock, never()).open(afMock);
			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			fail("Caught unexpected exception " + e.getClass());
		}

		
		// verify exception for closed source data line

		reset(sdlMock);
		doReturn(FULL_CHUNK_SIZE).when(sdlMock).getBufferSize();
		doReturn(afMock).when(sdlMock).getFormat();		
		
		when(sdlMock.isOpen()).thenReturn(false);
		
		try {
			doThrow(LineUnavailableException.class).when(sdlMock).open(afMock);
			
			assertThrows(LineUnavailableException.class, () -> { 
				SoundPlayer cut3 = new SoundPlayer(sdlMock, afMock);
				cut3.play(ByteBuffer.allocate(1)); 
			});
			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			fail("Caught unexpected exception " + e.getClass());
		}
		
	} // testPlay()
	
	
	
	/**
	 * Test method for {@link sound.SoundPlayer#runBackgroundTask()}.
	 * 
	 * Verifies the correct behavior of the tested method. 
	 */
	@Test
	final void testRunBackgroundTask() {
		LOGGER.info("testRunBackgroundTask()");
		
		final int   FULL_CHUNK_SIZE	= 1500;
		
		IllegalAccessError thrown;
		ByteBuffer soundSampleBuffer = ByteBuffer.allocate(1);
		
		doReturn(FULL_CHUNK_SIZE).when(sdlMock).getBufferSize();
		doReturn(afMock).when(sdlMock).getFormat();	
		
		SoundPlayer cut = spy(new SoundPlayer(sdlMock, afMock));
		
		thrown = assertThrows(IllegalAccessError.class, () -> cut.runBackgroundTask());
		assertEquals("No sound sample buffer set!", thrown.getMessage());
		
		cut.setSoundBuffer(soundSampleBuffer);
		assertDoesNotThrow(() -> cut.runBackgroundTask());
		
		try {
			
			verify(cut, times(1)).play(soundSampleBuffer);
			
		} catch (LineUnavailableException e) {
			
			LOGGER.error("Unexpected exception caught: " + e);
			fail("Unexpected exception caught: " + e);
			
		} // yrt
		
		
		
		try {
			
			doThrow(LineUnavailableException.class).when(cut).play(soundSampleBuffer);
			
			Void result = cut.runBackgroundTask();
			assertEquals(null, result);

			fail("Exception expected but not caught");

		} catch (ServiceConfigurationError e) {
			
			assertTrue(e instanceof ServiceConfigurationError);
			assertEquals("No output line available", e.getMessage());
			
			LOGGER.info("Expected exception caught: " + e);
			
		} // yrt
		catch (LineUnavailableException e) {

			LOGGER.info("Unexpected exception caught: " + e);
			fail("Unexpected exception caught");
			
		} // yrt
		
	} // testRunBackgroundTask()
	
	
	/**
	 * Test method for {@link sound.SoundPlayer#executeInBackground()}.
	 * 
	 * Verifies that the progress indicator is served during playback.
	 */
	@SuppressWarnings({ "rawtypes" })
	@Test
	final void testProgressIndicator() {
		LOGGER.info("testProgressIndicator()");

		final int   NBR_OF_SAMPLES	= 8000;
		final int   FULL_CHUNK_SIZE	= 1500;
		
		doReturn(FULL_CHUNK_SIZE).when(sdlMock).getBufferSize();
		doReturn(afMock).when(sdlMock).getFormat();		
		
		ByteBuffer soundSampleBuffer = ByteBuffer.allocate(NBR_OF_SAMPLES);

		try(
				
				MockedConstruction<BackgroundExecutor> bgeMockConstructed = mockConstruction(BackgroundExecutor.class);
					
			) // yrt() 
			{
		
			SoundPlayer cut = spy(new SoundPlayer(sdlMock, afMock));
			cut.setSoundBuffer(soundSampleBuffer);
			
			// execute created the BackgroundExecutor
			cut.execute();

			assertEquals(1,  bgeMockConstructed.constructed().size());
			
			BackgroundExecutor bgeMock = bgeMockConstructed.constructed().get(0);
			LOGGER.info("bgeMock = {}", bgeMock);
			
			InOrder progressSteps = inOrder(bgeMock);

			progressSteps.verify(bgeMock).execute();

			// We MUST trigger cut.runBackgroundTask() to start execution!
			cut.runBackgroundTask();
			
			progressSteps.verify(bgeMock, times(6)).stepOn(anyInt());
		
		} // yrt
			
	} // testProgressIndicator()
	

	/**
	 * Test method for {@link sound.SoundPlayer#setOutputGain()}.
	 * 
	 * Verifies the correct behavior of the tested method. 
	 */
	@Test
	final void testSetOutputGain() {
		LOGGER.info("testSetOutputGain()");
		
		FloatControl fcMock = mock(FloatControl.class);

		doReturn(fcMock).when(sdlMock).getControl(FloatControl.Type.MASTER_GAIN);
		
		IllegalArgumentException thrown;
		
		SoundPlayer cut = new SoundPlayer(sdlMock, afMock);
		
		
		//
		// boundaries
		//
		
		thrown = assertThrows(IllegalArgumentException.class, () -> {
			SoundPlayer cut2 = new SoundPlayer(sdlMock, afMock);
			cut2.setOutputGain(-1);
		});
		
		assertEquals("aValue can't be less than 0.", thrown.getMessage());
		
		thrown = assertThrows(IllegalArgumentException.class, () -> {
			SoundPlayer cut3 = new SoundPlayer(sdlMock, afMock);
			cut3.setOutputGain(101);
		});
		
		
		//
		// minimum level
		//

		assertDoesNotThrow(() -> {
			SoundPlayer cut4 = new SoundPlayer(sdlMock, afMock);
			cut4.setOutputGain(0);
		});

		
		reset(fcMock);
		doReturn((float) -80.0).when(fcMock).getMinimum();
		doReturn((float)   6.0).when(fcMock).getMaximum();

		try {
			cut.setOutputGain(0);
		} catch (LineUnavailableException e) {

			LOGGER.info("Unexpected exception caught: " + e);
			fail("Unexpected exception caught");
			
		}
		verify(fcMock, times(1)).setValue((float) -80.0);

		
		//
		// maximum level
		//
		
		assertDoesNotThrow(() -> {
			SoundPlayer cut4 = new SoundPlayer(sdlMock, afMock);
			cut4.setOutputGain(100);
		});

		reset(fcMock);
		doReturn((float) -80.0).when(fcMock).getMinimum();
		doReturn((float)   6.0).when(fcMock).getMaximum();

		try {
			cut.setOutputGain(100);
		} catch (LineUnavailableException e) {

			LOGGER.info("Unexpected exception caught: " + e);
			fail("Unexpected exception caught");
			
		}
		verify(fcMock, times(1)).setValue((float)   6.0);
		
		
		//
		// test open source data line
		//

		reset(sdlMock);
		doReturn(fcMock).when(sdlMock).getControl(FloatControl.Type.MASTER_GAIN);
		
		InOrder isOpenSequence = inOrder(sdlMock);
		when(sdlMock.isOpen()).thenReturn(true);
		
		cut = new SoundPlayer(sdlMock, afMock);
		
		try {
			
			cut.setOutputGain(50);
			
			isOpenSequence.verify(sdlMock, times(1)).isOpen();
			isOpenSequence.verify(sdlMock, never()).open(afMock);
			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			fail("Caught unexpected exception " + e.getClass());
		}
		
		
		//
		// test LineUnavailableException
		//

		reset(sdlMock);		
		reset(fcMock);
		
		try {
			doThrow(LineUnavailableException.class).when(sdlMock).open(afMock);
			
			assertThrows(LineUnavailableException.class, () -> { 
				SoundPlayer cut3 = new SoundPlayer(sdlMock, afMock);
				cut3.setOutputGain(50);
			});
			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			fail("Caught unexpected exception " + e.getClass());
		}

		
		//
		// test IllegalArgumentException
		//

		reset(sdlMock);		
		reset(fcMock);
		doReturn(fcMock).when(sdlMock).getControl(FloatControl.Type.MASTER_GAIN);

		doThrow(IllegalArgumentException.class).when(fcMock).setValue(anyFloat());
		
		assertThrows(IllegalArgumentException.class, () -> { 
			SoundPlayer cut3 = new SoundPlayer(sdlMock, afMock);
			cut3.setOutputGain(50);
		});


	} // testSetOutputGain()
	
	
	/**
	 * Test method for {@link sound.SoundPlayer#setSoundBuffer(ByteBuffer)}.
	 */
	@Test 
	final void testSetSoundBuffer() {
		LOGGER.info("testSetSoundBuffer()");
		
		final int   NBR_OF_SAMPLES	= 8000;

		IllegalArgumentException thrown;
		ByteBuffer soundSampleBuffer = ByteBuffer.allocate(NBR_OF_SAMPLES);

		SoundPlayer cut = new SoundPlayer(sdlMock, afMock);
		
		thrown = assertThrows(IllegalArgumentException.class, () -> cut.setSoundBuffer(null) );
		assertTrue(thrown.getMessage().equals("aSoundbufffer can't be null!"));
		
		cut.setSoundBuffer(soundSampleBuffer);
		
		
	} // testSetSoundBuffer()
	
	
	/**
	 * Test method for {@link sound.SoundPlayer#stop()}.
	 * 
	 * Makes sure that a running playback can be stopped.
	 */
	
	private int syncFlag;

	@Test
	final void testStop() {
		LOGGER.info("testStop()");
		
		// see http://www.awaitility.org/ for better solution!
		
		SoundPlayer cut = new SoundPlayer(sdlMock, afMock);
		
		ByteBuffer soundSampleBuffer = ByteBuffer.allocate(100_000);
		
		syncFlag = 2;
		
		Answer<?> answer = new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) {
				
				LOGGER.info("line.write( ... ), syncFlag = " + syncFlag);

				syncFlag--;
				
			return null;
					          
			} // answer(...)
			
		};
		
		Runnable r = new Runnable() {
			
			@Override
			public void run() {

				try {
					cut.play(soundSampleBuffer);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			} // run()
			
		}; // Runnable()
		
		doReturn(1500).when(sdlMock).getBufferSize();
		doReturn(afMock).when(sdlMock).getFormat();
		
		doReturn(9088.0f).when(afMock).getSampleRate();
		
		doAnswer(answer).when(sdlMock).write(any(), eq(0), eq(1500));
		
		LOGGER.info("start()");
		new Thread(r).start();
		LOGGER.info("started()");

		// give play a bit time to run
		while(syncFlag > 0) {
			LOGGER.info("while(syncFlag): " + syncFlag);
		}

		
		// test stop()
		LOGGER.info("stop()");
		cut.stop();
		verify(sdlMock, atMost(7)).write(any(), eq(0), eq(1500));
		
	} // testStop()
	

	/**
	 * Verifies the correct behavior of toString()
	 * 
	 * Test method for {@link sound.SoundPlayer#toString()}.
	 */
	@Test
	void testToString() {
		LOGGER.info("testToString()");
		
		SoundPlayer cut = new SoundPlayer(sdlMock, afMock);
		LOGGER.info("toString(): {}", cut.toString());
		
		assertTrue(cut.toString().startsWith("SoundPlayer [sourceDataLine=Mock for SourceDataLine, hashCode: "));
		assertTrue(cut.toString().contains(", audioformat=Mock for AudioFormat, hashCode: "));
		assertTrue(cut.toString().endsWith(", soundsampleBuffer=null, isRunning=false]"));
	
	} // testToString()
	
	
} // ssalc
