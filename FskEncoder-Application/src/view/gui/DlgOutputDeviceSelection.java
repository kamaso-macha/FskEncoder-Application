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

import control.gui.OutputDeviceController;
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

public class DlgOutputDeviceSelection extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private Logger logger = LogManager.getLogger(DlgOutputDeviceSelection.class.getName());

	protected OutputDeviceController controller;

	private JComboBox<String> cbOutDevice;

	
	/**
	 * 
	 */
	public DlgOutputDeviceSelection(final OutputDeviceController aController) {
		logger.trace("DlgOutputDeviceSelection(): aController = {}", aController);
		
		if(aController == null) throw new IllegalArgumentException("aController can't be null");
		controller = aController;
		
		createDialog();
		
	} // DlgOutputDeviceSelection()
	
	
	protected void createDialog() {
		logger.trace("createDialog()");


		// Combo box
		String[] devices = controller.getOutputDeviceNames();
		String currentOutputDevice = controller.getOutputDeviceName();
		
		int index = 0;
		
		for(; index < devices.length; index++)
			if((devices[index]).equals(currentOutputDevice)) {
				break;
			}
		
		index = index < devices.length ? index : 0;
		
		cbOutDevice = new JComboBox<>(devices);
		cbOutDevice.setName("cbOutDevice");
		cbOutDevice.addActionListener(controller);		
		cbOutDevice.setSelectedIndex(index);
		
		// Buttons
		JButton	btnOk = new JButton("OK");
		btnOk.setName("btnOk");
		btnOk.addActionListener(controller);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setName("btnCancel");
		btnCancel.addActionListener(controller);
		

		// Dialog
		setTitle("Select Output Device");
		
		// Dlg Panel
		JPanel panel = new JPanel();
		
		panel.setLayout(new MigLayout("", "20 [150] 30 [150] 20", "20 [] 20 [] 20"));
		panel.add(cbOutDevice, "cell 0 0, growx, spanx");
		panel.add(btnOk, "cell 0 1, growx");
		panel.add(btnCancel, "cell 1 1, growx");
		
		add(panel);
		pack();
		setLocation(controller.getCurrentPosition());
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		
	} // createDialog()
	
	
	public String getSelectedDeviceName() { 
		logger.trace("cbOutDevice.getSelectedDeviceName(): {}", cbOutDevice.getSelectedItem());
		
		return (String) cbOutDevice.getSelectedItem(); 
		
	} // getSelectedDeviceName()

	
} // ssalc
