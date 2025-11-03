/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : FskAudioFormatTest.java
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

package extension.sound;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import extension.sound.FskAudioFormat;

/**
 * Responsibilities:<br>
 * Satisfie the code coverage.
 * 
 * <p>
 * Collaborators:<br>
 * None
 * 
 * <p>
 * Description:<br>
 * This class defines some constants for the FSK encoding.
 * No real testing is necessary nor useful.
 * 
 * <p>
 * @author Stefan
 *
 */

class FskAudioFormatTest {

	private static Logger LOGGER = null;

	
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
	 * Test method for {@link extension.sound.FskAudioFormat#FskAudioFormat(int)}.
	 */
	@Test
	final void testFskAudioFormat() {
		LOGGER.info("()");
		
		/*
		 * 
		 * There is not really a need for testing this behavior.
		 *
		 * It' s only to satisfy the code coverage ...
		 * 
		 */
		
		FskAudioFormat cut = new FskAudioFormat(42);
		assertTrue(cut != null);
		
	} // ()

	/**
	 * Verifies the correct behavior of toString()
	 * 
	 * Test method for {@link #toString()}.
	 */
	@Test
	void testToString() {
		LOGGER.info("testToString()");
		
		FskAudioFormat cut = new FskAudioFormat(42);
		LOGGER.info("toString(): {}", cut.toString());
		assertEquals(
			"FskAudioFormat [encoding=PCM_SIGNED, sampleRate=42.0, sampleSizeInBits=8, channels=1, " 
			+ "frameSize=1, frameRate=42.0, bigEndian=true]",
			cut.toString())
			;
	
	} // testToString()
	
	
} // ssalc
