/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : StatusBarController.java
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

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
// Created at 2024-05-10 09:36:00

public class StatusBarUpdate {

	private Logger logger = LogManager.getLogger(StatusBarUpdate.class.getName());
	private StatusBarGui statusBarPanel;
	
	
	/**
	 * @param workFlowEngine 
	 * @param statusBarGui
	 */
	public StatusBarUpdate(WorkflowEngine aWorkflowEngine) {
		logger.trace("StatusBarUpdate(): aWorkFlowEngine = {}", aWorkflowEngine);
		
		if(aWorkflowEngine == null)	throw new IllegalArgumentException("aWorkflowEngine can't be null!");

		aWorkflowEngine.registerCallback(this);
		statusBarPanel = new StatusBarGui(this);

	} // InputFileSelection()
	

	/**
	 * @param string
	 */
	public void setStatusMessage(String aStatusMessage) {
		logger.trace("setStatusMessage(): aStatusMessage = {}", aStatusMessage);
		
		statusBarPanel.setTxtStatusMessage(aStatusMessage);
		
	} // setStatusMessage()


	/**
	 * @return
	 */
	public JPanel getGui() {
		logger.trace("getGui()");
		
		return statusBarPanel;
		
	} // getGui()


} // ssalc
