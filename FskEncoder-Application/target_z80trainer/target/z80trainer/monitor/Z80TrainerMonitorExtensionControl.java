/**
 *
 * **********************************************************************
 * PROJECT       : ScratchPad
 * FILENAME      : InputFileLoadController.java
 *
 * More information about this project can be found on Github
 * http://github.com/kamaso-macha/FskEncoder-Extensions
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


package target.z80trainer.monitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.control.StatusMessenger;
import target.z80trainer.Z80TrainerExtensionControlBase;
import target.z80trainer.Z80TrainerGuiType;


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
// Created at 2024-05-09 12:53:57

public class Z80TrainerMonitorExtensionControl  extends Z80TrainerExtensionControlBase {

	private Logger logger = LogManager.getLogger(Z80TrainerMonitorExtensionControl.class.getName());

	
	protected static final String DEFAULT_MONITOR_PROGRAM_NUMBER = "0x0001";
	
	/**
	 * @param aWorkFlowEngine
	 */
	public Z80TrainerMonitorExtensionControl(StatusMessenger aStatusMessenger) {
		super(aStatusMessenger, Z80TrainerGuiType.MONITOR_TYPE, DEFAULT_MONITOR_PROGRAM_NUMBER);
		
		logger.trace("Z80TrainerMonitorExtensionControl(): StatusMessenger {}", aStatusMessenger);
		
		protocol = new Z80TrainerMonitorProtocol();
		setProgramNumber();
		
		logger.debug("protocol: {}", protocol);
		
	} // Z80TrainerExtensionControl()
	

	/**
	 * 
	 */
	public void setProgramNumber() {
		logger.trace("setProgramNumber()");
		
		String txtProgramNumber = gui.getTxtProgramNumberText().replaceAll("^0x", ""); 
		logger.trace("txtProgramNumber = {}", txtProgramNumber);
		
		int programNumber = Integer.parseInt(txtProgramNumber, 16);
		logger.trace("txtProgramNumber = {}, programNumber = {}", txtProgramNumber, programNumber);
		
		protocol.setProgramNbr(programNumber);
		
	} // setProgramNumber()
	

} // ssalc
