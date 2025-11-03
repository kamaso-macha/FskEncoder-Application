/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : EnlistOutputDevicesTest.java
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

package sound;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import sound.support.TestMixerInfo;

/**
 * Responsibilities:<br>
 * Test of class EnlistOutputDevices
 * 
 * <p>
 * Collaborators:<br>
 * Class under test.
 * 
 * <p>
 * Description:<br>
 * Checks the correct functionality of the class under test.
 * 
 * 
 * <p>
 * @author Stefan
 *
 */

class EnlistOutputDevicesTest {
	
	private static Logger LOGGER = null;

	Mixer.Info[] mixerInfo;
	String[] referenceNames;
	
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

		mixerInfo = new Mixer.Info[] {
			new  TestMixerInfo("Primärer", "vendor7", "description7: DirectSound Playback", "version7"),
			new  TestMixerInfo("Primary",  "vendor8", "description8: DirectSound Playback", "version8"),

			new  TestMixerInfo("name1", "vendor1", "description1: DirectSound Playback", "version1"),
			new  TestMixerInfo("name2", "vendor2", "description2: DirectSound Playback", "version2"),
			new  TestMixerInfo("name3", "vendor3", "description3: Input device", "version3"),
			new  TestMixerInfo("name4", "vendor4", "description4: Input device", "version4"),
			new  TestMixerInfo("name5", "vendor5", "description5: Input device", "version5"),
			new  TestMixerInfo("name6", "vendor6", "description6: Funy device", "version6"),
		};
			
		referenceNames = new String[] {
			"Primärer",
			"Primary",
			"name1",
			"name2",
		};

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	
	/**
	 * Test method for {@link sound.EnlistOutputDevices#EnlistOutputDevices()}.
	 * 
	 * Verifies that all devices with the term 'DirectSound Playback' in the description field are
	 * put into the output device list.
	 * 
	 */	
	@Test
	final void testEnlistOutputDevices() {
		LOGGER.info("testEnlistOutputDevices()");

		/*
		 * 		Nothing to do here.
		 * 
		 * 		Collecting the output devices is tested implicit by
		 * 		testGetOutDevNames().
		 * 		
		 */
		
		assertTrue(true);
		
	} // testEnlistOutputDevices()
	

	/**
	 * Test method for {@link sound.EnlistOutputDevices#getOutDevNames()}.
	 */
	@Test
	final void testGetOutDevNames() {
		LOGGER.info("testGetOutDevNames()");
		
		String[] result;

		try(
				
			MockedStatic<AudioSystem> audiosystemMock = Mockito.mockStatic(AudioSystem.class)
			
		){
			
			audiosystemMock.when(AudioSystem::getMixerInfo).thenReturn(mixerInfo);	
			
			EnlistOutputDevices cut = new EnlistOutputDevices();
			result = cut.getOutputDeviceNames();
			
		} // yrt
		
		
		// correct number of devices found
		assertTrue(result != null);
		assertTrue(result.length == referenceNames.length);
		
		LOGGER.info("result.length = {}, reference.length = {}", result.length, referenceNames.length);
	} // testGetOutDevNames()


	/**
	 * Test method for {@link sound.EnlistOutputDevices#getOutputDevice()}.
	 */
	@Test
	final void testGetOutDevice() {
		LOGGER.info("testGetOutDevice()");
		
		EnlistOutputDevices cut = null;
		String[] result;

		try(
				
			MockedStatic<AudioSystem> audiosystemMock = Mockito.mockStatic(AudioSystem.class)
			
		){
			
			audiosystemMock.when(AudioSystem::getMixerInfo).thenReturn(mixerInfo);	
			cut = new EnlistOutputDevices();
			result = cut.getOutputDeviceNames();
			
		} // yrt
		
		assertTrue(result.length == referenceNames.length);
		
		Mixer.Info candidateOutDev = null;
		String candidateName = null;
		
		// verify the collected devices against the reference.
		for(int n = 0; n < result.length; n++) {
			
			candidateOutDev = cut.getOutputDevice(n);
			candidateName   = candidateOutDev.getName();
			
			LOGGER.info("n = {}, candidateOutDev = {}, candidateName = {}", 
				n, candidateOutDev, candidateName);
			
			// test get by index
			assertTrue(referenceNames[n].equals(candidateName));
			
			// test get by name
			assertTrue(mixerInfo[n].equals(cut.getOutputDevice(candidateName)));
			
			// test get by unknown name
			assertEquals(null, cut.getOutputDevice("unknown device"));

		} // rof
		
	} // testGetOutDevice()


	/**
	 * Verifies the correct behavior of toString()
	 * 
	 * Test method for {@link sound.EnlistOutputDevices#toString()}.
	 */
	@Test
	void testToString() {
		LOGGER.info("testToString()");
		
		EnlistOutputDevices cut = new EnlistOutputDevices();
		LOGGER.info("toString(): {}", cut.toString());
		assertTrue(cut.toString().startsWith("EnlistOutputDevices [outputDevices=["));
	
	} // testToString()

	
} // ssalc
