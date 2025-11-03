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

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

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

public class InputFileSelectionGui extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Logger logger = LogManager.getLogger(InputFileSelectionGui.class.getName());

	protected InputFileController controller;

	protected JButton btnSelectFile;
	protected JTextField txtSelectedFilePath;


	public InputFileSelectionGui(InputFileController aController) {
		logger.trace("InputFileSelectionGui(): aController = {}", aController);

		controller = aController;
		
		createComponents();
		createLayout();

	} // InputFileGui()
	
	
	protected void createLayout() {
		
		setLayout(new MigLayout("", "[100px,left] 20 [310]", "[]10[]"));

		add(btnSelectFile, "cell 0 0,grow,,width ::100px");
		add(txtSelectedFilePath, "cell 1 0 1 1,growx");
		add(new JSeparator(), "cell 0 3 2 1,growx");
		
	} // createLayout()

	
	protected void createComponents() {
				
		btnSelectFile = new JButton("Select File");
		btnSelectFile.setName("btnSelectFile");
//		btnSelectFile.setFocusTraversalPolicyProvider(true);
		btnSelectFile.addActionListener(controller);
		
		txtSelectedFilePath = new JTextField();
		txtSelectedFilePath.setName("txtSelectedFilePath");
		txtSelectedFilePath.setColumns(10);
		txtSelectedFilePath.setEditable(false);
		txtSelectedFilePath.setFocusable(false);
		txtSelectedFilePath.setFocusTraversalKeysEnabled(false);

	} // createComponents()

	
	public void setTxtSelectedFilePath(final String afilePath) { txtSelectedFilePath.setText(afilePath); }

	
} // ssalc
