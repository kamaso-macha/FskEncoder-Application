/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : TxtOutputVolumeValidator.java
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


package control.validator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.management.InvalidAttributeValueException;
import javax.swing.InputVerifier;
import javax.swing.JComponent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import view.gui.OutputDeviceGui;

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
// Created at 2025-10-23 15:17:51

public class TxtOutputVolumeValidator extends InputVerifier implements ActionListener {
	
	private Logger logger = LogManager.getLogger(TxtOutputVolumeValidator.class.getName());

	protected static final int MIN_VALUE = 0;
	protected static final int MAX_VALUE = 100;
	
	protected OutputDeviceGui gui;

	/**
	 * @throws InvalidAttributeValueException 
	 * 
	 */
	public TxtOutputVolumeValidator(OutputDeviceGui aGui) throws InvalidAttributeValueException {
		logger.trace("TxtOutputVolumeValidator()");
		
		if(aGui == null) throw new InvalidAttributeValueException("aGui can't be null!");
		
		gui = aGui;
		
	} // TxtOutputVolumeValidator()
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		logger.trace("actionPerformed(): e = {}", e);
		
		
	} // actionPerformed(...)

	
	@Override
	public boolean verify(JComponent input) {
		logger.trace("verify(): input: {}", input);
		
		String value = gui.getTxtOutputVolume();	
		int volume = Integer.parseInt(value);

		logger.trace("volume: {}", volume);
		
        //Value was invalid.
        if ((volume < MIN_VALUE) || (volume > MAX_VALUE)) {
            
            if (volume < MIN_VALUE) {
                volume = MIN_VALUE;
            } else { // amount is greater than MAX_AMOUNT
                volume = MAX_VALUE;
            }
            
            gui.setTxtOutputVolume(volume);
            
        } // range
        
        return true;

	} // verify(...)

	
	
} // ssalc
