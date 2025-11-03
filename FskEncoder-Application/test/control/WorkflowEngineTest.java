/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : WorkflowEngineTest.java
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


package control;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Mixer.Info;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import control.gui.InputFileController;
import control.gui.MainWindowController;
import control.gui.OutputDeviceController;
import control.gui.StatusBarUpdate;
import extension.control.ReaderExtensionControl;
import extension.control.StatusMessenger;
import extension.control.TargetSystemExtensionControl;
import extension.factory.InputReaderExtensionFactory;
import extension.factory.PlugInFactory;
import extension.factory.TargetSystemExtensionFactory;
import extension.model.InputReaderExtensionDao;
import extension.model.MemoryMap;
import extension.model.MemoryRegion;
import extension.model.TargetSystemExtensionDao;
import extension.protocol.Protocol;
import extension.sound.FskAudioFormat;
import extension.source.Reader;
import extension.view.gui.ExtensionGui;
import model.FskUploaderModel;
import sound.EnlistOutputDevices;
import sound.SoundPlayer;
import sound.support.TestMixerInfo;
import view.gui.EmptyExtensionGui;

/**
 * Responsibilities:<br>
 * JUnit tests of class WorkflowEngine
 * 
 * <p>
 * Collaborators:<br>
 * WorkflowEngine
 * 
 * <p>
 * Description:<br>
 * Contains all JUnit tests for the WorkflowEngine.
 * 
 * <p>
 * @author Stefan
 *
 */

class WorkflowEngineTest {

	private static Logger LOGGER = null;
	
	private FskUploaderModel fskUploaderModelMock;
	private ExtensionGui extensionGuiMock;
	private StatusBarUpdate statusBarUpdateMock;
	
	private OutputDeviceController outputDeviceControllerMock;
	
	private InputReaderExtensionFactory inputReaderFactoryMock;
	private ReaderExtensionControl readerExtensionControllerMock;
	private InputReaderExtensionDao inputReaderExtensionDao;

	private TargetSystemExtensionFactory targetSystemFactoryMock;
	private TargetSystemExtensionControl  targetSystemExtensionControllerMock;
	private TargetSystemExtensionDao targetSystemExtensionDao;

	private Reader readerMock;
	private MainWindowController mainWindowControllerMock;

	private Protocol protocolMock;

	

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
		
		fskUploaderModelMock = mock(FskUploaderModel.class);
		extensionGuiMock = mock(ExtensionGui.class);
		
		outputDeviceControllerMock = mock(OutputDeviceController.class);
		statusBarUpdateMock = mock(StatusBarUpdate.class);

		inputReaderFactoryMock = mock(InputReaderExtensionFactory.class);
		mainWindowControllerMock = mock(MainWindowController.class);
				
		readerMock = mock(Reader.class);
		MemoryMap memoryMapMock = mock(MemoryMap.class);

		readerExtensionControllerMock = mock(ReaderExtensionControl.class);
		
		inputReaderExtensionDao = new InputReaderExtensionDao(
				readerMock, 
				memoryMapMock, 
				extensionGuiMock, 
				readerExtensionControllerMock
		);
		
		
		protocolMock = mock(Protocol.class);
		
		targetSystemFactoryMock = mock(TargetSystemExtensionFactory.class);
		targetSystemExtensionControllerMock = mock(TargetSystemExtensionControl.class);
		
		targetSystemExtensionDao = new TargetSystemExtensionDao(
				protocolMock, 
				extensionGuiMock, 
				targetSystemExtensionControllerMock
		);

	} // setUp()
	

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	
	
	/**
	 * Test method for {@link control.WorkflowEngine#WorkflowEngine(model.FskUploaderModel)}.
	 */
	@Test
	final void testWorkflowEngine() {
		LOGGER.info("testWorkflowEngine()");

		IllegalArgumentException thrown;
		
		thrown = assertThrows(IllegalArgumentException.class, () -> new WorkflowEngine(null));
		assertEquals("aModel can't be null", thrown.getMessage());
		
		assertDoesNotThrow(() -> new WorkflowEngine(fskUploaderModelMock));
				
	} // testWorkflowEngine()


	/**
	 * Test method for {@link control.WorkflowEngine#getOutputDeviceNames()}.
	 */
	@Test
	final void testGetOutputDeviceNames() {
		LOGGER.info("testGetOutputDeviceNames()");

		try(
				
			MockedConstruction<EnlistOutputDevices> mcEnlistOutputDevices = mockConstruction(EnlistOutputDevices.class,
				(mock, context) -> {
					
					when(mock.getOutputDeviceNames()).thenReturn( new String[] { "one", "two"} );
					
				}
			);
				
		) {
			
			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
			assertEquals(1, mcEnlistOutputDevices.constructed().size());
			
			String[] result = cut.getOutputDeviceNames();
			assertTrue(result != null);
			assertEquals(2, result.length);
			
		} // yrt
		
	} // testGetOutputDeviceNames()


	/**
	 * Test method for {@link control.WorkflowEngine#getSelectedFileName()}.
	 */
	@Test
	final void testGetSelectedFileName() {
		LOGGER.info("testGetSelectedFileName()");

		/*
		 * 
		 * Due to the complex test setup, 
		 * this test is combined with testSetSelectedFileName()
		 * 
		 */
		
		assertTrue(true);
		
	} // testGetSelectedFileName()


	/**
	 * Test method for {@link control.WorkflowEngine#getSelectedMemoryRegions()}.
	 */
	@Test
	final void testGetSelectedMemoryRegions() {
		LOGGER.info("testGetSelectedMemoryRegions()");

		final String TARGET_SYSTEM = "Test";
		
		List<MemoryRegion> memoryRegions = new ArrayList<>();
		memoryRegions.add(new MemoryRegion(0));
		memoryRegions.add(new MemoryRegion(1));
		
		try(
			
			MockedStatic<PlugInFactory> msPlugInFactory = mockStatic(PlugInFactory.class);
				
		)
		{
		
			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
			cut.registerCallback(mainWindowControllerMock);

			when(fskUploaderModelMock.getInputReaderProviderClassName(TARGET_SYSTEM)).thenReturn("tEST");
			
			msPlugInFactory.when( () -> PlugInFactory.getInputReaderExtensionFactory(any()) )
				.thenReturn(inputReaderFactoryMock);
			
			when(inputReaderFactoryMock.getInputReaderExtensions(cut)).thenReturn(inputReaderExtensionDao);
			
			when(readerExtensionControllerMock.createLayout()).thenReturn(extensionGuiMock);
			when(readerExtensionControllerMock.getSelectedMemoryRegions()).thenReturn(memoryRegions);
			
			cut.setReaderPlugin(TARGET_SYSTEM);

			
			// test
			List<MemoryRegion> result = cut.getSelectedMemoryRegions();
			verify(readerExtensionControllerMock, times(1)).getSelectedMemoryRegions();
			assertTrue(result != null);
			assertEquals(memoryRegions, result);
			
		} // yrt
		catch (Exception e) {
			
			LOGGER.error("Unexpected exception caught: {}", e);
			fail("Unexpected exception caught: " + e);
		}
		
	} // testGetSelectedMemoryRegions()


	/**
	 * Test method for {@link control.WorkflowEngine#getOutputDevice(java.lang.String)}.
	 */
	@Test
	final void testGetOutputDevice() {
		LOGGER.info("testGetOutputDevice()");

		final String OUTPUT_DEVICE_NAME = "foo";
		
		try(
			
			MockedConstruction<EnlistOutputDevices> mcEnlistOutputDevices = mockConstruction(EnlistOutputDevices.class,
				(mock, context) -> {
					
					when(mock.getOutputDevice(OUTPUT_DEVICE_NAME)).thenReturn( 
					
						new TestMixerInfo("name", "vendor", "description", "version")
							
					); // when
					
				} // ->
			); // mockConstruction()
				
		) {
			
			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
			assertEquals(1, mcEnlistOutputDevices.constructed().size());
			
			Info result = cut.getOutputDevice(OUTPUT_DEVICE_NAME);
			assertTrue(result != null);
			assertEquals("name", result.getName());
			assertEquals("vendor", result.getVendor());
			assertEquals("description", result.getDescription());
			assertEquals("version", result.getVersion());
			
		} // yrt
		
	} // testGetOutputDevice()


	/**
	 * Test method for {@link control.WorkflowEngine#loadCandidateFile()}.
	 */
	@Test
	final void testLoadCandidateFile() {
		LOGGER.info("testLoadCandidateFile()");

		final String TARGET_SYSTEM = "Test";
		
		try(
			
			MockedStatic<PlugInFactory> msPlugInFactory = mockStatic(PlugInFactory.class);
				
		)
		{
		
			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
			cut.registerCallback(mainWindowControllerMock);

			when(fskUploaderModelMock.getInputReaderProviderClassName(TARGET_SYSTEM)).thenReturn("tEST");
			
			msPlugInFactory.when( () -> PlugInFactory.getInputReaderExtensionFactory(any()) )
				.thenReturn(inputReaderFactoryMock);
			
			when(inputReaderFactoryMock.getInputReaderExtensions(cut)).thenReturn(inputReaderExtensionDao);
			
			when(readerExtensionControllerMock.createLayout()).thenReturn(extensionGuiMock);
			
			cut.setReaderPlugin(TARGET_SYSTEM);
			
			// test
			cut.loadCandidateFile();
			verify(readerExtensionControllerMock, times(1)).load();
			verify(mainWindowControllerMock, times(2)).setReaderExtensionPanel(extensionGuiMock);
			
		} // yrt
		catch (Exception e) {
			
			LOGGER.error("Unexpected exception caught: {}", e);
			fail("Unexpected exception caught: " + e);
		}
		
	} // testLoadCandidateFile()


	/**
	 * Test method for {@link control.WorkflowEngine#registerCallback(control.gui.InputFileController)}.
	 */
	@Test
	final void testRegisterCallbackInputFileController() {
		LOGGER.info("testRegisterCallbackInputFileController()");

		IllegalArgumentException thrown;
		
		try(
		
			MockedConstruction<EnlistOutputDevices> mcEnlistOutputDevices = mockConstruction(EnlistOutputDevices.class);
			
		) {
			
			InputFileController controllerMock = mock(InputFileController.class);
			
			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
			
			thrown = assertThrows(IllegalArgumentException.class, () -> { 
				cut.registerCallback((InputFileController)null);
			});
			
			assertEquals("aInputFileController can't be null.", thrown.getMessage());
			
			assertDoesNotThrow(() -> { cut.registerCallback(controllerMock); });
			
		} // yrt
		
	} // testRegisterCallbackInputFileController()


	/**
	 * Test method for {@link control.WorkflowEngine#registerCallback(control.gui.MainWindowController)}.
	 */
	@Test
	final void testRegisterCallbackMainWindowController() {
		LOGGER.info("testRegisterCallbackMainWindowController()");

		IllegalArgumentException thrown;
		
		try(
		
			MockedConstruction<EnlistOutputDevices> mcEnlistOutputDevices = mockConstruction(EnlistOutputDevices.class);
			
		) {
			
			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
			
			thrown = assertThrows(IllegalArgumentException.class, () -> { 
				cut.registerCallback((MainWindowController)null);
			});
			
			assertEquals("aMainWindowController can't be null.", thrown.getMessage());
			
			assertDoesNotThrow(() -> { cut.registerCallback(mainWindowControllerMock); });
			
		} // yrt
		
	} // testRegisterCallbackMainWindowController()


	/**
	 * Test method for {@link control.WorkflowEngine#registerCallback(control.gui.OutputDeviceController)}.
	 */
	@Test
	final void testRegisterCallbackOutputDeviceController() {
		LOGGER.info("testRegisterCallbackOutputDeviceController()");

		IllegalArgumentException thrown;
		
		try(
		
			MockedConstruction<EnlistOutputDevices> mcEnlistOutputDevices = mockConstruction(EnlistOutputDevices.class);
			
		) {
			
			OutputDeviceController controllerMock = mock(OutputDeviceController.class);
			
			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
			
			thrown = assertThrows(IllegalArgumentException.class, () -> { 
				cut.registerCallback((OutputDeviceController)null);
			});
			
			assertEquals("aOutputDeviceSelection can't be null.", thrown.getMessage());
			
			assertDoesNotThrow(() -> { cut.registerCallback(controllerMock); });
			
		} // yrt
		
	} // testRegisterCallbackOutputDeviceController()


	/**
	 * Test method for {@link control.WorkflowEngine#registerCallback(control.gui.StatusBarUpdate)}.
	 */
	@Test
	final void testRegisterCallbackStatusBarUpdate() {
		LOGGER.info("testRegisterCallbackStatusBarUpdate()");

		IllegalArgumentException thrown;
		
		try(
		
			MockedConstruction<EnlistOutputDevices> mcEnlistOutputDevices = mockConstruction(EnlistOutputDevices.class);
			
		) {
			
			StatusBarUpdate controllerMock = mock(StatusBarUpdate.class);
			
			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
			
			thrown = assertThrows(IllegalArgumentException.class, () -> { 
				cut.registerCallback((StatusBarUpdate)null);
			});
			
			assertEquals("aStatusBarUpdate can't be null.", thrown.getMessage());
			
			assertDoesNotThrow(() -> { cut.registerCallback(controllerMock); });
			
		} // yrt
		
	} // testRegisterCallbackStatusBarUpdate()


	/**
	 * Test method for {@link control.WorkflowEngine#setReaderPlugin(java.lang.String)}.
	 */
	@Test
	final void testSetReaderPlugin() {
		LOGGER.info("testSetReaderPlugin()");

		final String TARGET_SYSTEM = "Test";
		
		try(
			
			MockedStatic<PlugInFactory> msPlugInFactory = mockStatic(PlugInFactory.class);
				
		)
		{
		
			// setup
			msPlugInFactory.when( () -> PlugInFactory.getInputReaderExtensionFactory(any()) )
			.thenReturn(inputReaderFactoryMock);
		
			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
			cut.registerCallback(mainWindowControllerMock);
			cut.registerCallback(statusBarUpdateMock);
			
			
			// test: IllegalArgumentException from factory
			when(inputReaderFactoryMock.getInputReaderExtensions(any())).thenThrow(IllegalArgumentException.class);
			
			cut.setReaderPlugin(TARGET_SYSTEM);
			
			verify(fskUploaderModelMock, times(1)).getInputReaderProviderClassName(TARGET_SYSTEM);
			verify(mainWindowControllerMock, never()).setReaderExtensionPanel(extensionGuiMock);
			verify(statusBarUpdateMock, times(1)).setStatusMessage("Can't set up reader, see logfile!");
			
			
			// further setup
			reset(fskUploaderModelMock);
			reset(inputReaderFactoryMock);
			reset(mainWindowControllerMock);
			reset(statusBarUpdateMock);

			
			// test: Exception from factory
			when(inputReaderFactoryMock.getInputReaderExtensions(any())).thenThrow(Exception.class);
			
			cut.setReaderPlugin(TARGET_SYSTEM);
			
			verify(fskUploaderModelMock, times(1)).getInputReaderProviderClassName(TARGET_SYSTEM);
			verify(mainWindowControllerMock, never()).setReaderExtensionPanel(extensionGuiMock);
			verify(statusBarUpdateMock, times(1)).setStatusMessage("Can't set up reader, see logfile!");
			
			
			// further setup
			reset(fskUploaderModelMock);
			reset(inputReaderFactoryMock);
			reset(mainWindowControllerMock);
			reset(statusBarUpdateMock);
			
			
			// test
			when(fskUploaderModelMock.getTargetSystemProviderClassName(TARGET_SYSTEM)).thenReturn("tEST");
			when(inputReaderFactoryMock.getInputReaderExtensions(any())).thenReturn(inputReaderExtensionDao);			
			when(readerExtensionControllerMock.createLayout()).thenReturn(extensionGuiMock);

			cut.setReaderPlugin(TARGET_SYSTEM);

			verify(fskUploaderModelMock, times(1)).getInputReaderProviderClassName(TARGET_SYSTEM);
			verify(readerExtensionControllerMock, times(1)).initialize(inputReaderExtensionDao, cut);
			verify(readerExtensionControllerMock, times(1)).createLayout();
			verify(mainWindowControllerMock, times(1)).setReaderExtensionPanel(extensionGuiMock);
			
			
//			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
//			cut.registerCallback(mainWindowControllerMock);
//
//			when(fskUploaderModelMock.getInputReaderProviderClassName(TARGET_SYSTEM)).thenReturn("tEST");
//			
//			msPlugInFactory.when( () -> PlugInFactory.getInputReaderExtensionFactory(any()) )
//				.thenReturn(inputReaderFactoryMock);
//			
//			when(inputReaderFactoryMock.getInputReaderExtensions(cut)).thenReturn(inputReaderExtensionDao);
//			
//			when(readerExtensionControllerMock.createLayout()).thenReturn(extensionGuiMock);
//			
//			// test
//			cut.setReaderPlugin(TARGET_SYSTEM);
//			
//			verify(fskUploaderModelMock, times(1)).getInputReaderProviderClassName(TARGET_SYSTEM);
//			verify(readerExtensionControllerMock, times(1)).initialize(inputReaderExtensionDao, cut);
//			verify(readerExtensionControllerMock, times(1)).createLayout();
//			verify(mainWindowControllerMock, times(1)).setReaderExtensionPanel(extensionGuiMock);
			
		} // yrt
		catch (Exception e) {
			
			LOGGER.error("Unexpected exception caught: {}", e);
			fail("Unexpected exception caught: " + e);
		}
		
	} // testSetReaderPlugin()


	/**
	 * Test method for {@link control.WorkflowEngine#setSelectedFileName(java.lang.String)}.
	 */
	@Test
	final void testSetSelectedFileName() {
		LOGGER.info("testSetSelectedFileName()");

		final String TARGET_SYSTEM = "Test";
		final String SELECTED_FILE_NAME = "/foo/bar/baz.boo";
		
		try(
			
			MockedStatic<PlugInFactory> msPlugInFactory = mockStatic(PlugInFactory.class);
				
		)
		{
		
			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
			cut.registerCallback(mainWindowControllerMock);

			when(fskUploaderModelMock.getInputReaderProviderClassName(TARGET_SYSTEM)).thenReturn("tEST");
			
			msPlugInFactory.when( () -> PlugInFactory.getInputReaderExtensionFactory(any()) )
				.thenReturn(inputReaderFactoryMock);
			
			when(inputReaderFactoryMock.getInputReaderExtensions(cut)).thenReturn(inputReaderExtensionDao);
			
			when(readerExtensionControllerMock.createLayout()).thenReturn(extensionGuiMock);

			cut.setReaderPlugin(TARGET_SYSTEM);

			
			// test
			cut.setSelectedFileName(SELECTED_FILE_NAME);
			
			verify(readerExtensionControllerMock, times(1)).setFileName(SELECTED_FILE_NAME);
			
			assertEquals(SELECTED_FILE_NAME, cut.getSelectedFileName());
			
		} // yrt
		catch (Exception e) {
			
			LOGGER.error("Unexpected exception caught: {}", e);
			fail("Unexpected exception caught: " + e);
		}
		
	} // testSetSelectedFileName()


	/**
	 * Test method for {@link control.WorkflowEngine#setStatusMessage(java.lang.String)}.
	 */
	@Test
	final void testSetStatusMessage() {
		LOGGER.info("testSetStatusMessage()");

		final String MESSAGE = "Hello world!";
		
		try(
		
			MockedConstruction<EnlistOutputDevices> mcEnlistOutputDevices = mockConstruction(EnlistOutputDevices.class);
			
		) {
			
			StatusBarUpdate controllerMock = mock(StatusBarUpdate.class);
			
			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
			cut.registerCallback(controllerMock);
			
			cut.setStatusMessage(MESSAGE);
			verify(controllerMock, times(1)).setStatusMessage(MESSAGE);
			
		} // yrt
		
	} // testSetStatusMessage()


	/**
	 * Test method for {@link control.WorkflowEngine#setTargetPlugin(java.lang.String)}.
	 */
	@Test
	final void testSetTargetPlugin() {
		LOGGER.info("testSetTargetPlugin()");

		/* needs
		 *	PlugInFactory
		 *	TargetSystemExtensionFactory
		 *	TargetSystemExtensionDao  
		 *	targetController
		 */
		
		final String TARGET_SYSTEM = "Test";
		
		try(
				
			MockedStatic<PlugInFactory> msPlugInFactory = mockStatic(PlugInFactory.class);
				
		)
		{
		
			// setup
			msPlugInFactory.when( () -> PlugInFactory.getTargetSystemExtensionFactory(any()) )
			.thenReturn(targetSystemFactoryMock);
		
			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
			cut.registerCallback(mainWindowControllerMock);
			cut.registerCallback(statusBarUpdateMock);
			
			
			// test: exception from factory
			when(targetSystemFactoryMock.getTargetSystemExtension((StatusMessenger)cut)).thenThrow(IllegalArgumentException.class);
			cut.setTargetPlugin(TARGET_SYSTEM);
			
			verify(fskUploaderModelMock, times(1)).getTargetSystemProviderClassName(TARGET_SYSTEM);
			verify(mainWindowControllerMock, never()).setTargetExtensionPanel(extensionGuiMock);
			verify(statusBarUpdateMock, times(1)).setStatusMessage("Can't set up target system, see logfile!");
			
			
			// further setup
			reset(fskUploaderModelMock);
			reset(targetSystemFactoryMock);
			reset(mainWindowControllerMock);

			when(fskUploaderModelMock.getTargetSystemProviderClassName(TARGET_SYSTEM)).thenReturn("tEST");
			when(targetSystemFactoryMock.getTargetSystemExtension((StatusMessenger)cut)).thenReturn(targetSystemExtensionDao);
			when(targetSystemExtensionControllerMock.createLayout()).thenReturn(extensionGuiMock);
			
			
			// test
			cut.setTargetPlugin(TARGET_SYSTEM);

			verify(fskUploaderModelMock, times(1)).getTargetSystemProviderClassName(TARGET_SYSTEM);
			verify(targetSystemExtensionControllerMock, times(1)).createLayout();
			verify(mainWindowControllerMock, times(1)).setTargetExtensionPanel(extensionGuiMock);
			
		} // yrt
		catch (Exception e) {
			
			LOGGER.error("Unexpected exception caught: {}", e);
			fail("Unexpected exception caught: " + e);
		}
		
	} // testSetTargetPlugin()


	/**
	 * Test method for {@link control.WorkflowEngine#setTargetSystem(java.lang.String)}.
	 */
	@Test
	final void testSetTargetSystem() {
		LOGGER.info("testSetTargetSystem()");

		/*
		 * needs
		 * 	setReaderPlugin
		 * 	setTargetPlugin
		 * 
		 * 	outputDeviceSelectionCallback
		 * 	setStatusMessage()
		 *  mainWindowCallback
		 */

		/*
		 * Needs 
		 */
		
		final String TARGET_SYSTEM = "Test";
		InOrder fskUploaderModelOrder;
		
		try(
			
			MockedStatic<PlugInFactory> msPlugInFactory = mockStatic(PlugInFactory.class);
				
		)
		{
		
			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
			cut.registerCallback(mainWindowControllerMock);
			cut.registerCallback(statusBarUpdateMock);

			msPlugInFactory.when( () -> PlugInFactory.getInputReaderExtensionFactory(any()) )
				.thenReturn(inputReaderFactoryMock);
			
			msPlugInFactory.when( () -> PlugInFactory.getTargetSystemExtensionFactory(any()) )
			.thenReturn(targetSystemFactoryMock);

			when(inputReaderFactoryMock.getInputReaderExtensions(cut)).thenReturn(inputReaderExtensionDao);	
			when(targetSystemFactoryMock.getTargetSystemExtension((StatusMessenger)cut)).thenReturn(targetSystemExtensionDao);
			
			when(readerExtensionControllerMock.createLayout()).thenReturn(extensionGuiMock);	
			when(targetSystemExtensionControllerMock.createLayout()).thenReturn(extensionGuiMock);
			
			// test: No name, NO default name
			when(fskUploaderModelMock.getTargetSystemName()).thenReturn(WorkflowEngine.NOT_DEFINED);

			cut.setTargetSystem(null);
			verify(fskUploaderModelMock, times(1)).getTargetSystemName();
			verify(fskUploaderModelMock, never()).getInputReaderProviderClassName(TARGET_SYSTEM);
			verify(fskUploaderModelMock, never()).getTargetSystemProviderClassName(TARGET_SYSTEM);
			verify(statusBarUpdateMock, never()).setStatusMessage("");
			verify(mainWindowControllerMock, times(1)).setTitle();
			
			
			// test: No name, BUT default name & NO outputDeviceCallback
			reset(fskUploaderModelMock);
			reset(mainWindowControllerMock);
			reset(statusBarUpdateMock);
			fskUploaderModelOrder = inOrder(fskUploaderModelMock);
			
			when(fskUploaderModelMock.getTargetSystemName()).thenReturn(TARGET_SYSTEM);

			cut.setTargetSystem(null);

			fskUploaderModelOrder.verify(fskUploaderModelMock, times(1)).getTargetSystemName();
			fskUploaderModelOrder.verify(fskUploaderModelMock, times(1)).getInputReaderProviderClassName(TARGET_SYSTEM);
			fskUploaderModelOrder.verify(fskUploaderModelMock, times(1)).getTargetSystemProviderClassName(TARGET_SYSTEM);
			verify(statusBarUpdateMock, times(1)).setStatusMessage("");
			verify(mainWindowControllerMock, times(1)).setTitle();

		
			// test: BUT name, IGNORE default name & BUT outputDeviceCallback
			reset(fskUploaderModelMock);
			reset(mainWindowControllerMock);
			reset(statusBarUpdateMock);
			fskUploaderModelOrder = inOrder(fskUploaderModelMock);
			
			when(fskUploaderModelMock.getTargetSystemName()).thenReturn(TARGET_SYSTEM);

			cut.registerCallback(outputDeviceControllerMock);
			cut.setTargetSystem(TARGET_SYSTEM);

			verify(fskUploaderModelMock, never()).getTargetSystemName();
			fskUploaderModelOrder.verify(fskUploaderModelMock, times(1)).setTargetSystemName(TARGET_SYSTEM);
			fskUploaderModelOrder.verify(fskUploaderModelMock, times(1)).getInputReaderProviderClassName(TARGET_SYSTEM);
			fskUploaderModelOrder.verify(fskUploaderModelMock, times(1)).getTargetSystemProviderClassName(TARGET_SYSTEM);
			verify(outputDeviceControllerMock, times(1)).setOutputDevice();
			verify(statusBarUpdateMock, times(1)).setStatusMessage("");
			verify(mainWindowControllerMock, times(1)).setTitle();

		} // yrt
		catch (Exception e) {
			
			LOGGER.error("Unexpected exception caught: {}", e);
			fail("Unexpected exception caught: " + e);
		}
		
	} // testSetTargetSystem()


	/**
	 * Test method for {@link control.WorkflowEngine#setUploadTimestamp(java.lang.String)}.
	 */
	@Test
	final void testSetUploadTimestamp() {
		LOGGER.info("testSetUploadTimestamp()");

		final String TIME_STAMP = "12:34:56";
		
		try(
				
			MockedConstruction<EnlistOutputDevices> mcEnlistOutputDevices = mockConstruction(EnlistOutputDevices.class);
			
		) {
			
			InputFileController controllerMock = mock(InputFileController.class);
			
			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
			cut.registerCallback(controllerMock);
			
			cut.setUploadTimestamp(TIME_STAMP);
			
			verify(controllerMock, times(1)).setLastUploadTime(TIME_STAMP);
			
		} // yrt
		
	} // testSetUploadTimestamp()


	/**
	 * Test method for {@link control.WorkflowEngine#getAudioFormat()}.
	 */
	@Test
	final void testGetAudioFormat() {
		LOGGER.info("testGetAudioFormat()");

		final String TARGET_SYSTEM = "Test";
		
		FskAudioFormat fskAudioFormatMock = mock(FskAudioFormat.class);
		FskAudioFormat result;
		
		try(
			
			MockedStatic<PlugInFactory> msPlugInFactory = mockStatic(PlugInFactory.class);
				
		)
		{
		
			// setup
			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
			cut.registerCallback(mainWindowControllerMock);

			msPlugInFactory.when( () -> PlugInFactory.getTargetSystemExtensionFactory(any()) )
				.thenReturn(targetSystemFactoryMock);
			
			when(targetSystemFactoryMock.getTargetSystemExtension((StatusMessenger)cut)).thenReturn(targetSystemExtensionDao);
			
			
			// test: NO targetController
			result = cut.getAudioFormat();
			assertEquals(null, result);
			verify(targetSystemExtensionControllerMock, never()).getAudioFormat();
			
			
			// test: BUT targetController
			reset(targetSystemExtensionControllerMock);
			when(targetSystemExtensionControllerMock.getAudioFormat()).thenReturn(fskAudioFormatMock);
			
			cut.setTargetPlugin(TARGET_SYSTEM);
			result = cut.getAudioFormat();
			assertEquals(fskAudioFormatMock, result);
			verify(targetSystemExtensionControllerMock, times(1)).getAudioFormat();
			
		} // yrt
		catch (Exception e) {
			
			LOGGER.error("Unexpected exception caught: {}", e);
			fail("Unexpected exception caught: " + e);
		}
		
	} // testGetAudioFormat()


	/**
	 * Test method for {@link control.WorkflowEngine#getReaderGui()}.
	 */
	@Test
	final void testGetReaderGui() {
		LOGGER.info("testGetReaderGui()");

		final String TARGET_SYSTEM = "Test";
		
		try(
			
			MockedStatic<PlugInFactory> msPlugInFactory = mockStatic(PlugInFactory.class);
				
		)
		{
		
			msPlugInFactory.when( () -> PlugInFactory.getInputReaderExtensionFactory(any()) )
				.thenReturn(inputReaderFactoryMock);
		
			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
			cut.registerCallback(mainWindowControllerMock);
			
			// test 1: no input extension set
			JPanel result = cut.getReaderGui();
			assertTrue(result instanceof EmptyExtensionGui);
			
			
			// test
			when(fskUploaderModelMock.getInputReaderProviderClassName(TARGET_SYSTEM)).thenReturn("tEST");			
			when(inputReaderFactoryMock.getInputReaderExtensions(cut)).thenReturn(inputReaderExtensionDao);			
			when(readerExtensionControllerMock.createLayout()).thenReturn(extensionGuiMock);
			cut.setReaderPlugin(TARGET_SYSTEM);

			result = cut.getReaderGui();

			assertEquals(extensionGuiMock, result);
			
		} // yrt
		catch (Exception e) {
			
			LOGGER.error("Unexpected exception caught: {}", e);
			fail("Unexpected exception caught: " + e);
		}
		
	} // testGetReaderGui()


	/**
	 * Test method for {@link control.WorkflowEngine#getTargetGui()}.
	 */
	@Test
	final void testGetTargetGui() {
		LOGGER.info("testGetTargetGui()");

		final String TARGET_SYSTEM = "Test";
		
		try(
				
			MockedStatic<PlugInFactory> msPlugInFactory = mockStatic(PlugInFactory.class);
				
		)
		{
			
			msPlugInFactory.when( () -> PlugInFactory.getTargetSystemExtensionFactory(any()) )
				.thenReturn(targetSystemFactoryMock);

			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
			cut.registerCallback(mainWindowControllerMock);
				
			
			// test 1: no input extension set
			JPanel result = cut.getTargetGui();
			assertTrue(result instanceof EmptyExtensionGui);
			
			
			// test
			when(targetSystemFactoryMock.getTargetSystemExtension((StatusMessenger)cut)).thenReturn(targetSystemExtensionDao);
			when(targetSystemExtensionControllerMock.createLayout()).thenReturn(extensionGuiMock);
			cut.setTargetPlugin(TARGET_SYSTEM);

			result = cut.getTargetGui();
			
			assertTrue(result instanceof ExtensionGui);
			assertEquals(extensionGuiMock, result);
			
		} // yrt
		catch (Exception e) {
			
			LOGGER.error("Unexpected exception caught: {}", e);
			fail("Unexpected exception caught: " + e);
		}
		
	} // testGetTargetGui()


	/**
	 * Test method for {@link control.WorkflowEngine#getProtocol()}.
	 */
	@Test
	final void testGetProtocol() {
		LOGGER.info("testGetProtocol()");

		final String TARGET_SYSTEM = "Test";
		
		try(
				
				MockedStatic<PlugInFactory> msPlugInFactory = mockStatic(PlugInFactory.class);
					
			)
			{
			
				WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
				
				// test 1: no input extension set
				Protocol result = cut.getProtocol();
				assertTrue(result == null);
				
				
				// further setup
				cut.registerCallback(mainWindowControllerMock);

				when(fskUploaderModelMock.getTargetSystemProviderClassName(TARGET_SYSTEM)).thenReturn("tEST");
				
				msPlugInFactory.when( () -> PlugInFactory.getTargetSystemExtensionFactory(any()) )
					.thenReturn(targetSystemFactoryMock);
				
				when(targetSystemFactoryMock.getTargetSystemExtension((StatusMessenger)cut)).thenReturn(targetSystemExtensionDao);
				
				when(targetSystemExtensionControllerMock.createLayout()).thenReturn(extensionGuiMock);
				
				// test
				cut.setTargetPlugin(TARGET_SYSTEM);

				result = cut.getProtocol();
				assertEquals(protocolMock, result);
				
			} // yrt
			catch (Exception e) {
				
				LOGGER.error("Unexpected exception caught: {}", e);
				fail("Unexpected exception caught: " + e);
			}
		
	} // testGetProtocol()


	/**
	 * Test method for {@link control.WorkflowEngine#getSoundPlayer()}.
	 */
	@Test
	final void testGetSoundPlayer() {
		LOGGER.info("testGetSoundPlayer()");

		try(
		
			MockedConstruction<EnlistOutputDevices> mcEnlistOutputDevices = mockConstruction(EnlistOutputDevices.class);
			
		) {
			
			SoundPlayer soundPlayerMock = mock(SoundPlayer.class);
			OutputDeviceController controllerMock = mock(OutputDeviceController.class);
			when(controllerMock.getSoundPlayer()).thenReturn(soundPlayerMock);
			
			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);			
			cut.registerCallback(controllerMock);
			
			assertEquals(soundPlayerMock, cut.getSoundPlayer());
			
		} // yrt
		
	} // testGetSoundPlayer()


	/**
	 * Test method for {@link control.WorkflowEngine#getFileNameExtensionFilter()}.
	 */
	@Test
	final void testGetFileNameExtensionFilter() {
		LOGGER.info("testGetFileNameExtensionFilter()");

		final String TARGET_SYSTEM = "Test";
		
		InputReaderExtensionFactory inputReaderFactoryMock = mock(InputReaderExtensionFactory.class);
		MainWindowController mainWindowControllerMock = mock(MainWindowController.class);
		
		FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("foo", "bar", "baz");
				
		try(
			
			MockedStatic<PlugInFactory> msPlugInFactory = mockStatic(PlugInFactory.class);
				
		)
		{
		
			WorkflowEngine cut = new WorkflowEngine(fskUploaderModelMock);
			cut.registerCallback(mainWindowControllerMock);

			when(fskUploaderModelMock.getInputReaderProviderClassName(TARGET_SYSTEM)).thenReturn("tEST");
			
			msPlugInFactory.when( () -> PlugInFactory.getInputReaderExtensionFactory(any()) )
				.thenReturn(inputReaderFactoryMock);
			
			when(inputReaderFactoryMock.getInputReaderExtensions(cut)).thenReturn(inputReaderExtensionDao);
			
			when(readerMock.getFileNameExtensionFilter()).thenReturn(fileNameExtensionFilter);
			
			// test
			cut.setReaderPlugin(TARGET_SYSTEM);
			
			assertEquals(fileNameExtensionFilter, cut.getFileNameExtensionFilter());
			
		} // yrt
		catch (Exception e) {
			
			LOGGER.error("Unexpected exception caught: {}", e);
			fail("Unexpected exception caught: " + e);
		}
		
	} // testGetFileNameExtensionFilter()


} // ssalc
