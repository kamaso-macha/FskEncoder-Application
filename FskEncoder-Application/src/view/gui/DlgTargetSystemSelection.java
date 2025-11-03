/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : DlgOutputDeviceSelection.java
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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import control.gui.TargetSystemSelection;
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
// Created at 2024-05-11 09:31:38

public class DlgTargetSystemSelection extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private Logger logger = LogManager.getLogger(DlgTargetSystemSelection.class.getName());

	protected TargetSystemSelection controller;

	private JComboBox<String> cbTargetsystem;

	
	/**
	 * 
	 */
	public DlgTargetSystemSelection(final TargetSystemSelection aController) {
		logger.trace("DlgTargetSystemSelection()");
		
		if(aController == null) throw new IllegalArgumentException("aController can't be null");
		controller = aController;
		
		createDialog();
		
	} // DlgTargetSystemSelection()
	
	
	protected void createDialog() {
		logger.trace("createDialog()");


		// Combo box
		String[] targets = controller.getTargetSystemNames();
		String currentTarget = controller.getTargetSystemName();
		
		int index = 0;
		
		for(; index < targets.length; index++)
			if((targets[index]).equals(currentTarget)) {
				break;
			}
		
		index = index < targets.length ? index : 0;
		
		cbTargetsystem = new JComboBox<>(targets);
		cbTargetsystem.setName("cbOutDevice");
		cbTargetsystem.addActionListener(controller);		
		cbTargetsystem.setSelectedIndex(index);
		
		// Buttons
		JButton	btnOk = new JButton("OK");
		btnOk.setName("btnOk");
		btnOk.addActionListener(controller);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setName("btnCancel");
		btnCancel.addActionListener(controller);
		

		// Dialog
		setTitle("Select Target System");
		
		// Dlg Panel
		JPanel panel = new JPanel();
		
		panel.setLayout(new MigLayout("", "20 [150] 30 [150] 20", "20 [] 20 [] 20"));
		panel.add(cbTargetsystem, "cell 0 0, growx, spanx");
		panel.add(btnOk, "cell 0 1, growx");
		panel.add(btnCancel, "cell 1 1, growx");
		
		add(panel);
		pack();
		setLocation(controller.getCurrentPosition());
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		
	} // createDialog()
	
	
	/**
	 * @return
	 */
	public String getSelectedDeviceName() {
		
		return (String) cbTargetsystem.getSelectedItem();
	}

	
} // ssalc
