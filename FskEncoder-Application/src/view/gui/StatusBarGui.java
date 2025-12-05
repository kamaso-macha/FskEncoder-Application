/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : Gui.java
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


package view.gui;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import control.gui.StatusBarUpdate;
import net.miginfocom.swing.MigLayout;

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
// Created at 2024-05-08 17:26:55

public class StatusBarGui extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Logger logger = LogManager.getLogger(StatusBarGui.class.getName());
	
	protected StatusBarUpdate controller;
	
	protected JTextField txtStatusMessage;

	
	public StatusBarGui(StatusBarUpdate aController) {
		logger.trace("StatusBarGui(): aController = {}", aController);

		controller = aController;
		
		createComponents();
		createLayout();

	} // OutputDeviceGui()


	/**
	 * 
	 */
	protected void createLayout() {
		
		setLayout(new MigLayout("", "[430px]", "[]"));
		
		add(txtStatusMessage, "growx");

	} // createLayout()
	
	
	/**
	 * 
	 */
	protected void createComponents() {
		
		txtStatusMessage = new JTextField();
		txtStatusMessage.setName("txtStatusMessage");
		txtStatusMessage.setEditable(false);
		
	} // createComponents()
	
	
	public void setTxtStatusMessage(final String aMessage) { 
		logger.trace("setTxtStatusMessage(): aMessage = {}", aMessage);
		
		txtStatusMessage.setText(aMessage); 
		
	}


} // ssalc
