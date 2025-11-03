/**
 *
 * **********************************************************************
 * PROJECT       : ScratchPad
 * FILENAME      : Application.java
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


package application;

import java.awt.EventQueue;
import java.util.Locale;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import control.WorkflowEngine;
import control.gui.CompileAndUploadAction;
import control.gui.InputFileController;
import control.gui.MainWindowController;
import control.gui.OutputDeviceController;
import control.gui.StatusBarUpdate;
import control.gui.TargetSystemSelection;
import model.FskUploaderModel;
import model.InputFileControllerModel;
import model.MainWindowControllerModel;
import model.OutputDeviceControllerModel;
import model.TargetSystemSelectionModel;
import view.gui.MainWindow;

/**
 * Responsibilities:<br>
 * Create the application window, the data model and all controllers 
 * needed for the GUI and workflow management. 
 * 
 * <p>
 * Collaborators:<br>
 * None
 * 
 * 
 * <p>
 * Description:<br>
 * Creates the business logic and the application window.
 * <br>
 * Note: The application language is fixed to English!
 * 
 * <p>
 * @author Stefan
 *
 */

public class Application {

	// Log4J2 config file is set via VM argument!
	private static Logger logger = LogManager.getLogger(Application.class.getName());
	protected MainWindow mainWindow;
	

	/**
	 * 
	 * Constructor.
	 * 
	 * Set the locale to EN, en and invokes the initialization of the application.
	 * 
	 */
	public Application() {
		
		logger.trace("Application()");
		
		Locale.setDefault(new Locale("en", "EN"));
		initialize();
		
	} // Application()


	/**
	 * 
	 * Does the initialization.
	 * <br>
	 * Prepares the business logic, the main window and it's main panel 
	 * and all controllers fur the different GUI areas.<br>
	 * After preparation of the required components it puts all together.
	 * 
	 */
	protected void initialize() {
		logger.trace("initialize()");

		FskUploaderModel model = new FskUploaderModel();
		WorkflowEngine workflowEngine = new WorkflowEngine(model);
		
		MainWindowController mainWindowController = new MainWindowController(workflowEngine, (MainWindowControllerModel) model);
		mainWindow = mainWindowController.getMainWindow();
		JPanel mainPanel = mainWindowController.getMainPanel();		

		StatusBarUpdate statusBarUpdate = new StatusBarUpdate(workflowEngine);

		workflowEngine.setTargetSystem(null);

		OutputDeviceController outputDeviceController = new OutputDeviceController(workflowEngine, (OutputDeviceControllerModel) model);	
		InputFileController inputFileController = new InputFileController(workflowEngine, (InputFileControllerModel) model, mainPanel);
		CompileAndUploadAction compileAndUploadAction = new CompileAndUploadAction(workflowEngine);
		TargetSystemSelection targetSystemSelection = new TargetSystemSelection(workflowEngine, (TargetSystemSelectionModel) model);
		
		mainWindowController.setTitle();
		mainWindow.setMnuTargetSystemController(targetSystemSelection);
		mainWindow.setMnuOutputDeviceController(outputDeviceController);
		mainWindow.setMnuDefaultPathController(inputFileController);		
		
		mainPanel.add(outputDeviceController.getGui());
		mainPanel.add(inputFileController.getFileSelectGui());
		mainPanel.add(inputFileController.getFileLoadGui());

		mainWindow.setExtensionPanels();

		mainPanel.add(workflowEngine.getReaderGui());
		mainPanel.add(workflowEngine.getTargetGui());
		mainPanel.add(compileAndUploadAction.getGui());
		mainPanel.add(statusBarUpdate.getGui());
		
		mainWindow.pack();
		
	} // initialize()
	
	
	/**
	 * 
	 * Launch the application.
	 * 
	 */
	public static void main(String[] args) {	// NOSONAR
		logger.trace("main(...)");
		
    	@SuppressWarnings("java:S2440")
        CliParameter cli = new CliParameter();				// NOSONAR
        CmdLineParser cliParser = new CmdLineParser(cli);	// NOSONAR
        
        try {
        	
			cliParser.parseArgument(args);
			
			String baseDirectory = System.getProperty("user.dir").replace("\\", "/");
			String configPath = baseDirectory + "/" + CliParameter.cfgPath;		// NOSONAR
			ApplicationResources.setPath(configPath);
			
		} catch (CmdLineException e) {
			
			System.err.println("Cli option '-c <property file> not set, giving up!");	// NOSONAR
			System.exit(ExitCodes.EINVAL);
			
		} // yrt

		
//		logger.trace("Current class_path: -> {}",
//				new ClassGraph().scan()
//					.getClasspath()
//				);
//
//		for (Entry<String, ResourceList> dup :
//	        new ClassGraph().scan()
//	            .getAllResources()
////	            .classFilesOnly()                        // Remove this for all resource types
//	            .findDuplicatePaths()
//	        ) {
//			
//			logger.trace(dup.getKey());                // Classfile path
//		    
//			for (Resource res : dup.getValue()) {
//		    	logger.trace(" -> {}", res.getURI());   // Print Resource URI
//		    }
//		}		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				logger.trace("Runnable.run()");
				
				try {
					
					new Application().mainWindow.setVisible(true);
					
				} catch (Exception e) {
					logger.fatal("Unexpected exception caught: {}", e);
					e.printStackTrace();
				}
			}
		});
		
		System.exit(ExitCodes.EOK);
		
	} // main(...)
	
	
} // ssalc
