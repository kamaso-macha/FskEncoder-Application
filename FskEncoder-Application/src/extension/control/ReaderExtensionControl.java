/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : ExtensionControl.java
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


package extension.control;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import extension.model.InputReaderExtensionDao;
import extension.model.MemoryRegion;

/**
 * Responsibilities:<br>
 * Base class for Input Reader Extension controller.
 * 
 * <p>
 * Collaborators:<br>
 * ActionListener
 * 
 * <p>
 * Description:<br>
 * Abstract base class for input reader controller.
 * <p>
 * NOTE:<br>
 * Don't convert it to an interface!<br>
 * If it is an interface no typecast to the concrete implementation is possible!
 * 
 * <p>
 * @author Stefan
 *
 */

public abstract class ReaderExtensionControl implements ActionListener {	// NOSONAR

	
	/**
	 * 
	 * Derived class must implement this method.<br>
	 * On invocation it must create and return a subclass of ExtensionGui containing the 
	 * extension specific GUI elements.
	 * 
	 * @return
	 * : a subclass of ExtensionGui, containing the extension specific GUI elements.
	 * 
	 */
	public abstract JPanel createLayout();

	
	/**
	 * 
	 * Derived class must implement this method and must initialize itself if the method is invoked. 
	 * 
	 * @param aInputReaderExtensionDao 
	 * : a parameter set. See  {@link extension.model.InputReaderExtensionDao}.
	 * 
	 * @param workflowEngine
	 * : a instance of the workflowEngine class.
	 * 
	 */
	public abstract void initialize(InputReaderExtensionDao aInputReaderExtensionDao, StatusMessenger aStatusMessenger);

	
	/**
	 * 
	 * Derived class must implement this method to receive the file path of the candidate file.
	 * 
	 * @param aFilePath
	 * : the file FQ path of the candidate file.
	 *  
	 */
	public abstract void setFileName(String aFilePath);

	
	/**
	 * 
	 * Derived class must implement this method.
	 * On invocation it must perform the load of the candidate file.
	 * 
	 */
	public abstract void load();

	
	/**
	 * 
	 * Derived class must implement this method.
	 * On invocation it must return a List of all selected memory regions.
	 *  
	 * @return
	 * : a list of all selected memory regions.
	 */
	public abstract List<MemoryRegion> getSelectedMemoryRegions();
	
	
} // ssalc
