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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.model.MemoryMap;

/**
 * Responsibilities:<br>
 * Abstract base class acting as interface between the GUI controllers and the format specific file processor.
 * 
 * <p>
 * Collaborators:<br>
 * Specific file processor implementation.<br>
 * Several GUI controllers.
 * 
 * <p>
 * Description:<br>
 * Abstract base class defines the basic set of operations required to load a source file.
 * <p>
 * An application must - after construction of an instance - call setFilename() to set the candidate source file. 
 * This file can be processed as often as required but it's reopened on every call to load() method.
 * <p>
 * The derived class must set the operationStatus according to the result of the last executed operation which 
 * can - and should - be read by the application. At least it should be read if the result of load() was false!. 
 * 
 * <p>
 * @author Stefan
 *
 */

public abstract class ReaderBase implements Reader {

	private Logger logger = LogManager.getLogger(ReaderBase.class.getName());
	
	protected MemoryMap memoryMap;	
	protected String sourceFileName;
	protected String operationStatus;

	protected FileNameExtensionFilter filter;

	
	
	/**
	 * Constructor.
	 * 
	 * @param aMemoryMap
	 * to collect and keep all MemoryRegions defined in the source file.
	 * 
	 * @throws IllegalArgumentException
	 * if aMemoryMap is null.
	 */
	public ReaderBase(final MemoryMap aMemoryMap) throws IllegalArgumentException {
		
		if(aMemoryMap == null) throw new IllegalArgumentException("aMemoryMap can't be null");
		
		logger.trace("Reader(): aMemoryMap = {}", aMemoryMap);
		memoryMap = aMemoryMap;
		
	} // Reader(...)
	
	
	/**
	 * 
	 */
	public FileNameExtensionFilter getFileNameExtensionFilter() { return filter; }
	
	
	/**
	 * All methods in the derived class, constructor, setUp() and load(), must set
	 * an operationStatus after execution.<br>
	 * This operation status is meant to be displayed on the GUI as feedback to the user.
	 * 
	 * @return
	 * the currently userStatus.
	 */
	@Override
	public String getOperationStatus() { return operationStatus; }
	
	
	/**
	 * Encapsulates the functionality required to read and parse a source file.<br>
	 * It's also responsible for translation of errors (in form of Exceptions) into meaningfull
	 * operationStatus messages.
	 * 
	 * @return
	 * true if the operation was successful, false otherwise.
	 * 
	 * @throws IllegalAccessError
	 * if no source file is set.
	 */
	@Override
	public abstract boolean loadFile() throws IllegalAccessError;
	
	
	/**
	 * Set the filename and path of the source file which should be processed.
	 * 
	 * @param aFileName
	 * relative or absolute path and name of the candidate file.
	 */
	@Override
	public void setFilename(final String aFileName) {
		logger.trace("setFilename(): aFileName = {}", aFileName);
		
		if(aFileName == null) throw new IllegalArgumentException("aFileName can't be null");
		if(aFileName.isBlank()) throw new IllegalArgumentException("aFileName can't be blank");
		
		sourceFileName = aFileName;
		
	} // setFilename()
		

	/**
	 * Returns a string representation of the current instance.
	 * 
	 * @return
	 * a string representing the current state of this object. 
	 */
	@Override
	public String toString() {
		return "Reader [memoryMap=" + memoryMap + ", sourceFileName=" + sourceFileName + ", operationStatus="
				+ operationStatus + "]";
	}

	
} // ssalc
