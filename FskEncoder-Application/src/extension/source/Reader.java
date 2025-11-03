/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : Reader.java
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

package extension.source;

import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Responsibilities:<br>
 * Interface between InputReaderExtensionDao, BinReaderControl and ReaderBase
 * 
 * <p>
 * Collaborators:<br>
 * ReaderBase,<br>
 * InputReaderExtensionDao,<br>
 * BinReaderControl
 * 
 * <p>
 * Description:<br>
 * Defines the basic functionality of an input reader implementation.
 * 
 * <p>
 * @author Stefan
 *
 */

public interface Reader {

	
	/**
	 * 
	 * Retrieves the FileNameExtensionFilter which is defined in the subclass of ReaderBase.
	 * 
	 * @return
	 * : an instance of the currently defined FileNameExtensionFilter.
	 * 
	 */
	public FileNameExtensionFilter getFileNameExtensionFilter();
	
	
	/**
	 * 
	 * All methods in the derived class, constructor, setUp() and load(), must set
	 * an operationStatus after execution.<br>
	 * This operation status is meant to be displayed on the GUI as feedback to the user.
	 * 
	 * @return
	 * the currently userStatus.
	 * 
	 */
	public String getOperationStatus();

	
	/**
	 * 
	 * Encapsulates the functionality required to read and parse a source file.<br>
	 * It's also responsible for translation of errors (in form of Exceptions) into meaningfull
	 * operationStatus messages.
	 * 
	 * @return
	 * true if the operation was successful, false otherwise.
	 * 
	 * @throws IllegalAccessError
	 * if no source file is set.
	 * 
	 */
	public boolean loadFile() throws IllegalAccessError;

	
	/**
	 * 
	 * Set the filename and path of the source file which should be processed.
	 * 
	 * @param aFileName
	 * relative or absolute path and name of the candidate file.
	 * 
	 */
	public void setFilename(String aFileName); // setFilename()

	
}