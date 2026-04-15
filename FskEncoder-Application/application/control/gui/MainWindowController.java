/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : MainWindowController.java
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


package control.gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import control.WorkflowEngine;
import model.MainWindowControllerModel;
import view.gui.MainWindow;

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
// Created at 2024-05-13 16:14:13

public class MainWindowController implements ActionListener{

	private Logger logger = LogManager.getLogger(MainWindowController.class.getName());

	protected static final String APPLICATION_TITLE = "FSK Uploader for ";

	protected MainWindow mainWindow;
	protected WorkflowEngine workflowEngine;
	protected MainWindowControllerModel model;

	
	/**
	 * @param aModel 
	 * @param statusMessenger
	 */
	public MainWindowController(WorkflowEngine aWorkflowEngine, MainWindowControllerModel aModel) {
		logger.trace("MainWindowController(): aWorkflowEngine = {}, aModel = {}", aWorkflowEngine, aModel);

		workflowEngine = aWorkflowEngine;
		model = aModel;
		
		workflowEngine.registerCallback(this);
		
		mainWindow = new MainWindow(this);
		mainWindow.setAutosaveFlag(model.getAutosaveFlag());

	} // MainWindowController()


	/**
	 * @param location
	 */
	public void setCurrentPosition(Point aLocation) {
		logger.trace("setCurrentPosition()");
		
		model.setCurrentPosition(aLocation);
		
	} // setCurrentPosition()


	/**
	 * @param aReaderExtensionPanel
	 */
	public void setReaderExtensionPanel(JPanel aReaderExtensionPanel) {
		logger.trace("setReaderExtensionPanel(): aGuiExtension = {}", aReaderExtensionPanel);
		
		mainWindow.setReaderExtensionPanel(aReaderExtensionPanel);
		
	} // setReaderExtensionPanel()
	
	
	public void setTitle() {
		logger.trace("setTitle()");
		
		String targetSystemName = APPLICATION_TITLE +  model.getTargetSystemName();
		
		if(targetSystemName.endsWith("Not defined")) {
			targetSystemName = targetSystemName.replace(" for Not defined", ": No Target System set");
			workflowEngine.setStatusMessage("Please select a target system");
		}
		
		mainWindow.setTitle(targetSystemName);
		
	} // setTitle()


	/**
	 * @param aTargetExtensionPanel
	 */
	public void setTargetExtensionPanel(JPanel aTargetExtensionPanel) {
		logger.trace("setTargetExtensionPanel(): aTargetExtensionPanel = {}", aTargetExtensionPanel);
		
		mainWindow.setTargetExtensionPanel(aTargetExtensionPanel);
		
	} // setTargetExtensionPanel()


	/**
	 * @return
	 */
	public JPanel getMainPanel() {
		logger.trace("getMainPanel(): mainWindow = {}", mainWindow);
		
		return mainWindow.getMainPanel();
		
	} // setTargetExtensionPanel()


	/**
	 * @return
	 */
	public MainWindow getMainWindow() {
		logger.trace("getMainWindow()");
		
		return mainWindow;
		
	} // getMainWindow()


	/**
	 * 
	 */
	public void saveSetings() {
		logger.trace("saveSetings()");
		
		model.saveSetings();
		
	} // saveSetings()


	/**
	 * @return
	 */
	public Point getCurrentPosition() {
		logger.trace("getCurrentPosition()");
		
		return model.getCurrentPosition();
		
	} // getCurrentPosition()


	@Override
	public void actionPerformed(ActionEvent e) {
		logger.trace("actionPerformed(): e = {}", e);
		
		String cmd = e.getActionCommand();
		
		if(cmd.equals("Automatically save on exit")) {
			
			boolean state = mainWindow.getAutosaveMode();
			model.setAutosaveFlag(state);
			mainWindow.setAutosaveFlag(state);
			
		}
		else {
		} // esle

	} // actionPerformed()


	/**
	 * @return
	 */
	public boolean getAutosaveFlag() {
		logger.trace("getAutosaveFlag()");
		
		return model.getAutosaveFlag();
		
	} // getAutosaveFlag()


} // ssalc
