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

import javax.management.InvalidAttributeValueException;
import javax.swing.InputVerifier;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import control.gui.OutputDeviceController;
import control.validator.TxtOutputVolumeValidator;
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

public class OutputDeviceGui extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Logger logger = LogManager.getLogger(OutputDeviceGui.class.getName());
	
	protected OutputDeviceController controller;

	protected JLabel lblOutputDevice;
	protected JTextField txtOutputDevice;
	protected JLabel lblOutputVolume;
	protected JSlider slOutputVolume;
	protected JTextField txtOutputVolume;
	
	protected InputVerifier txtOutputVolumeVerifier;


	
	public OutputDeviceGui( OutputDeviceController aController) throws InvalidAttributeValueException {
		logger.trace("OutputDeviceGui(): aOutputDeviceSelection = {}", aController);

		controller = aController;

		createComponents();
		createLayout();

	} // OutputDeviceGui()


	/**
	 * 
	 */
	protected void createLayout() {
		
		// 3 cols x 3 rows
		setLayout(new MigLayout("", "[100px,left] 20 [265] 15 [30px]", "[]20[]10[]"));
		
		add(lblOutputDevice, "cell 0 0,growx");
		add(txtOutputDevice, "cell 1 0 2 1,growx");
		
		add(lblOutputVolume, "cell 0 1,growx");
		add(slOutputVolume, "cell 1 1,growx");
		
		add(txtOutputVolume, "cell 2 1,growx");
		
		add(new JSeparator(), "cell 0 3 3 1,growx");

	} // createLayout()
	
	
	/**
	 * @throws InvalidAttributeValueException 
	 * 
	 */
	protected void createComponents() throws InvalidAttributeValueException {

		lblOutputDevice = new JLabel("Output device");
		
		txtOutputDevice = new JTextField(0);
		txtOutputDevice.setEditable(false);
		txtOutputDevice.setName("txtOutputDevice");
		
		lblOutputVolume = new JLabel("Output volume");
		
		slOutputVolume = new JSlider(0, 100);
		slOutputVolume.setName("slOutputVolume");
		slOutputVolume.setMajorTickSpacing(10);
		slOutputVolume.setMinorTickSpacing(5);
		slOutputVolume.setPaintTicks(true);
		slOutputVolume.setPaintLabels(true);
		slOutputVolume.addChangeListener(controller);
		
		slOutputVolume.setFocusTraversalKeysEnabled(false);
		
		txtOutputVolume = new JTextField();
		txtOutputVolume.setName("txtOutputVolume");
		txtOutputVolume.setHorizontalAlignment(SwingConstants.RIGHT);
		txtOutputVolume.setColumns(10);
		txtOutputVolume.addFocusListener(controller);
		txtOutputVolume.addActionListener(controller);
		
		txtOutputVolumeVerifier = new TxtOutputVolumeValidator(this);
		txtOutputVolume.setInputVerifier(txtOutputVolumeVerifier);
		
//		txtOutputVolume.setFocusTraversalKeysEnabled(false);
		
	} // createComponents()


	public int getSlOutputVolume() { return slOutputVolume.getValue(); }
	public String getTxtOutputVolume() { return txtOutputVolume.getText(); }
	
	
	public void setSlOutputVolume(final int aOutputVolume) { 
	
		txtOutputVolumeVerifier.verify(txtOutputVolume);
		slOutputVolume.setValue(aOutputVolume); 
	
	}

	public void setTxtOutputDevice(final String aOutputDevice) { txtOutputDevice.setText(aOutputDevice); }
	
	public void setTxtOutputVolume(final int value) { 
		logger.trace("setTxtOutputVolume(): value = {}", value);
		
		txtOutputVolume.setText(Integer.toString(value)); 
	}

	
} // ssalc
