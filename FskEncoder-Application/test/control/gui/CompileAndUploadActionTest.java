/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : CompileAndUploadActionTest.java
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import control.WorkflowEngine;
import extension.control.StatusListener;
import extension.model.MemoryRegion;
import extension.protocol.BackgroundTask;
import extension.protocol.BackgroundTaskProtokol;
import sound.SoundPlayer;
import view.gui.CompileAndUploadGui;

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
// Created at 2025-04-26 09:34:56

class CompileAndUploadActionTest {

	private static Logger LOGGER = null;
	
	CompileAndUploadAction cut;
	
	WorkflowEngine wfeMock;
	BackgroundTaskProtokol protocolMock;
	
	SoundPlayer soundplayerMock;
	
	ActionEvent actionEventMock;
	PropertyChangeEvent propertyChangeEventMock;
	
	PropertyChangeListener propertyChangeListenerMock;
	StatusListener statusListenerMock;
	
	List<MemoryRegion> memoryRegionList;
		MemoryRegion memoryRegionMock0;
		MemoryRegion memoryRegionMock1;
	
	ByteBuffer byteBuffer0 = ByteBuffer.allocate(100);
	ByteBuffer byteBuffer1 = ByteBuffer.allocate(100);
	
	CompileAndUploadGui compileAndUploadGuiMock;
	OptionPane optionPaneMock;

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
		
		protocolMock = mock(BackgroundTaskProtokol.class);
			when(protocolMock.getSoundSampleBuffer()).thenReturn(byteBuffer0);
		
		soundplayerMock = mock(SoundPlayer.class);
		
		statusListenerMock = mock(StatusListener.class);
		
		actionEventMock = mock(ActionEvent.class);
			when(actionEventMock.getActionCommand()).thenReturn(
					"Unknown", 
					"Do Upload", 	// empty MemoryRegions
					"Do Upload", 	// one MemoryRegion
					"Abort"
				);
			
		propertyChangeEventMock = mock(PropertyChangeEvent.class);
			when(propertyChangeEventMock.getPropertyName()).thenReturn(
					"progress",	// 42						setProgress()
					"state",	// bgCompiler	STARTED		nothing
					"state",	// bgCompiler	DONE		runUpload()
					"state",	// bgPlayer		STARTED		nothing
					"state",	// bgPlayer		DONE		compileNextRegion()
					
					"state",	// bgPlayer		UNDEF		ERROR
					"state",	// null			*			ERROR
					"unknown"	//							ERROR
				);
			
			when(propertyChangeEventMock.getNewValue()).thenReturn(
					42,			// progress
					"STARTED",	// state bgCompiler			nothing
					"DONE",		// state bgCompiler			runUpload()
					"STARTED",	// state bgPlayer			nothing
					"DONE",		// state bgPlayer			compileNextRegion()
					
					"UNDEF"		// state bgPlayer			ERROR
				);
			
		propertyChangeListenerMock = mock(PropertyChangeListener.class);

		memoryRegionList = new ArrayList<MemoryRegion>();
		
		memoryRegionMock0 = mock(MemoryRegion.class);
			when(memoryRegionMock0.getContent()).thenReturn(byteBuffer0);
		
		memoryRegionMock1 = mock(MemoryRegion.class);
			when(memoryRegionMock0.getContent()).thenReturn(byteBuffer1);
			
		wfeMock = mock(WorkflowEngine.class);
			when(wfeMock.getProtocol()).thenReturn(protocolMock);
			when(wfeMock.getSoundPlayer()).thenReturn(soundplayerMock);
			when(wfeMock.getSelectedMemoryRegions()).thenReturn(memoryRegionList);

			
		try(
					
			MockedConstruction<CompileAndUploadGui> mcCAGui = mockConstruction(CompileAndUploadGui.class);
				
		) {
				
			cut = new CompileAndUploadAction(wfeMock);

			assertTrue(cut != null);
			assertEquals(1, mcCAGui.constructed().size());
			compileAndUploadGuiMock = mcCAGui.constructed().get(0);
						
		}; // yrt
				
	} // setUp()
	

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		
		cut = null;
		
		reset(wfeMock, protocolMock);
		wfeMock = null;
		
	} // tearDown()
	
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	

	/**
	 * Test method for {@link control.gui.CompileAndUploadAction#CompileAndUploadAction(control.WorkflowEngine)}.
	 */
//	@Test
	final void testCompileAndUploadAction() {
		LOGGER.info("testCompileAndUploadAction()");

		reset(wfeMock);
		cut = new CompileAndUploadAction(wfeMock);
		
		verify(wfeMock, times(1)).getProtocol();
		verify(wfeMock, times(1)).getSoundPlayer();
		
	} // testCompileAndUploadAction()

	
	/**
	 * Test method for {@link control.gui.CompileAndUploadAction#actionPerformed(java.awt.event.ActionEvent)}.
	 */
	@SuppressWarnings("rawtypes")
//	@Test
	final void testActionPerformed() {
		LOGGER.info("testActionPerformed()");

		InOrder inOrder = Mockito.inOrder(
				wfeMock, 
				protocolMock,
				propertyChangeListenerMock
			);
		
		
		// -----------------------------------------------------------------------------------------------
		// Unknown action
		IllegalAccessError thrownIAE;
		
		thrownIAE = assertThrows(IllegalAccessError.class, () -> {
			cut.actionPerformed(actionEventMock);
		});
		
		LOGGER.debug(thrownIAE.toString());
		assertTrue(thrownIAE.getMessage().equals("Unknown Action Command!"));
		
		
		// -----------------------------------------------------------------------------------------------
		// Do Upload" on empty MemoryRegion
		cut.actionPerformed(actionEventMock);
		
		inOrder.verify(wfeMock).getSelectedMemoryRegions();
		inOrder.verify(wfeMock).setStatusMessage("Please select one or more memory regions for upload");
		
		
		// Do Upload" having one MemoryRegion
		memoryRegionList.add(memoryRegionMock0);
		cut.actionPerformed(actionEventMock);
		
		inOrder.verify(wfeMock).getSelectedMemoryRegions();
		inOrder.verify(wfeMock).setStatusMessage(anyString());
		
		inOrder.verify(protocolMock).setStartAddress(anyLong());
		inOrder.verify(protocolMock).setEndAddress(anyLong());
		inOrder.verify(protocolMock).setDataBuffer(byteBuffer0);
		
		inOrder.verify((BackgroundTask)protocolMock).addPropertyChangeListener(cut);
		inOrder.verify(protocolMock).registerStatusListener(cut);
		
		inOrder.verify(protocolMock).execute();
		
		
		// -----------------------------------------------------------------------------------------------
		// Do Upload" on empty MemoryRegion
		cut.actionPerformed(actionEventMock);
		
		verify(protocolMock).stop();
		
	} // testActionPerformed()


	/**
	 * Test method for {@link control.gui.CompileAndUploadAction#propertyChange(java.beans.PropertyChangeEvent)}.
	 */
	@Test
	final void testPropertyChange() {
		LOGGER.info("testPropertyChange()");

//		try(
//				
//			MockedConstruction<OptionPane> mcOptionPane = mockConstruction(OptionPane.class);	
//					
//		) {
//					
//			assertEquals(1, mcOptionPane.constructed().size());
//			optionPaneMock = mcOptionPane.constructed().get(0);
//			assertTrue(optionPaneMock != null);
//
//			// set progress
//			LOGGER.info("set progress");
//			cut.propertyChange(propertyChangeEventMock);
//
//			verify(compileAndUploadGuiMock, times(1)).setProgress(42);
//			
//			// bgCompiler state STARTED
//			LOGGER.info("bgCompiler state STARTED");
//			cut.runCompile(memoryRegionMock0);
//			cut.propertyChange(propertyChangeEventMock);
//			
//			// bgCompiler state DONE
//			LOGGER.info("bgCompiler state DONE");
//			cut.propertyChange(propertyChangeEventMock);
//
//			when(optionPaneMock.showConfirmDialog(any(), anyString(), anyString(), anyInt(), anyInt()))
//			.thenReturn(JOptionPane.YES_OPTION);
//
//				
//			
//			
//			}; // yrt

//			verify(compileAndUploadGuiMock, times(1)).setProgress(0);
//			verify(wfeMock).setStatusMessage("Pending upload of region 1");
			
	} // testPropertyChange()
	
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	

	/**
	 * Test method for {@link control.gui.CompileAndUploadAction#runCompile(extension.model.MemoryRegion)}.
	 */
//	@Test
	final void testRunCompile() {
		LOGGER.info("testRunCompile()");

		/*
		 * Tested by testActionPerformed()
		 */
		
		assertTrue(true);
		
	} // testRunCompile()


	/**
	 * Test method for {@link control.gui.CompileAndUploadAction#runUpload()}.
	 */
//	@Test
	final void testRunUpload() {
		LOGGER.info("testRunUpload()");

		/*
		 * tested by testPropertyChange()
		 */
		
		assertTrue(true);
		
	} // testRunUpload()


	/**
	 * Test method for {@link control.gui.CompileAndUploadAction#setProtocol(protocol.Protocol)}.
	 */
//	@Test
	final void testSetProtocol() {
		LOGGER.info("()");

		fail("Not yet implemented"); // TODO
		
	} // ()


	/**
	 * Test method for {@link control.gui.CompileAndUploadAction#compileNextRegion()}.
	 */
//	@Test
	final void testCompileNextRegion() {
		LOGGER.info("()");

		fail("Not yet implemented"); // TODO
		
	} // ()


	/**
	 * Test method for {@link control.gui.CompileAndUploadAction#done()}.
	 */
//	@Test
	final void testDone() {
		LOGGER.info("()");

		fail("Not yet implemented"); // TODO
		
	} // ()


	/**
	 * Test method for {@link control.gui.CompileAndUploadAction#setSoundPlayer(sound.SoundPlayer)}.
	 */
//	@Test
	final void testSetSoundPlayer() {
		LOGGER.info("()");

		fail("Not yet implemented"); // TODO
		
	} // ()


	/**
	 * Test method for {@link control.gui.CompileAndUploadAction#getGui()}.
	 */
//	@Test
	final void testGetGui() {
		LOGGER.info("()");

		fail("Not yet implemented"); // TODO
		
	} // ()


	/**
	 * Test method for {@link control.gui.CompileAndUploadAction#notification(java.lang.Object)}.
	 */
//	@Test
	final void testNotification() {
		LOGGER.info("()");

		fail("Not yet implemented"); // TODO
		
	} // ()


} // sslac
