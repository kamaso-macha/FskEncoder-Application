/**
 *
 * **********************************************************************
 * PROJECT       : ScratchPad
 * FILENAME      : Gui.java
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
import javax.swing.JProgressBar;
import javax.swing.JSeparator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import control.gui.CompileAndUploadAction;
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

public class CompileAndUploadGui extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Logger logger = LogManager.getLogger(CompileAndUploadGui.class.getName());
	
	private CompileAndUploadAction controller;

	private JProgressBar progressBar;
	private JButton btnCompileAndUpload;
	private JButton btnAbort;



	
	public CompileAndUploadGui(CompileAndUploadAction aController) {
		logger.trace("CompileAndUploadGui(): aController = {}", aController);

		if(aController == null) throw new IllegalArgumentException("aController can't be null");
		controller = aController;
		
		createComponents();
		createLayout();

	} // OutputDeviceGui()


	/**
	 * 
	 */
	private void createLayout() {
		
		// 3 cols x 3 rows
		setLayout(new MigLayout("", "[430px, center]", "[] 20 [] 20 []"));
		
		add(progressBar, "growx, spanx");

		add(btnCompileAndUpload,  "cell 0 1, gapright 50");
		add(btnAbort, "cell 0 1, wrap");

		add(new JSeparator(), "cell 0 2, grow");

	} // createLayout()
	
	
	/**
	 * 
	 */
	private void createComponents() {

		progressBar = new JProgressBar(0, 100);
		progressBar.setName("progressBar");
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		btnCompileAndUpload = new JButton("Do Upload");
		btnCompileAndUpload.setName("btnCompileAndUpload");
		btnCompileAndUpload.addActionListener(controller);

		btnAbort = new JButton("Abort");
		btnAbort.setName("btnAbort");
		btnAbort.addActionListener(controller);

	} // createComponents()


	public void setProgress(final int aProgress) { progressBar.setValue(aProgress); }
	
	
} // ssalc
