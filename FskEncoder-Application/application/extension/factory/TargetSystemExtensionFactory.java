/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : ProtocolFactory.java
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


package extension.factory;

import extension.control.StatusMessenger;
import extension.model.TargetSystemExtensionDao;

/**
 * Responsibilities:<br>
 * Defines the interface of Target System Extension Factories
 * 
 * <p>
 * Collaborators:<br>
 * None.
 * 
 * <p>
 * Description:<br>
 * Interface uses by the workflow engine to create and access the required factory.
 * 
 * <p>
 * @author Stefan
 *
 */

public interface TargetSystemExtensionFactory {

	/**
	 * 
	 * Method which creates the extension DAO holding the extension components.
	 * 
	 * @param aStatusMessenger
	 * A reference to the StatusMessenger interface of the workflow engine.
	 * 
	 * @return TargetSystemExtensionDao
	 * A DAO holding references to the extension components.
	 * <br>
	 * Please refer to {@link extension.model.TargetSystemExtensionDao} for more and detailed information 
	 * about the extension components.
	 * 
	 * @throws Exception 
	 * If an exception is thrown inside the factory, than it's re-thrown here. 
	 * 
	 */
	TargetSystemExtensionDao getTargetSystemExtension(StatusMessenger aStatusMessenger);
	
} // ssalc
