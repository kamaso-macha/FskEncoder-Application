/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : MemoryBlock.java
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


package extension.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Responsibilities:<br>
 * Brief description of a MemoryRegion
 * 
 * <p>
 * Collaborators:<br>
 * None.
 * 
 * <p>
 * Description:<br>
 * DAO which can represent the start and end address along with the size of a MemoryBlock. <br>
 * The attributes are made final for a quick access to them. 
 * 
 * <p>
 * @author Stefan
 *
 */

public class MemoryBlockDescription implements Comparable<MemoryBlockDescription> {
	
	private Logger logger = LogManager.getLogger(MemoryBlockDescription.class.getName());

	public final long START_ADDRESS;	// NOSONAR>
	public final long END_ADDRESS;		// NOSONAR>
	public final int  SIZE				// NOSONAR>
	;
	
	
	/**
	 *  Default constructor.
	 *  
	 * @param aStartAddress
	 * 0 <= aStartAddress < aEndAddress
	 *  
	 * @param aEndAddress
	 * (aStartAddress + aSize - 1) == aEndAddress
	 *  
	 * @param aSize
	 * 0 < aSize
	 *
	 * @throws IllegalArgumentException <br>
	 * IF aSize == 0 <br>
	 * or aEndAddress != aStartAddress + aSize - 1 <br>
	 * or aStartAddress  >= aEndAddress
	 * 
	 */
	public MemoryBlockDescription(final long aStartAddress, final long aEndAddress, final int aSize) {
		
		logger.trace(String.format("aStartAddress = 0x%04X, aEndAddress = 0x%04X, aSize = 0x%04X", 
				aStartAddress, aEndAddress, aSize));
		
		if(aSize == 0) throw new IllegalArgumentException("aSize can't be 0");
		if(aStartAddress > aEndAddress) throw new IllegalArgumentException("Illegal value for aStartAddress or aEndAddress: aStartAddress  > aEndAddress");
		if(aStartAddress + aSize - 1 != aEndAddress) throw new IllegalArgumentException("Illegal value for aEndAddress or aSize: aStartAddress + aSize - 1 != aEndAddress");
		
		START_ADDRESS = aStartAddress;
		SIZE          = aSize;
		END_ADDRESS   = aEndAddress;
		
	}


	/**
	 * To be sortable. Sorting is done by comparison of the start addresses.
	 * 
	 * @return<br>
	 * A negative number if the start address of the candidate is less than this start address. <br>
	 * Zero if the start address of the candidate is equal to this start address. <br>
	 * A positive number if the start address of the candidate is above this start address. <br>
	 */
	@Override
	public int compareTo(MemoryBlockDescription aCandidate) {
		
		return (int) (this.START_ADDRESS - aCandidate.START_ADDRESS);
		
	} // compareTo()


	/**
	 * Returns a string representation of the current state.
	 * 
	 * @return
	 * A string representing the current state.
	 */
	@Override
	public String toString() {
		
		return "MemoryBlockDescription ["
				+ "START_ADDRESS=" + String.format("0x%04X", START_ADDRESS) 
				+ ", END_ADDRESS=" + String.format("0x%04X", END_ADDRESS)
				+ ", SIZE=" + String.format("0x%02X", SIZE) + " / " + SIZE
				+ "]";
		
	} // toString()


} // ssalc
