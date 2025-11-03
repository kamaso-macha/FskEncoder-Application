/**
 *
 * **********************************************************************
 * PROJECT       : ScratchPad
 * FILENAME      : InputFileGui.java
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


package view.gui;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import control.gui.InputFileController;
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
// Created at 2024-05-09 10:15:17

public class InputFileLoadGui extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Logger logger = LogManager.getLogger(InputFileLoadGui.class.getName());

	protected InputFileController controller;

	protected JButton btnLoadFile;
	protected JTextField txtLastUploadTime;
	protected JTextField txtFileStamp;
	protected Component lblLastUploadTime;
	protected JLabel lblFileStamp;


	
	public InputFileLoadGui(InputFileController aController) {
		logger.trace("InputFileLoadGui(): aController = {}", aController);

		controller = aController;
		
		createComponents();
		createLayout();

	} // InputFileGui()
	
	
	protected void createLayout() {
		
		setLayout(new MigLayout("", "[100px,left] 20 [150px][160px]", "[][]20[]20[]"));	

		add(lblFileStamp, "cell 0 0");
		add(txtFileStamp, "cell 1 0,grow");
		
		add(lblLastUploadTime, "cell 0 1");
		add(txtLastUploadTime, "cell 1 1,grow");
		
		add(btnLoadFile, "cell 0 2,grow,,width ::100px");

		add(new JSeparator(), "cell 0 3 3 1,grow");
		
	} // createLayout()

	
	protected void createComponents() {
		
		lblFileStamp = new JLabel("File stamp");

		txtFileStamp = new JTextField();
		txtFileStamp.setName("txtFileStamp");
		txtFileStamp.setColumns(10);
		txtFileStamp.setHorizontalAlignment(SwingConstants.RIGHT);
		txtFileStamp.setEditable(false);
		txtFileStamp.setFocusable(false);
		txtFileStamp.setFocusTraversalKeysEnabled(false);

		lblLastUploadTime = new JLabel("Last upload");

		txtLastUploadTime = new JTextField();
		txtLastUploadTime.setName("txtLastUploadTime");
		txtLastUploadTime.setHorizontalAlignment(SwingConstants.RIGHT);
		txtLastUploadTime.setColumns(10);
		txtLastUploadTime.setEditable(false);
		txtLastUploadTime.setFocusable(false);
		txtLastUploadTime.setFocusTraversalKeysEnabled(false);

		btnLoadFile = new JButton("Load File");
		btnLoadFile.setName("btnLoadFile");
		btnLoadFile.addActionListener(controller);
		
	} // createComponents()

	
	public void setTxtFileStamp(final String aFileStamp) { txtFileStamp.setText(aFileStamp); }
	public void setTxtLastUploadTime(final String aLastUploadTime) { txtLastUploadTime.setText(aLastUploadTime); }


	/**
	 * @param aTimeStamp
	 */
	public void setLastUploadTime(String aTimeStamp) {
		logger.trace("setLastUploadTime()");
		
		txtLastUploadTime.setText(aTimeStamp);
		
	} // setLastUploadTime()
	
	
} // ssalc
