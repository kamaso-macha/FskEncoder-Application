/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : ApplicationTest.java
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


package application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
//import static org.mockito.Mockito.mockConstructionWithAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.MockedConstruction;

import control.WorkflowEngine;
import control.gui.CompileAndUploadAction;
import control.gui.InputFileController;
import control.gui.MainWindowController;
import control.gui.OutputDeviceController;
import control.gui.StatusBarUpdate;
import control.gui.TargetSystemSelection;
import model.FskUploaderModel;
import view.gui.MainWindow;

/**
 * Responsibilities:<br>
 * Test the functionality of the class Application.
 * 
 * <p>
 * Collaborators:<br>
 * None.
 * 
 * <p>
 * Description:<br>
 * Verifies that the initialization of the application is correct.
 * 
 * <p>
 * @author Stefan
 *
 */

class ApplicationTest {

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
	 * Test method for {@link application.Application#Application()}.
	 * 
	 * Verifies that the application is initialized correctly.
	 * 
	 */
	@Test
	final void testApplication() {
		LOGGER.info("testApplication()");

		MainWindow mainWindowMock = mock(MainWindow.class);
		InOrder mwSequence = inOrder(mainWindowMock);

		JPanel mainPanelMock = mock(JPanel.class);
		InOrder mpSequence = inOrder(mainPanelMock);

		JPanel outputDeviceControllerPanelMock = mock(JPanel.class);
		JPanel inputFileControllerFileSelectionPanelMock = mock(JPanel.class);
		JPanel inputFileControllerFileLoadPanelMock = mock(JPanel.class);
		JPanel readerPanelMock = mock(JPanel.class);
		JPanel targetPanelMock = mock(JPanel.class);
		JPanel compileAndUploadPanelMock = mock(JPanel.class);
		JPanel statusBarUpdatePanelMock = mock(JPanel.class);
		
		try(
		
			MockedConstruction<FskUploaderModel> mcFskUploaderModel	= mockConstruction(FskUploaderModel.class);
			
			MockedConstruction<WorkflowEngine> mcWorkflowEngine = mockConstruction(WorkflowEngine.class,
				(mock, contxt) -> {
					
					when(mock.getReaderGui()).thenReturn(readerPanelMock);
					when(mock.getTargetGui()).thenReturn(targetPanelMock);
					
				}
			);
			
			MockedConstruction<MainWindowController> mcMainWindowController	= mockConstruction(MainWindowController.class,
				(mock, contxt) -> {
					
					when(mock.getMainWindow()).thenReturn(mainWindowMock);
					when(mock.getMainPanel()).thenReturn(mainPanelMock);
					
				}
			);
				
			MockedConstruction<StatusBarUpdate>	mcStatusBarUpdate = mockConstruction(StatusBarUpdate.class,
				(mock, context) -> {
					
					when(mock.getGui()).thenReturn(statusBarUpdatePanelMock);
					
				}
			);
					
			
			MockedConstruction<OutputDeviceController> mcOutputDeviceController	= mockConstruction(OutputDeviceController.class,
				(mock, context) -> {
					
					when(mock.getGui()).thenReturn(outputDeviceControllerPanelMock);
					
				}
			);
			
			MockedConstruction<InputFileController> mcInputFileController = mockConstruction(InputFileController.class,
				(mock, context) -> {
					
					when(mock.getFileSelectGui()).thenReturn(inputFileControllerFileSelectionPanelMock);
					when(mock.getFileLoadGui()).thenReturn(inputFileControllerFileLoadPanelMock);
					
				}
			);
			
			MockedConstruction<CompileAndUploadAction> mcCompileAndUploadAction	= mockConstruction(CompileAndUploadAction.class,
				(mock, context) -> {
					
					when(mock.getGui()).thenReturn(compileAndUploadPanelMock);
					
				}
			);
			
			MockedConstruction<TargetSystemSelection> mcTargetSystemSelection = mockConstruction(TargetSystemSelection.class);
				
		) // yrt()
		{
			
			@SuppressWarnings("unused")
			Application cut = new Application();
			LOGGER.info("Application initialized");
			
			// verify construction of mocks
			assertEquals(1,  mcFskUploaderModel.constructed().size());
			assertEquals(1,  mcWorkflowEngine.constructed().size());
			assertEquals(1,  mcMainWindowController.constructed().size());

			assertEquals(1,  mcStatusBarUpdate.constructed().size());

			assertEquals(1,  mcOutputDeviceController.constructed().size());
			assertEquals(1,  mcInputFileController.constructed().size());
			assertEquals(1,  mcCompileAndUploadAction.constructed().size());
			assertEquals(1,  mcTargetSystemSelection.constructed().size());
			
			
			// create mocks
			WorkflowEngine workflowEngineMock = mcWorkflowEngine.constructed().get(0);
			InOrder wfeSequence = inOrder(workflowEngineMock);
			
			MainWindowController mainWindowControllerMock = mcMainWindowController.constructed().get(0);
			InOrder mwcSequence = inOrder(mainWindowControllerMock);

			OutputDeviceController outpuDeviceControllerMock = mcOutputDeviceController.constructed().get(0);
			
			InputFileController inputFileControllerMock = mcInputFileController.constructed().get(0);
			
			CompileAndUploadAction compileAndUploadActionMock = mcCompileAndUploadAction.constructed().get(0);
			
			TargetSystemSelection targetSystemSelectionMock = mcTargetSystemSelection.constructed().get(0);
			
			StatusBarUpdate statusBarUpdateMock = mcStatusBarUpdate.constructed().get(0);
			
			
			// verify order of invocation of methods
			mwcSequence.verify(mainWindowControllerMock, times(1)).getMainWindow();
			mwcSequence.verify(mainWindowControllerMock, times(1)).getMainPanel();
			
			wfeSequence.verify(workflowEngineMock, times(1)).setTargetSystem(null);
		
			mwcSequence.verify(mainWindowControllerMock, times(1)).setTitle();
		
			mwSequence.verify(mainWindowMock, times(1)).setMnuTargetSystemController(targetSystemSelectionMock);
			mwSequence.verify(mainWindowMock, times(1)).setMnuOutputDeviceController(outpuDeviceControllerMock);
			mwSequence.verify(mainWindowMock, times(1)).setMnuDefaultPathController(inputFileControllerMock);
			
			verify(outpuDeviceControllerMock, times(1)).getGui();
			mpSequence.verify(mainPanelMock, times(1)).add(outputDeviceControllerPanelMock);

			verify(inputFileControllerMock, times(1)).getFileSelectGui();
			mpSequence.verify(mainPanelMock, times(1)).add(inputFileControllerFileSelectionPanelMock);

			verify(inputFileControllerMock, times(1)).getFileLoadGui();
			mpSequence.verify(mainPanelMock, times(1)).add(inputFileControllerFileLoadPanelMock);
			
			mwSequence.verify(mainWindowMock, times(1)).setExtensionPanels();

			wfeSequence.verify(workflowEngineMock, times(1)).getReaderGui();
			mpSequence.verify(mainPanelMock, times(1)).add(readerPanelMock);

			wfeSequence.verify(workflowEngineMock, times(1)).getTargetGui();
			mpSequence.verify(mainPanelMock, times(1)).add(targetPanelMock);

			verify(compileAndUploadActionMock, times(1)).getGui();
			mpSequence.verify(mainPanelMock, times(1)).add(compileAndUploadPanelMock);

			verify(statusBarUpdateMock, times(1)).getGui();
			mpSequence.verify(mainPanelMock, times(1)).add(statusBarUpdatePanelMock);

			mwSequence.verify(mainWindowMock, times(1)).pack();

		} // yrt
		
	} // testApplication()
	

	/**
	 * Test method for {@link application.Application#main(java.lang.String[])}.
	 */
	@Test
	final void testMain() {
		LOGGER.info("testMain()");

		/*
		 * TODO: No plan how to verify this! 
		 * 
		 */
		
		assertTrue(true);			
				
	} // testMain()

	
} // ssalc
