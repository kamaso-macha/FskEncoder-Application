/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : TargetSystemExtensionDaoTest.java
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


package extension.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import extension.control.TargetSystemExtensionControl;
import extension.model.TargetSystemExtensionDao;
import extension.protocol.Protocol;
import extension.view.gui.ExtensionGui;

/**
 * Responsibilities:<br>
 * 
 * 
 * <p>
 * Collaborators:<br>
 * 
 * 
 * <p>
 * Description:<br>
 * 
 * 
 * <p>
 * @author Stefan
 *
 */

// DOC
// Created at 2024-06-07 18:48:43

class TargetSystemExtensionDaoTest {

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
	 * Test method for {@link extension.model.TargetSystemExtensionDao#TargetSystemExtensionDao(extension.protocol.Protocol, extension.view.gui.ExtensionGui, extension.control.TargetSystemExtensionControl)}.
	 */
	@Test
	final void testTargetSystemExtensionDao() {
		LOGGER.info("testInputReaderExtensionDao()");

		Protocol protocolMock = mock(Protocol.class);
		ExtensionGui extensionGuiMock = mock(ExtensionGui.class);
		TargetSystemExtensionControl targetSystemExtensionControlMock = mock(TargetSystemExtensionControl.class);

		
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, 
			() -> new TargetSystemExtensionDao(null, null, null)
		);
		assertEquals("aGui can't be null", thrown.getMessage());
		
		
		thrown = assertThrows(IllegalArgumentException.class, 
				() -> new TargetSystemExtensionDao(null, extensionGuiMock, null)
		);
		assertEquals("aProtocol can't be null", thrown.getMessage());

		
		thrown = assertThrows(IllegalArgumentException.class, 
				() -> new TargetSystemExtensionDao(protocolMock, extensionGuiMock, null)
		);
		assertEquals("aControl can't be null", thrown.getMessage());
			

		assertDoesNotThrow(() -> new TargetSystemExtensionDao(
			protocolMock,
			extensionGuiMock,
			targetSystemExtensionControlMock
		));
		
	} // testInputReaderExtensionDao()


} // ssalc
