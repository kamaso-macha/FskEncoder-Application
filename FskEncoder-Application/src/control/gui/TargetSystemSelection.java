/**
 *
 * **********************************************************************
 * PROJECT       : ScratchPad
 * FILENAME      : TargetController.java
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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
// Created at 2024-05-09 09:47:31

public class TargetSystemSelection implements ActionListener {

	private Logger logger = LogManager.getLogger(TargetSystemSelection.class.getName());
	
	protected JDialog dialog;

	protected WorkflowEngine workflowEngine;
	protected TargetSystemSelectionModel model;
	
	
	/**
	 * @param model 
	 * @param workFlowEngine
	 */
	public TargetSystemSelection(WorkflowEngine aWorkflowEngine, TargetSystemSelectionModel aModel) {
		logger.trace("MainWindowController(): aWorkflowEngine = {}, aModel = {}", aWorkflowEngine, aModel);

		if(aWorkflowEngine == null)	throw new IllegalArgumentException("aWorkflowEngine can't be null!");
		if(aModel == null)			throw new IllegalArgumentException("aModel can't be null!");
		
		workflowEngine = aWorkflowEngine;
		model = aModel;
		
	} // TargetSystemSelection()
	

	@Override
	public void actionPerformed(ActionEvent e) {
		logger.trace("actionPerformed(): e.paramString = {}", e.paramString());
		
		if(e.getActionCommand().contentEquals("Target System")) {
			setTargetSystem();
		}
		else if(e.getActionCommand().contentEquals("comboBoxChanged")) {
			handleComboBoxEvent();
		}
		else if(e.getActionCommand().contentEquals("OK")) {
			handleTargetSystemOKEvent();
		}
		else if(e.getActionCommand().contentEquals("Cancel")) {
			handleDlgTargetSystemCancelEvent();
		}
		else {
			throw new IllegalAccessError("Unknown Action Command!");
		}

	} // actionPerformed()


	public void setTargetSystem() {
		logger.trace("setTargetSystem()");
		
		dialog = new DlgTargetSystemSelection(this);
		dialog.setVisible(true);
				
	} // setTargetSystem()


	protected void handleDlgTargetSystemCancelEvent() {
		logger.trace("handleDlgTargetSystemCancelEvent()");
		
		dialog.setVisible(false);
		dialog.dispose();
		
	} // handleDlgTargetSystemCancelEvent()


	protected void handleTargetSystemOKEvent() {
		logger.trace("handleTargetSystemOKEvent()");

		String name = ((DlgTargetSystemSelection) dialog).getSelectedDeviceName();
		logger.trace("dialog: {}, selected device: {}", dialog, name);
		
		workflowEngine.setTargetSystem(name);
		
		handleDlgTargetSystemCancelEvent();
		
	} // handleTargetSystemOKEvent()


	protected void handleComboBoxEvent() {
		logger.trace("handleComboBoxEvent()");
		
		// TODO
		
	} // handleComboBoxEvent()


	/**
	 * @return
	 */
	public Point getCurrentPosition() {
		logger.trace("getCurrentPosition()");
		
		return new Point(100, 100);
		
	} // getCurrentPosition()


	/**
	 * @return
	 */
	public String[] getTargetSystemNames() {
		logger.trace("getTargetSystemNames()");
		
		return model.getTargetSystemNames();

	} // getTargetSystemNames()


	/**
	 * @return
	 */
	public String getTargetSystemName() {
		logger.trace("getTargetSystemName()");
		
		return model.getTargetSystemName();
		
	} // getTargetSystemName()

	
} // ssalc
