/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : MemoryMap.java
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


package extension.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Responsibilities:<br>
 * Stores one or more discontinuous MemoryRegion instances.<br>
 * Provides a brief overview of the memory layout.<br>
 * Returns single MemoryRegion on request.<br>
 * 
 * <p>
 * Collaborators:<br>
 * Specific XxxReader implementation<br>
 * MemoryRegion<br>
 * MemoryBlockDescription<br>
 * 
 * <p>
 * Description:<br>
 * Keeps at least one or multiple MemoryRegion objects as a complete memory image. 
 * On request it creates a brief view of the entire memory layout by creating a list of MemoryBlockDescription objects 
 * and/or returns a specific MemoryRegion object.
 * 
 * <p>
 * @author Stefan
 *
 */

public class MemoryMap implements ExtendableMemory {

	private Logger logger = LogManager.getLogger(MemoryMap.class.getName());
	
				// Start Address, Memory Region
	protected Map<Long, MemoryRegion> memoryLayout;
	
	
	/**
	 * Empty default constructor. 
	 */
	public MemoryMap() {
	
		logger.trace("MemoryMap()");
		
		memoryLayout = new HashMap<>();
		
	} // MemoryMap()
	
	
	/**
	 * Adds a new MemoryRegion object to the internal collection of memory regions.
	 * 
	 * @param aRegion
	 * The MemoryRegion object to add to the internal collection.
	 * 
	 * @throws IllegalArgumentException
	 * if aRegion is null.
	 */
	
	@Override
	public void addRegion(final MemoryRegion aRegion) {
		logger.trace("addRegion()");
		
		if(aRegion == null) throw new IllegalArgumentException("aRegion can't be null");
		
		memoryLayout.put(aRegion.getStartAddress(), aRegion);
		logger.trace("memoryLayout.size = {}", memoryLayout.size());
		
	} // addRegion()
	
	
	/**
	 * 
	 */
	@Override
	public void clear() {
		logger.trace("clear(): memoryLayout.size = {}", memoryLayout.size());
		
		memoryLayout.clear();
		logger.trace("memoryLayout.size = {}", memoryLayout.size());
		
	} // clear()

	
	/**
	 * Creates a brief overview of the current memory layout.
	 * For each MemoryRegion in the collection a MemoryBlockDescription is created which held the start and end addresses
	 * along with the size of the region.
	 *  
	 * @return
	 * A list of MemoryBlockDescription objects.
	 */
	
	public List<MemoryBlockDescription> getMemoryLayout() { 
		logger.trace("getMemoryLayout()");
		
		List<MemoryBlockDescription> result = new ArrayList<>();
		MemoryBlockDescription memBlockDesc;
		MemoryRegion memRegion;
		
		logger.debug("number of available MemoryRegions = {}", memoryLayout.entrySet().size());
		
		for(Entry<Long, MemoryRegion> entry : memoryLayout.entrySet()) { // NOSONAR
			
			memRegion = (MemoryRegion) entry.getValue();
			memBlockDesc = new MemoryBlockDescription(memRegion.getStartAddress(), memRegion.getEndAddress(), memRegion.getSize());
			
			result.add(memBlockDesc);
			
		} // rof
		
		Collections.sort(result);
		
		return result; 
		
	} // getMemoryLayout()
	
	
	/**
	 * Returns a MemoryRegion object specified by it's start address.
	 *  
	 * @param aStartAddress
	 * The start address of the requested MemoryRegion.
	 * 
	 * @return
	 * The selected MemoryRegion object.
	 */
	public MemoryRegion getMemoryRegion(final long aStartAddress) {
		logger.trace("getMemoryRegion()");
		
		return memoryLayout.get(aStartAddress); 
		
	} // getMemoryRegion()
	
	
	/**
	 * Returns the number of currently held MemoryRegion objects.
	 * 
	 * @return
	 * Number of currently held MemoryRegion objects.
	 */
	public int getRegionCount() { return memoryLayout.size(); }


	/**
	 * Returns a string representation of the current instance.
	 * 
	 * @return
	 * A string representing the current state.
	 */
	@Override
	public String toString() {
		return "MemoryMap [memoryLayout=" + memoryLayout + "]";
	}


} // ssalc
