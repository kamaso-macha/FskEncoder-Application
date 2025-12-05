/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : TestTargetSystemExtensionFactory.java
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


package application.support;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.control.StatusMessenger;
import extension.factory.InputReaderExtensionFactory;
import extension.model.InputReaderExtensionDao;

/**
 * Responsibilities:<br>
 * InputReaderExtensionFactory implementation for test purpose
 * 
 * <p>
 * Collaborators:<br>
 * ApplicationTest,<br>
 * superclass
 * 
 * <p>
 * Description:<br>
 * Provides a implementation for test purpose.
 * 
 * <p>
 * @author Stefan
 *
 */

public class TestInputReaderExtension implements InputReaderExtensionFactory {

	private Logger logger = LogManager.getLogger(TestInputReaderExtension.class.getName());
	
	public TestInputReaderExtension() { }
	
	@Override 
	public InputReaderExtensionDao getInputReaderExtensions(StatusMessenger aStatusMessenger) {
		logger.trace("getInputReaderExtensions(): aStatusMessenger = {}", aStatusMessenger);
		
		if(aStatusMessenger == null) throw new IllegalArgumentException("aStatusMessenger can't be null");
		
		return new InputReaderExtensionDao(null, null, null, null); 
		
	} // getInputReaderExtensions()
	
} // ssalc
