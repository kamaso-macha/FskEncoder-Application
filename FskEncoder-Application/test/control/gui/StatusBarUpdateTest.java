/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : StatusBarUpdateTest.java
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


package control.gui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import control.WorkflowEngine;
import view.gui.StatusBarGui;

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
// Created at 2025-02-15 13:58:10

class StatusBarUpdateTest {

	private static Logger LOGGER = null;
	
	
	StatusBarUpdate cut;
	
	WorkflowEngine wfeMock;

	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	    System.setProperty("log4j.configurationFile","./test-cfg/log4j2.xml");
		LOGGER = LogManager.getLogger();
		
	} // setUpBeforeClass()

	
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
		
		wfeMock = mock(WorkflowEngine.class);
		
	} // setUp()
	

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	
	/**
	 * Test method for {@link control.gui.StatusBarUpdate#StatusBarUpdate(control.WorkflowEngine)}.
	 */
	@Test
	final void testStatusBarUpdate() {
		LOGGER.info("testStatusBarUpdate()");

		IllegalArgumentException thrown;
		
		thrown = assertThrows(IllegalArgumentException.class, () -> {
			new StatusBarUpdate(null);
		});
		
		LOGGER.debug(thrown.toString());
		assertTrue(thrown.getMessage().equals("aWorkflowEngine can't be null!"));

		assertDoesNotThrow(() -> new StatusBarUpdate(wfeMock) );
		
	} // testStatusBarUpdate()
	

	/**
	 * Test method for {@link control.gui.StatusBarUpdate#setStatusMessage(java.lang.String)}.
	 */
	@Test
	final void testSetStatusMessage() {
		LOGGER.info("testSetStatusMessage()");

		String message = "Foo goes to a bar in bazz";
		
		try (
				
			MockedConstruction<StatusBarGui> mcStatusBarStatusBarGuiMock = mockConstruction(StatusBarGui.class);
				
			) {
			
				cut = new StatusBarUpdate(wfeMock);
				assertTrue(cut != null, "cut not null");
			
				assertEquals(1, mcStatusBarStatusBarGuiMock.constructed().size());
				
				StatusBarGui statusBarGuiMock = mcStatusBarStatusBarGuiMock.constructed().get(0);
				
				cut.setStatusMessage(message);
				verify(statusBarGuiMock, times(1)).setTxtStatusMessage(message);
				
		} // yrt
			
	} // testSetStatusMessage()
	

	/**
	 * Test method for {@link control.gui.StatusBarUpdate#getGui()}.
	 */
	@Test
	final void testGetGui() {
		LOGGER.info("testGetGui()");

		JPanel result;
		
		try (
				
				MockedConstruction<StatusBarGui> mcStatusBarStatusBarGuiMock = mockConstruction(StatusBarGui.class);
					
				) {
				
					cut = new StatusBarUpdate(wfeMock);
					assertTrue(cut != null, "cut not null");
				
					assertEquals(1, mcStatusBarStatusBarGuiMock.constructed().size());
					
					StatusBarGui statusBarGuiMock = mcStatusBarStatusBarGuiMock.constructed().get(0);
					
					result = cut.getGui();
					
					assertEquals(statusBarGuiMock, result);
					
			} // yrt
				
	} // testGetGui()
	

} // class
