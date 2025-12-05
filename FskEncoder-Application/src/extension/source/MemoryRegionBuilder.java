/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : MemoryRegionBuilder.java
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.model.ExtendableMemory;
import extension.model.MemoryRegion;

/**
 * Responsibilities:<br>
 * Abstract base class for specific XxxMemoryRegionBuilder implementations. <br>
 * Provides functionality for 
 * <ul>
 * 	<li>creation,</li>
 * 	<li>extending and </li>
 * 	<li>closing</li>
 *  </ul>
 *  of MemoryRegions <br>
 *  as well as a method to transfer the payload of a given DataRecord into the buffer of the current MemoryRegion.
 * 
 * <p>
 * Collaborators:<br>
 * A specific XxxMemoryRegionBuilder implementation<br>
 * MemoryMap <br>
 * MemoryRegion
 * 
 * <p>
 * Description:<br>
 * This abstract base class encapsulates the basic functionality to maintain MemoryRegion objects. 
 * The derived subclass must provide an interface to the Parser implementation on which specific implementations 
 * of the Record class are accepted, analyzed and processed.
 * 
 * <p>
 * @author Stefan
 *
 */

public abstract class MemoryRegionBuilder {

	private Logger logger = LogManager.getLogger(MemoryRegionBuilder.class.getName());
	
	protected long startAddress;
	
	protected MemoryRegion memoryRegion;
	
	protected ExtendableMemory memoryMap;
	
	/*
	 * Flag that indicates that an EOF record was received.
	 */
	protected boolean eofFlag = false;
	
	
	/**
	 * Default constructor.
	 * 
	 * @param aMemoryMap 
	 * to hold the MemoryRegions build by the derived class.
	 */
	public MemoryRegionBuilder(final ExtendableMemory aMemoryMap) { 
		logger.trace("MemoryRegionBuilder()");
		
		if(aMemoryMap == null) throw new IllegalArgumentException("aMemoryMap can't be null");
		
		memoryMap = aMemoryMap;
		
	} // MemoryRegionBuilder()
	
	
	/**
	 * 
	 * Derive classes must implement this method specific to the data / record format 
	 * of the source file.
	 * 
	 */
	
	public abstract void append(final DataRecord aDataRecord) throws ReaderException;

	
	/**
	 * Transfers the current MemoryRegion to the MemoryMap and set the current to null.
	 */
	
	protected void endRegion() {
		logger.trace("endRegion()");
		
		memoryMap.addRegion(memoryRegion);
		memoryRegion = null;
		
	} // endRegion()
	
	
	/**
	 * Extends the current MemoryRegion with the content of the given DataRecord.
	 * 
	 * @param aDataRecord
	 * the DataRecord to be used to extend the current MemoryMap.
	 */
	protected void extendRegion(final DataRecord aDataRecord) {
		logger.trace("extendRegion()");
		
		if(aDataRecord.getOffset() == (memoryRegion.getEndAddress() + 1)) {
			
			// Continuous block, just append
			unpackData(aDataRecord);
			
		}
		else {
			
			// A gap occurs between the last memory block and the 
			// loadOffset of the current record.
			// This indicates a new memory block
			endRegion();
			newRegion(aDataRecord);
			
		} // esle

	} // extendRegion()
	
	
	/**
	 * Creates a new MemoryRegion, ready to accept data.
	 * 
	 * @param aDataRecord
	 * A DataRecord to be inserted into the newly created MemoryRegion.
	 */
	protected void newRegion(final DataRecord aDataRecord) {
		logger.trace("newRegion()");
		
		startAddress = aDataRecord.getOffset();
		memoryRegion = new MemoryRegion(startAddress);

		logger.trace("newRegion(): startAddress = {}, memoryRegion = {}", startAddress, memoryRegion);
		
		unpackData(aDataRecord);
		
	} // newRegion()
	
	
	/**
	 * Returns a string representation of the current instance.
	 * 
	 * @return
	 * a string representing the current state.
	 */
	@Override
	public String toString() {
		
		return "MemoryRegionBuilder [" 
			+ "startAddress=" + String.format("0x%04X",startAddress) 
			+ ", memoryRegion=" + memoryRegion 
			+ ", memoryMap=" + memoryMap 
			+ "]";
		
	} // toString()


	/**
	 * Method who unpacks the payload of a given DataRecord and hands it over to the current MemoryRegion for expansion.
	 * 
	 * @param aDataRecord
	 * DataRecord to be unpacked.
	 */
	protected void unpackData(final DataRecord aDataRecord) {
		logger.trace("unpackData()");
		
		memoryRegion.addContent(aDataRecord.getData());

	} // unpackData()


	/**
	 * 
	 */
	public void clear() {
		logger.trace("clear()");
		
		eofFlag = false;
		memoryMap.clear();
		
	} // clear()
		
	
} // ssalc
