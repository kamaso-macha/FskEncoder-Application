/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : Z80Extension.java
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
import extension.factory.TargetSystemExtensionFactory;
import extension.model.TargetSystemExtensionDao;


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
// Created at 2024-05-13 09:56:40

public class Z80TrainerMonitorExtension implements TargetSystemExtensionFactory {
	
	private Logger logger = LogManager.getLogger(Z80TrainerMonitorExtension.class.getName());

	
	@Override
	public TargetSystemExtensionDao getTargetSystemExtension(StatusMessenger aStatusMessenger) {
		logger.trace("getTargetSystemExtension()");
		
		if(aStatusMessenger == null) throw new IllegalArgumentException("aStatusMessenger can't be null");
		
		Z80TrainerMonitorExtensionControl control = new Z80TrainerMonitorExtensionControl(aStatusMessenger);
		
		logger.debug("control: {}", control);
		
		return new TargetSystemExtensionDao(control.getProtocol(), control.getGui(), control);
		
	} // getTargetSystemExtension()
	
	
} // ssalc
