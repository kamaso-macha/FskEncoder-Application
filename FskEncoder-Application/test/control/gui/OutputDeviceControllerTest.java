/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : OutputDeviceControllerTest.java
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

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import application.ApplicationResources;
import control.WorkflowEngine;
import extension.sound.FskAudioFormat;
import model.FskUploaderModel;
import model.OutputDeviceControllerModel;
import sound.SoundPlayer;
import view.gui.OutputDeviceGui;

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

@SuppressWarnings("unused")
// DOC
// Created at 2025-02-15 16:27:29

class OutputDeviceControllerTest {

	private static Logger LOGGER = null;
	
	OutputDeviceController cut;
	
	WorkflowEngine wfeMock;
	OutputDeviceControllerModel odcMock;
	

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
		odcMock = mock(OutputDeviceControllerModel.class);
		
	} // setUp()
	

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}
	

	/**
	 * Test method for {@link control.gui.OutputDeviceController#OutputDeviceController(control.WorkflowEngine, model.OutputDeviceControllerModel)}.
	 */
//	@Test
	final void testOutputDeviceController() {
		LOGGER.info("testOutputDeviceController()");

		IllegalArgumentException thrown;
		
		thrown = assertThrows(IllegalArgumentException.class, () -> {
			new OutputDeviceController(null, null);
		});
		
		LOGGER.debug(thrown.toString());
		assertTrue(thrown.getMessage().equals("aWorkflowEngine can't be null!"));

		thrown = assertThrows(IllegalArgumentException.class, () -> {
			new OutputDeviceController(wfeMock, null);
		});
		
		LOGGER.debug(thrown.toString());
		assertTrue(thrown.getMessage().equals("aModel can't be null!"));


		assertDoesNotThrow(() -> new OutputDeviceController(wfeMock, odcMock) );
		
	} // testOutputDeviceController()
	

	/**
	 * Test method for {@link control.gui.OutputDeviceController#focusGained(java.awt.event.FocusEvent)}.
	 */
//	@Test
	final void testFocusGained() {
		LOGGER.info("testFocusGained()");

		/*
		 * Currently no tests here
		 * 
		 */
		
		assertTrue(true);
		
	} // testFocusGained()
	

	/**
	 * Test method for {@link control.gui.OutputDeviceController#focusLost(java.awt.event.FocusEvent)}.
	 */
//	@Test
	final void testFocusLost() {
		LOGGER.info("testFocusLost()");

		/*
		 * Currently no tests here
		 * 
		 */
		
		assertTrue(true);
		
	} // testFocusLost()
	

	/**
	 * Test method for {@link control.gui.OutputDeviceController#stateChanged(javax.swing.event.ChangeEvent)}.
	 */
//	@Test
	final void testStateChanged() {
		LOGGER.info("testStateChanged()");

		ChangeEvent testEvent = new ChangeEvent(new JSlider());
		
		fail("Not yet implemented"); // TODO
		
	} // testStateChanged()
	

	/**
	 * Test method for {@link control.gui.OutputDeviceController#actionPerformed(java.awt.event.ActionEvent)}.
	 */
//	@Test
	final void testActionPerformed() {
		LOGGER.info("testActionPerformed()");

		fail("Not yet implemented"); // TODO
		
	} // testActionPerformed()
	

	/**
	 * Test method for {@link control.gui.OutputDeviceController#getOutputDeviceName()}.
	 */
//	@Test
	final void testGetOutputDeviceName() {
		LOGGER.info("testGetOutputDeviceName()");

		String odName = "Foo";
		String result = null;

		cut = new OutputDeviceController(wfeMock, odcMock);
		
		// constructor does 2 irrelevant calls to getOutputDeviceName()
		reset(odcMock);
		
		doReturn(odName).when(odcMock).getOutputDeviceName();
		
		result = cut.getOutputDeviceName();
		
		verify(odcMock, times(1)).getOutputDeviceName();
		assertEquals(odName, result);

	} // testGetOutputDeviceName()
	

	/**
	 * Test method for {@link control.gui.OutputDeviceController#getOutputDeviceNames()}.
	 */
//	@Test
	final void testGetOutputDeviceNames() {
		LOGGER.info("testGetOutputDeviceNames()");

		String[] odNames = { "Foo", "Bar", "Boo"};
		String[] result = null;

		cut = new OutputDeviceController(wfeMock, odcMock);
		
		doReturn(odNames).when(wfeMock).getOutputDeviceNames();
		
		result = cut.getOutputDeviceNames();
		
		verify(wfeMock, times(1)).getOutputDeviceNames();
		assertEquals(odNames, result);

	} // testGetOutputDeviceNames()
	

	/**
	 * Test method for {@link control.gui.OutputDeviceController#getCurrentPosition()}.
	 */
//	@Test
	final void testGetCurrentPosition() {
		LOGGER.info("testGetCurrentPosition()");

		Point ref = new Point(100, 100);
		
		cut = new OutputDeviceController(wfeMock, odcMock);
		Point test = cut.getCurrentPosition();
		
		assertEquals(test, ref);
		
	} // testGetCurrentPosition()
	

	/**
	 * Test method for {@link control.gui.OutputDeviceController#handleDefaultOutputDevice()}.
	 */
//	@Test
	final void testHandleDefaultOutputDevice() {
		LOGGER.info("testHandleDefaultOutputDevice()");

		fail("Not yet implemented"); // TODO
		
	} // testHandleDefaultOutputDevice()
	

	/**
	 * Test method for {@link control.gui.OutputDeviceController#handleDlgOutputDeviceCancelEvent()}.
	 */
//	@Test
	final void testHandleDlgOutputDeviceCancelEvent() {
		LOGGER.info("()");

		fail("Not yet implemented"); // TODO
		
	} // ()
	

	/**
	 * Test method for {@link control.gui.OutputDeviceController#handleDlgOutputDeviceOKEvent()}.
	 */
//	@Test
	final void testHandleDlgOutputDeviceOKEvent() {
		LOGGER.info("()");

		fail("Not yet implemented"); // TODO
		
	} // ()
	

	/**
	 * Test method for {@link control.gui.OutputDeviceController#handleComboBoxEvent()}.
	 */
//	@Test
	final void testHandleComboBoxEvent() {
		LOGGER.info("()");

		fail("Not yet implemented"); // TODO
		
	} // ()
	

	/**
	 * Test method for {@link control.gui.OutputDeviceController#setOutputDevice()}.
	 */
//	@Test
	final void testSetOutputDevice() {
		LOGGER.info("()");

		fail("Not yet implemented"); // TODO
		
	} // ()
	

	/**
	 * Test method for {@link control.gui.OutputDeviceController#setDefaults()}.
	 */
	@Test
	final void testSetDefaults() {
		LOGGER.info("testSetDefaults()");

		int outputVolume = Integer.parseInt(ApplicationResources.DEFAULT_OUTPUT_VOLUME);
		
		FskAudioFormat audioformat = new FskAudioFormat(42);
		String outputDeviceName = "Foo";
		
		try (
				
				MockedConstruction<OutputDeviceGui> mcOutputDeviceGui = mockConstruction(OutputDeviceGui.class);
				MockedConstruction<SoundPlayer> mcSoundPlayer = mockConstruction(SoundPlayer.class);
				
			) {
			
			doReturn(outputDeviceName).when(odcMock).getOutputDeviceName();
			
			doReturn(audioformat).when(wfeMock).getAudioFormat();
			doReturn(null).when(wfeMock).getOutputDevice(outputDeviceName);
			
			doReturn(Integer.parseInt(ApplicationResources.DEFAULT_OUTPUT_VOLUME)).when(wfeMock).getOutputVolume();

			
			cut = new OutputDeviceController(wfeMock, odcMock);
			assertEquals(1, mcOutputDeviceGui.constructed().size());
//			assertEquals(1, mcSoundPlayer.constructed().size());
			
			OutputDeviceGui odGuiMock = mcOutputDeviceGui.constructed().get(0);
			SoundPlayer spMock = mcSoundPlayer.constructed().get(0);
			
			reset(odGuiMock);
			cut.setDefaults();
			
			verify(odGuiMock, times(1)).setTxtOutputVolume(outputVolume);
			verify(odGuiMock, times(1)).setSlOutputVolume(outputVolume);
			
			try {
				verify(spMock, times(1)).setOutputGain(outputVolume);
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} // yrt
		
	} // testSetDefaults()
	

	/**
	 * Test method for {@link control.gui.OutputDeviceController#getGui()}.
	 */
//	@Test
	final void testGetGui() {
		LOGGER.info("testGetGui()");

		try (
				
				MockedConstruction<OutputDeviceGui> mcOutputDeviceGui = mockConstruction(OutputDeviceGui.class);
				
			) {
			
			cut = new OutputDeviceController(wfeMock, odcMock);
			assertEquals(1, mcOutputDeviceGui.constructed().size());
			
			OutputDeviceGui odGuiMock = mcOutputDeviceGui.constructed().get(0);
			
			JPanel result = cut.getGui();
			
			assertEquals(odGuiMock, result);
			
		} // yrt
		
	} // testGetGui()
	

	/**
	 * Test method for {@link control.gui.OutputDeviceController#getSoundPlayer()}.
	 */
//	@Test
	final void testGetSoundPlayer() {
		LOGGER.info("()");

		fail("Not yet implemented"); // TODO
		
	} // ()
	

} // class
