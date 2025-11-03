/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : TargetSystemSelectionTest.java
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.Point;
import java.awt.event.ActionEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import control.WorkflowEngine;
import model.TargetSystemSelectionModel;
import view.gui.DlgTargetSystemSelection;

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
// Created at 2025-02-15 12:33:49

class TargetSystemSelectionTest {

	private static Logger LOGGER = null;
	
	
	TargetSystemSelection cut;
	
	WorkflowEngine wfeMock;
	TargetSystemSelectionModel tssmMock;
	
	
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
		tssmMock = mock(TargetSystemSelectionModel.class);
		
		cut = new TargetSystemSelection(wfeMock, tssmMock);
		assertTrue(cut != null, "cut not null");
		
	} // setUp()
	

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}
	

	/**
	 * Test method for {@link control.gui.TargetSystemSelection#TargetSystemSelection(control.WorkflowEngine, model.TargetSystemSelectionModel)}.
	 */
	@Test
	final void testTargetSystemSelection() {
		LOGGER.info("testTargetSystemSelection()");

		IllegalArgumentException thrown;
		
		thrown = assertThrows(IllegalArgumentException.class, () -> {
			new TargetSystemSelection(null, null);
		});
		
		LOGGER.debug(thrown.toString());
		assertTrue(thrown.getMessage().equals("aWorkflowEngine can't be null!"));

		thrown = assertThrows(IllegalArgumentException.class, () -> {
			new TargetSystemSelection(wfeMock, null);
		});
		
		LOGGER.debug(thrown.toString());
		assertTrue(thrown.getMessage().equals("aModel can't be null!"));


		assertDoesNotThrow(() -> new TargetSystemSelection(wfeMock, tssmMock) );

	} // testTargetSystemSelection()
	

	/**
	 * Test method for {@link control.gui.TargetSystemSelection#actionPerformed(java.awt.event.ActionEvent)}.
	 */
	@Test
	final void testActionPerformed() {
		LOGGER.info("testActionPerformed()");

		/*
		 * Nothing to test!
		 * 
		 * Test is implicitly done by action handler tests.
		 * 
		 */
		
		assertTrue(true);
		
	} // testActionPerformed()
	

	/**
	 * Test method for {@link control.gui.TargetSystemSelection#setTargetSystem()}.
	 */
	@Test
	final void testSetTargetSystem() {
		LOGGER.info("testSetTargetSystem()");

		ActionEvent testActionevent = new ActionEvent(this, 1, "Target System", 0);

		try(
				
				MockedConstruction<DlgTargetSystemSelection> mcDlgTargetSystemSelectionMock = mockConstruction(DlgTargetSystemSelection.class);
					
			) {
				
				cut.actionPerformed(testActionevent);
				
				assertEquals(1, mcDlgTargetSystemSelectionMock.constructed().size());
				
				DlgTargetSystemSelection dlgTargetSystemSelectionMock = mcDlgTargetSystemSelectionMock.constructed().get(0);
				
				verify(dlgTargetSystemSelectionMock, times(1)).setVisible(true);
				
			} // yrt
		
	} // testSetTargetSystem()
	

	/**
	 * Test method for {@link control.gui.TargetSystemSelection#handleDlgTargetSystemCancelEvent()}.
	 */
	@Test
	final void testHandleDlgTargetSystemCancelEvent() {
		LOGGER.info("testHandleDlgTargetSystemCancelEvent()");

		ActionEvent testActionevent;

		try(
				
				MockedConstruction<DlgTargetSystemSelection> mcDlgTargetSystemSelectionk = mockConstruction(DlgTargetSystemSelection.class);
					
			) {
				
				// To create a instance of DlgTargetSystemSelection
				testActionevent = new ActionEvent(this, 1, "Target System", 0);
				cut.actionPerformed(testActionevent);
				
				
				// do the test
				testActionevent = new ActionEvent(this, 1, "Cancel", 0);
				cut.actionPerformed(testActionevent);

				
				assertEquals(1, mcDlgTargetSystemSelectionk.constructed().size());
				
				DlgTargetSystemSelection dlgTargetSystemSelectionMock = mcDlgTargetSystemSelectionk.constructed().get(0);
				
				verify(dlgTargetSystemSelectionMock, times(1)).setVisible(false);
				verify(dlgTargetSystemSelectionMock, times(1)).dispose();
				
			} // yrt
		
	} // testHandleDlgTargetSystemCancelEvent()
	

	/**
	 * Test method for {@link control.gui.TargetSystemSelection#handleTargetSystemOKEvent()}.
	 */
	@Test
	final void testHandleTargetSystemOKEvent() {
		LOGGER.info("testHandleTargetSystemOKEvent()");

		ActionEvent testActionevent;
		String name = "Foobar";

		try(
				
				MockedConstruction<DlgTargetSystemSelection> mcDlgTargetSystemSelectionMock = mockConstruction(DlgTargetSystemSelection.class);
					
			) {
				
				// To create a instance of DlgTargetSystemSelection
				testActionevent = new ActionEvent(this, 1, "Target System", 0);
				cut.actionPerformed(testActionevent);
				
				assertEquals(1, mcDlgTargetSystemSelectionMock.constructed().size());
				
				DlgTargetSystemSelection dlgTargetSystemSelectionMock = mcDlgTargetSystemSelectionMock.constructed().get(0);
				assertNotNull(dlgTargetSystemSelectionMock);
				

				// do the test
				testActionevent = new ActionEvent(this, 1, "OK", 0);
				doReturn(name).when(dlgTargetSystemSelectionMock).getSelectedDeviceName();

				cut.actionPerformed(testActionevent);

				
				// read selection and update WFE
				verify(dlgTargetSystemSelectionMock, times(1)).getSelectedDeviceName();
				LOGGER.debug("dialog: {}, selected device: {}", dlgTargetSystemSelectionMock, dlgTargetSystemSelectionMock.getSelectedDeviceName());
				
				verify(wfeMock, times(1)).setTargetSystem(name);
				
				// close dialogue
				verify(dlgTargetSystemSelectionMock, times(1)).setVisible(false);
				verify(dlgTargetSystemSelectionMock, times(1)).dispose();
				
				
			} // yrt
		
	} // testHandleTargetSystemOKEvent()
	

	/**
	 * Test method for {@link control.gui.TargetSystemSelection#handleComboBoxEvent()}.
	 */
	@Test
	final void testHandleComboBoxEvent() {
		LOGGER.info("testHandleComboBoxEvent()");

		ActionEvent testActionevent;

		try(
				
				MockedConstruction<DlgTargetSystemSelection> mcDlgTargetSystemSelectionk = mockConstruction(DlgTargetSystemSelection.class);
					
			) {
				
				// To create a instance of DlgTargetSystemSelection
				testActionevent = new ActionEvent(this, 1, "Target System", 0);
				cut.actionPerformed(testActionevent);
				
				
				// do the test
				testActionevent = new ActionEvent(this, 1, "comboBoxChanged", 0);
				cut.actionPerformed(testActionevent);

				
				assertEquals(1, mcDlgTargetSystemSelectionk.constructed().size());
				
//				DlgTargetSystemSelection dlgTargetSystemSelectionMock = mcDlgTargetSystemSelectionk.constructed().get(0);
				
				/*
				 * Currently nothing to test!
				 * 
				 */
				assertTrue(true);
				
			} // yrt
		
	} // testHandleComboBoxEvent()
	

	/**
	 * Test method for {@link control.gui.TargetSystemSelection#getCurrentPosition()}.
	 */
	@Test
	final void testGetCurrentPosition() {
		LOGGER.info("testGetCurrentPosition()");

		Point ref = new Point(100, 100);
		
		Point test = cut.getCurrentPosition();
		
		assertEquals(test, ref);
		
	} // testGetCurrentPosition()
	

	/**
	 * Test method for {@link control.gui.TargetSystemSelection#getTargetSystemNames()}.
	 */
	@Test
	final void testGetTargetSystemNames() {
		LOGGER.info("testGetTargetSystemNames()");

		String[] tsnNames = { "Foo", "Bar", "Boo"};
		String[] result = null;
		
		doReturn(tsnNames).when(tssmMock).getTargetSystemNames();
		
		result = cut.getTargetSystemNames();
		
		verify(tssmMock, times(1)).getTargetSystemNames();
		assertEquals(tsnNames, result);
		
	} // testGetTargetSystemNames()
	

	/**
	 * Test method for {@link control.gui.TargetSystemSelection#getTargetSystemName()}.
	 */
	@Test
	final void testGetTargetSystemName() {
		LOGGER.info("testGetTargetSystemName()");

		String tsnName = "Foo";
		String result = null;
		
		doReturn(tsnName).when(tssmMock).getTargetSystemName();
		
		result = cut.getTargetSystemName();
		
		verify(tssmMock, times(1)).getTargetSystemName();
		assertEquals(tsnName, result);
		
	} // testGetTargetSystemName()
	

} // class
